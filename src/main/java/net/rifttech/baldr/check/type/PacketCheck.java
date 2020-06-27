package net.rifttech.baldr.check.type;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R3.PacketListenerPlayOut;
import net.rifttech.baldr.check.Check;
import net.rifttech.baldr.player.PlayerData;
import org.bukkit.entity.Player;

public abstract class PacketCheck extends Check<Packet<PacketListenerPlayIn>> {
    public PacketCheck(PlayerData playerData, String name) {
        super(playerData, name);
    }

    @Override
    public abstract void handle(Player player, Packet<PacketListenerPlayIn> packet);

    public void handleOutbound(Player player, Packet<PacketListenerPlayOut> packet) { }
}
