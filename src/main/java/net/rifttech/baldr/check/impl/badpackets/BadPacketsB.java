package net.rifttech.baldr.check.impl.badpackets;

import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.rifttech.baldr.check.type.packetorder.InversePacketOrderCheck;
import net.rifttech.baldr.player.PlayerData;

public class BadPacketsB extends InversePacketOrderCheck {
    public BadPacketsB(PlayerData playerData) {
        super(playerData, "Bad Packets B",
                packet -> packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig) packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM,
                packet -> packet instanceof PacketPlayInBlockPlace);
    }
}
