package net.rifttech.baldr.check.impl.killaura;

import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.rifttech.baldr.check.type.packetorder.PacketOrderCheck;
import net.rifttech.baldr.player.PlayerData;

/**
 * This is a no swing check, it checks if the player attacked before swinging
 */
public class KillAuraA extends PacketOrderCheck {
    public KillAuraA(PlayerData playerData) {
        super(playerData, "Kill Aura A",
                packet -> packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK,
                packet -> packet instanceof PacketPlayInArmAnimation);
    }
}
