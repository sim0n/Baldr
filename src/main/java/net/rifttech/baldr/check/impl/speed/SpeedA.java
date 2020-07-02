package net.rifttech.baldr.check.impl.speed;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.rifttech.baldr.check.type.movement.PositionCheck;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.player.tracker.impl.MovementTracker;
import net.rifttech.baldr.player.tracker.impl.StatusTracker;
import net.rifttech.baldr.util.location.CustomLocation;
import net.rifttech.baldr.util.update.MovementUpdate;
import net.rifttech.baldr.util.velocity.Velocity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * <p>This is a simple friction check, all it does is follow the game movement and check if the client has it
 * enforced, if it doesn't then they're cheating</p>
 */
public class SpeedA extends PositionCheck {
    private final StatusTracker statusTracker = playerData.getStatusTracker();
    private final MovementTracker movementTracker = playerData.getMovementTracker();

    private double lastOffsetH;
    private double blockFriction = 0.91;

    public SpeedA(PlayerData playerData) {
        super(playerData, "Speed A");
    }

    @Override
    public void handle(Player player, MovementUpdate update) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        CustomLocation from = update.getFrom();
        CustomLocation to = update.getTo();

        double offsetH = hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
        double offsetY = to.getY() - from.getY();

        double movementSpeed = statusTracker.getMovementSpeed();
        double jumpHeight = 0.42 + statusTracker.getJumpBoost() * 0.1;

        if (entityPlayer.onGround) {
            /*
             * Don't always assume the player is sprinting, it's better to
             * make a condition for it in {@link net.rifttech.baldr.player.tracker.impl.ActionTracker}
             */
            movementSpeed *= 1.3;

            if (getMoveAngle(from, to) > 90) // The player is sprinting in another direction
                movementSpeed /= 1.05;

            movementSpeed *= 0.16277136 / Math.pow(blockFriction, 3);

            if (offsetY > 0.00001 && offsetY < jumpHeight) {
                movementSpeed += 0.2; // jump speed boost
            }
        } else {
            movementSpeed = 0.026;
            blockFriction = 0.91;
        }

        // Velocity usage
        movementSpeed += movementTracker.getVelocities().stream()
                .mapToDouble(Velocity::getHorizontal)
                .max()
                .orElse(0D);

        double speedup = (offsetH - lastOffsetH) / movementSpeed;

        if (speedup > 1D && !movementTracker.isTeleporting()) {
            if ((violations += 10) > 45) {
                alert(player, String.format("P %d%%", Math.round(speedup * 100D)));
            }
        } else {
            decreaseVl(1);
        }

        BlockPosition blockPosition = new BlockPosition(
                Math.floor(to.getX()), Math.floor(to.getY() - 1), Math.floor(to.getZ()));

        lastOffsetH = offsetH * blockFriction;
        blockFriction = entityPlayer.world.getType(blockPosition).getBlock().frictionFactor * 0.91;
    }
}
