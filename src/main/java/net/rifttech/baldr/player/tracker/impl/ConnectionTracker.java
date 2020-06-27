package net.rifttech.baldr.player.tracker.impl;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTransaction;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.player.tracker.PlayerTracker;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ConnectionTracker extends PlayerTracker {
    @Getter
    private boolean receivedKeepAlive;

    private final Map<Short, Long> transactionTimes = new HashMap<>();

    private short transactionID = Short.MIN_VALUE;

    @Getter
    private long transactionPing;

    private final Map<Integer, Long> keepAliveTimes = new HashMap<>();

    @Getter
    private long keepAlivePing;

    public ConnectionTracker(Player player, PlayerData playerData) {
        super(player, playerData);
    }

    public void handleTick() {
        if (++transactionID > 0) // don't want to interfere with actual transaction packets
            transactionID = Short.MIN_VALUE;

        transactionTimes.put(transactionID, System.currentTimeMillis());

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutTransaction(0, transactionID, false));
    }

    public void handleTransaction(short id) {
        if (id < 0)
            return;

        if (transactionTimes.containsKey(id)) {
            transactionPing = System.currentTimeMillis() - transactionTimes.get(id);
        } else {
            // The player sent a transaction we didn't send them, could flag for it
        }
    }

    public void handleInboundKeepAlive(int id) {
        if (keepAliveTimes.containsKey(id)) {
            receivedKeepAlive = true;

            keepAlivePing = System.currentTimeMillis() - keepAliveTimes.get(id);
        } else {
            // The player sent a keep alive we didn't send them, could flag for it
        }
    }

    public void handleOutboundKeepAlive(int id) {
        keepAliveTimes.put(id, System.currentTimeMillis());
    }
}
