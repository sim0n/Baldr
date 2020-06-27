package net.rifttech.baldr.check.impl.autoclicker;

import net.rifttech.baldr.check.type.swing.AirSwingCheck;
import net.rifttech.baldr.player.PlayerData;
import org.bukkit.entity.Player;

public class AutoClickerC extends AirSwingCheck {
    public AutoClickerC(PlayerData playerData) {
        super(playerData, "Auto Clicker C", 500);
    }

    @Override
    public void handle(Player player) {
        double cps = getCPS(delays);
        double kurtosis = getKurtosis(delays);

        if (kurtosis < 0D) {
            if ((violations += 3) > 4) {
                alert(player, String.format("KU %.3f, CPS %.1f", kurtosis, cps));
            }
        } else {
            decreaseVl(1);
        }
    }
}
