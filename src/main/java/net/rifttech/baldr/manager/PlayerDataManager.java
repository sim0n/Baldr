package net.rifttech.baldr.manager;

import net.rifttech.baldr.player.PlayerData;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final Map<UUID, PlayerData> dataMap = new HashMap<>();

    public Collection<PlayerData> getPlayers() {
        return dataMap.values();
    }

    public void registerData(Player player) {
        dataMap.put(player.getUniqueId(), new PlayerData(player));
    }

    public PlayerData getData(Player player) {
        return dataMap.get(player.getUniqueId());
    }

    public void removeData(Player player) {
        dataMap.remove(player.getUniqueId());
    }
}
