package net.rifttech.baldr.check.type.movement;

import net.rifttech.baldr.check.Check;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.util.update.MovementUpdate;
import org.bukkit.entity.Player;

public abstract class PositionCheck extends Check<MovementUpdate> {
    public PositionCheck(PlayerData playerData, String name) {
        super(playerData, name);
    }

    @Override
    public abstract void handle(Player player, MovementUpdate update);
}
