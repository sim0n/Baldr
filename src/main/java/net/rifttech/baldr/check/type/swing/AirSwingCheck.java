package net.rifttech.baldr.check.type.swing;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.rifttech.baldr.check.type.PacketCheck;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.player.tracker.impl.ActionTracker;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class AirSwingCheck extends PacketCheck {
    private final ActionTracker actionTracker = playerData.getActionTracker();
    protected final List<Integer> delays;

    private final int maxSamples;

    private int movements;

    public AirSwingCheck(PlayerData playerData, String name, int maxSamples) {
        super(playerData, name);

        this.maxSamples = maxSamples;

        // We're providing an initial capacity considering we don't want to reallocate and expand the array
        delays = new ArrayList<>(maxSamples);
    }

    @Override
    public void handle(Player player, Packet<PacketListenerPlayIn> packet) {
        if (packet instanceof PacketPlayInArmAnimation && !actionTracker.isDigging()) {
            if (movements < 10) {
                delays.add(movements);

                if (delays.size() == maxSamples) {
                    handle(player);

                    delays.clear();
                }
            }

            movements = 0;
        } else if (packet instanceof PacketPlayInFlying) {
            ++movements;
        }
    }

    public abstract void handle(Player player);
}
