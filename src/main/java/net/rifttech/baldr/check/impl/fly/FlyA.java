package net.rifttech.baldr.check.impl.fly;

import net.rifttech.baldr.check.type.movement.PositionCheck;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.player.tracker.impl.MovementTracker;
import net.rifttech.baldr.util.update.MovementUpdate;
import org.bukkit.entity.Player;

/**
 * This is a gravity check, it enforces the game physics (on the vertical axis) and compares the player vertical move
 * to our game physics enforced estimation from the last vertical move
 */
public class FlyA extends PositionCheck {
    private final MovementTracker movementTracker = playerData.getMovementTracker();

    private double lastOffsetY;

    public FlyA(PlayerData playerData) {
        super(playerData, "Fly A");
    }

    @Override
    public void handle(Player player, MovementUpdate update) {
        double offsetY = update.getTo().getY() - update.getFrom().getY();

        if (update.getTo().getY() % ON_GROUND == 0 || movementTracker.isTeleporting())
            return;

        double estimatedOffsetY = (lastOffsetY - 0.08) * VERTICAL_AIR_FRICTION;

        if (Math.abs(estimatedOffsetY - offsetY) > 0.005) {
            if ((violations += 10) > 40) {
                alert(player, String.format("O %.3f", offsetY));
            }
        } else {
            decreaseVl(8);
        }

        lastOffsetY = offsetY;
    }
}
