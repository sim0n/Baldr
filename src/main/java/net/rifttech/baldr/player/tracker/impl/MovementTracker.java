package net.rifttech.baldr.player.tracker.impl;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.player.tracker.PlayerTracker;
import net.rifttech.baldr.util.location.CustomLocation;
import net.rifttech.baldr.util.update.MovementUpdate;
import net.rifttech.baldr.util.velocity.Velocity;
import net.rifttech.baldr.wrapper.impl.clientbound.EntityVelocityWrapper;
import net.rifttech.baldr.wrapper.impl.clientbound.PositionWrapper;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Deque;
import java.util.LinkedList;

@Getter
public class MovementTracker extends PlayerTracker {
    private CustomLocation lastLocation;
    private CustomLocation teleportLocation;

    private final Deque<Velocity> velocities = new LinkedList<>();

    private boolean resetTeleport;

    public MovementTracker(Player player, PlayerData playerData) {
        super(player, playerData);
    }

    public void handleFlying(PacketPlayInFlying flying) {
        CustomLocation location = new CustomLocation(flying.a(), flying.b(), flying.c(), flying.d(), flying.e());

        boolean moved = flying.g();
        boolean looked = flying.h();

        if (lastLocation != null) {
            if (!moved) {
                location.setX(lastLocation.getX());
                location.setY(lastLocation.getY());
                location.setZ(lastLocation.getZ());
            }

            if (!looked) {
                location.setYaw(lastLocation.getYaw());
                location.setPitch(lastLocation.getPitch());
            }

            if (location.getX() != lastLocation.getX() ||
                    location.getY() != lastLocation.getY() ||
                    location.getZ() != lastLocation.getZ()) {
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    playerData.getCheckData().getPositionChecks()
                            .forEach(check -> check.handle(player, new MovementUpdate(lastLocation, location)));
                }
            }
        }

        if (moved && resetTeleport) {
            teleportLocation = null;
            resetTeleport = false;
        }

        if (moved && teleportLocation != null) {
            double x = Math.abs(location.getX() - teleportLocation.getX());
            double z = Math.abs(location.getZ() - teleportLocation.getZ());

            if (x <= 0.005 || z <= 0.005)
                resetTeleport = true;
        }

        lastLocation = location;
        // We only want to iterate through every entry once
        velocities.removeIf(velocity -> {
            velocity.onMove();

            return velocity.hasExpired();
        });
    }

    public boolean isTeleporting() {
        return teleportLocation != null;
    }

    /**
     * You're going to want to make something better than this, a good start would be checking if the player
     * moved very close to the position the server sent
     * @param packet The position packet the server sent
     */
    public void handleServerPosition(PacketPlayOutPosition packet) {
        PositionWrapper position = new PositionWrapper(packet);

        teleportLocation = new CustomLocation(position.getX(), position.getY(), position.getZ(), 0F, 0F);
    }

    public void handleVelocity(PacketPlayOutEntityVelocity packet) {
        EntityVelocityWrapper entityVelocity = new EntityVelocityWrapper(packet);

        if (entityVelocity.getId() != player.getEntityId())
            return;

        double velocityX = Math.abs(entityVelocity.getX() / 8000D);
        double velocityY = entityVelocity.getY() / 8000D;
        double velocityZ = Math.abs(entityVelocity.getZ() / 8000D);

        velocities.add(new Velocity(velocityX, velocityY, velocityZ));
    }
}
