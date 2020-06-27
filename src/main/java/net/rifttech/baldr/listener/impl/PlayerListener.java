package net.rifttech.baldr.listener.impl;

import io.netty.channel.Channel;
import me.lucko.helper.Events;
import net.rifttech.baldr.Baldr;
import net.rifttech.baldr.listener.Listener;
import net.rifttech.baldr.manager.AlertManager;
import net.rifttech.baldr.manager.PlayerDataManager;
import net.rifttech.baldr.packet.PacketHandler;
import net.rifttech.baldr.util.PermissionNode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlayerListener implements Listener {
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    private final Baldr plugin = Baldr.getInstance();

    private final PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
    private final AlertManager alertManager = plugin.getAlertManager();

    @Override
    public void register() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(event -> {
                    Player player = event.getPlayer();

                    if (player.isOp() || player.hasPermission(PermissionNode.ALERTS))
                        alertManager.toggleAlerts(player);

                    playerDataManager.registerData(player);

                    EXECUTOR.execute(() -> {
                        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
                        PacketHandler packetHandler = new PacketHandler(playerDataManager.getData(player));

                        channel.pipeline().addBefore("packet_handler", "baldr-handler", packetHandler);
                    });
                }).bindWith(plugin);

        Events.subscribe(PlayerQuitEvent.class)
                .handler(event -> {
                    Player player = event.getPlayer();

                    // This is quicker than to check permission first
                    alertManager.getAlerts().remove(player.getUniqueId());

                    playerDataManager.removeData(player);

                    EXECUTOR.execute(() -> {
                        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;

                        try {
                            channel.pipeline().remove("baldr-handler");
                        } catch (Throwable ignored) { }
                    });
                }).bindWith(plugin);
    }
}
