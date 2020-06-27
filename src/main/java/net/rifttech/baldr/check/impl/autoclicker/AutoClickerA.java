package net.rifttech.baldr.check.impl.autoclicker;

import net.rifttech.baldr.check.type.swing.AirSwingCheck;
import net.rifttech.baldr.player.PlayerData;
import org.bukkit.entity.Player;

public class AutoClickerA extends AirSwingCheck {
    private static final double MAX_ALLOWED_CPS = 20;

    public AutoClickerA(PlayerData playerData) {
        super(playerData, "Auto Clicker A", 40);
    }

    @Override
    public void handle(Player player) {
        double cps = getCPS(delays);

        if (cps > MAX_ALLOWED_CPS) {
            alert(player, String.format("CPS %.1f", cps));
        }
    }
}
