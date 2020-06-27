package net.rifttech.baldr.check.impl.autoclicker;

import net.rifttech.baldr.check.type.swing.AirSwingCheck;
import net.rifttech.baldr.player.PlayerData;
import org.bukkit.entity.Player;

public class AutoClickerB extends AirSwingCheck {
    public AutoClickerB(PlayerData playerData) {
        super(playerData, "Auto Clicker B", 100);
    }

    @Override
    public void handle(Player player) {
        double cps = getCPS(delays);
        double stDev = getStandardDeviation(delays);

        if (stDev < 0.45) {
            if ((violations += 3) > 9) {
                alert(player, String.format("SD %.2f, CPS %.1f", stDev, cps));
            }
        } else {
            decreaseVl(1);
        }

    }
}
