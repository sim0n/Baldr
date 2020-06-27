package net.rifttech.baldr.check.impl.badpackets;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.rifttech.baldr.check.type.PacketCheck;
import net.rifttech.baldr.player.PlayerData;
import org.bukkit.entity.Player;

/**
 * The client follows a packet sequence where there has to has be at least 1 position update every 20 flying packets
 */
public class BadPacketsA extends PacketCheck {
    private int streak;

    public BadPacketsA(PlayerData playerData) {
        super(playerData, "Bad Packets A");
    }

    @Override
    public void handle(Player player, Packet<PacketListenerPlayIn> packet) {
        if (packet instanceof PacketPlayInFlying) {
            if (((PacketPlayInFlying) packet).g() || player.isInsideVehicle()) {
                streak = 0;

                return;
            }

            if (++streak > 20) {
                alert(player, String.format("STR %d", streak));
            }
        }
    }
}
