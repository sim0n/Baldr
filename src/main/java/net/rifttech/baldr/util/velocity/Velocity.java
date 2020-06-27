package net.rifttech.baldr.util.velocity;

import lombok.Getter;
import net.rifttech.baldr.helper.MathHelper;

@Getter
public class Velocity implements MathHelper {
    private final double x, y, z;
    private final double horizontal;

    private int ticksExisted;

    public Velocity(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        horizontal = hypot(x, z);

        // These are the ticks that this velocity should be alive for
        ticksExisted = (int) (((x + z) / 2D + 2D) * 15D);
    }

    // this is better than using a setter in our movement tracker
    public void onMove() {
        --ticksExisted;
    }

    /**
     * Checks if {@link #ticksExisted} is greater than 0
     *
     * @return Whether it has expired
     */
    public boolean hasExpired() {
        return ticksExisted < 0;
    }
}
