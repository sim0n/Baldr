package net.rifttech.baldr.manager;

import lombok.Getter;
import me.lucko.helper.Schedulers;
import net.rifttech.baldr.check.Check;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class AlertManager {
    private final Set<UUID> alerts = new HashSet<>();

    public void toggleAlerts(Player player) {
        UUID uuid = player.getUniqueId();

        if (!alerts.remove(uuid))
            alerts.add(uuid);
    }

    public void handleAlert(Check<?> check, String details) {
        Schedulers.async().run(() -> {
            check.setViolations(check.getViolations() + 1);

            String message = details + " " + check.getViolations();
            
            alerts.stream()
                    .map(Bukkit::getPlayer)
                    .forEach(player -> player.sendMessage(message));
        });
    }
}
