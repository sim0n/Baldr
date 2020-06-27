package net.rifttech.baldr.check;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.rifttech.baldr.Baldr;
import net.rifttech.baldr.helper.MathHelper;
import net.rifttech.baldr.helper.TrigHelper;
import net.rifttech.baldr.manager.AlertManager;
import net.rifttech.baldr.player.PlayerData;
import org.atteo.classindex.IndexSubclasses;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@IndexSubclasses
@RequiredArgsConstructor
public abstract class Check<T> implements MathHelper, TrigHelper {
    private final AlertManager alertManager = Baldr.getInstance().getAlertManager();

    protected final PlayerData playerData;

    private final String name;

    // This can be used to check how many times someone set off an alert in an x amount of time
    private final Set<Long> alertTimes = new HashSet<>();

    // Can also be changed to a double if that's your kind of thing
    protected int violations;

    public void alert(Player player) {
        alertTimes.add(System.currentTimeMillis());

        alertManager.handleAlert(this, String.format("§c[AC] §f%s §7failed §c%s §7VL", player.getName(), name));
    }

    public void alert(Player player, String details) {
        alertTimes.add(System.currentTimeMillis());

        alertManager.handleAlert(this, String.format("§c[AC] §f%s §7failed §c%s §7%s VL", player.getName(), name, details));
    }

    protected void decreaseVl(int decrement) {
        violations = Math.max(0, violations - decrement);
    }

    public abstract void handle(Player player, T t);
}
