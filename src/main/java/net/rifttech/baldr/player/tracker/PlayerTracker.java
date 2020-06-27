package net.rifttech.baldr.player.tracker;

import lombok.RequiredArgsConstructor;
import net.rifttech.baldr.player.PlayerData;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public abstract class PlayerTracker {
    protected final Player player;
    protected final PlayerData playerData;
}
