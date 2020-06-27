package net.rifttech.baldr.check.impl.autoclicker;

import net.rifttech.baldr.check.type.swing.AirSwingCheck;
import net.rifttech.baldr.player.PlayerData;
import org.bukkit.entity.Player;

/**
 * For us an outlier would be anything less than or equal to 5 cps
 */
public class AutoClickerE extends AirSwingCheck {
    private static final int OUTLIER = 3;

    public AutoClickerE(PlayerData playerData) {
        super(playerData, "Auto Clicker E", 1000);
    }

    @Override
    public void handle(Player player) {
        double cps = getCPS(delays);

        int outliers = (int) delays.stream()
                .filter(delay -> delay > OUTLIER)
                .count();

        if (outliers < 8) {
            if ((violations += 14) > 40) {
                alert(player, String.format("O %d, CPS %.1f", outliers, cps));
            }
        } else {
            decreaseVl(8);
        }
    }
}
