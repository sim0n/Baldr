package net.rifttech.baldr.check.impl.killaura;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.rifttech.baldr.check.type.PacketCheck;
import net.rifttech.baldr.player.PlayerData;
import org.bukkit.entity.Player;

public class KillAuraB extends PacketCheck {
    private int legalHits, illegalHits;

    private int movements, lastMovements;

    public KillAuraB(PlayerData playerData) {
        super(playerData, "Kill Aura B");
    }

    @Override
    public void handle(Player player, Packet<PacketListenerPlayIn> packet) {
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            if (movements < 10) {
                if (movements == lastMovements)
                    ++illegalHits;
                else
                    ++legalHits;

                if (legalHits + illegalHits == 30) {
                    if (illegalHits > 24) { // 80%
                        if ((violations += 12) > 20) {
                            alert(player, String.format("R %.1f", illegalHits / 30D));
                        }
                    } else {
                        decreaseVl(4);
                    }

                    legalHits = illegalHits = 0;
                }
            }

            lastMovements = movements;
            movements = 0;
        } else if (packet instanceof PacketPlayInFlying) {
            ++movements;
        }
    }
}
