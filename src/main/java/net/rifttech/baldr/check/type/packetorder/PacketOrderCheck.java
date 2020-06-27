package net.rifttech.baldr.check.type.packetorder;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.rifttech.baldr.check.type.PacketCheck;
import net.rifttech.baldr.player.PlayerData;
import org.bukkit.entity.Player;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>This will check if {@second} was sent with a flying in-between </p>
 * if the player fails to do this then it will alert
 */
public abstract class PacketOrderCheck extends PacketCheck {
    private boolean sent;

    private final Predicate<Packet<?>> first;
    private final Predicate<Packet<?>> second;

    public PacketOrderCheck(PlayerData playerData, String name,
                            Predicate<Packet<?>> first, Predicate<Packet<?>> second) {
        super(playerData, name);

        this.first = first;
        this.second = second;
    }

    @Override
    public void handle(Player player, Packet<PacketListenerPlayIn> packet) {
        if (first.test(packet)) {
            if (sent)
                return;

            // alert
            alert(player);
        } else if (second.test(packet)) {
            sent = true;
        } else if (packet instanceof PacketPlayInFlying) {
            sent = false; // reset on flying
        }
    }
}
