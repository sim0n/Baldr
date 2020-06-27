package net.rifttech.baldr.player.tracker.impl;

import net.minecraft.server.v1_8_R3.*;
import net.rifttech.baldr.check.CheckData;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.player.tracker.PlayerTracker;
import net.rifttech.baldr.wrapper.impl.clientbound.KeepAliveWrapper;
import org.bukkit.entity.Player;

// This entire class should be replaced with a packet system
public class PacketTracker extends PlayerTracker {
    private final ConnectionTracker connectionTracker = playerData.getConnectionTracker();
    private final MovementTracker movementTracker = playerData.getMovementTracker();
    private final ActionTracker actionTracker = playerData.getActionTracker();
    private final StatusTracker statusTracker = playerData.getStatusTracker();

    private final CheckData checkData = playerData.getCheckData();

    private int serverTicks;

    public PacketTracker(Player player, PlayerData playerData) {
        super(player, playerData);
    }

    public void handleTick() {
        if (++serverTicks % 3 == 0) {
            connectionTracker.handleTick();
        }
    }

    public void handleInbound(Packet<PacketListenerPlayIn> packet) {
        if (packet instanceof PacketPlayInBlockDig) {
            actionTracker.handleBlockDig((PacketPlayInBlockDig) packet);
        } else if (packet instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity useEntity = (PacketPlayInUseEntity) packet;

            actionTracker.handleUseEntity(useEntity);
        } else if (packet instanceof PacketPlayInFlying) {
            movementTracker.handleFlying((PacketPlayInFlying) packet);
        } else if (packet instanceof PacketPlayInTransaction) {
            short id = ((PacketPlayInTransaction) packet).b();

            connectionTracker.handleTransaction(id);
            statusTracker.handle();
        } else if (packet instanceof PacketPlayInKeepAlive) {
            int id = ((PacketPlayInKeepAlive) packet).a();

            connectionTracker.handleInboundKeepAlive(id);
        }

        checkData.getPacketChecks().forEach(check -> check.handle(player, packet));
    }

    public void handleOutbound(Packet<PacketListenerPlayOut> packet) {
        if (packet instanceof PacketPlayOutKeepAlive) {
            KeepAliveWrapper keepAliveWrapper = new KeepAliveWrapper((PacketPlayOutKeepAlive) packet);

            connectionTracker.handleOutboundKeepAlive(keepAliveWrapper.getId());
        } else if (packet instanceof PacketPlayOutPosition) {
            movementTracker.handleServerPosition((PacketPlayOutPosition) packet);
        } else if (packet instanceof PacketPlayOutEntityVelocity) {
            movementTracker.handleVelocity((PacketPlayOutEntityVelocity) packet);
        }

        checkData.getPacketChecks().forEach(check -> check.handleOutbound(player, packet));
    }
}
