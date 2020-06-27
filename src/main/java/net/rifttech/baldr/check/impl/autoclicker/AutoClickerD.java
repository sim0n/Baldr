package net.rifttech.baldr.check.impl.autoclicker;

import net.rifttech.baldr.check.type.swing.AirSwingCheck;
import net.rifttech.baldr.player.PlayerData;
import org.bukkit.entity.Player;

/**
 * This checks for the difference between the current and last standard deviation. A decent amount of
 * auto clickers still
 */
public class AutoClickerD extends AirSwingCheck {
    private double lastStDev = -1; // default value

    public AutoClickerD(PlayerData playerData) {
        super(playerData, "Auto Clicker D", 50);
    }

    @Override
    public void handle(Player player) {
        double cps = getCPS(delays);
        double stDev = getStandardDeviation(delays);

        if (Math.abs(stDev - lastStDev) < 0.05) {
            if ((violations += 3) > 12) {
                alert(player, String.format("SD %.2f, CPS %.1f", stDev, cps));
            }
        } else {
            decreaseVl(1);
        }

        lastStDev = stDev;
    }
}
