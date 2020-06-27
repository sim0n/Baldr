package net.rifttech.baldr.helper;

import net.rifttech.baldr.util.location.CustomLocation;

public interface TrigHelper {

    /**
     * Gets the angle between {@param from} and {@param to} and subtracts with the direction of {@param to}
     * @param from The from location
     * @param to The to location
     * @return The move angle
     */
    default double getMoveAngle(CustomLocation from, CustomLocation to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();

        double moveAngle = Math.toDegrees(Math.atan2(dz, dx)) - 90D; // have to subtract by 90 because minecraft does it

        return Math.abs(wrapAngleTo180_double(moveAngle - to.getYaw()));
    }

    /**
     * Wrap a degree measure to 180 degrees. Used for yaw calculation in checks
     * @param value The inputted yaw value
     * @return the wrapped value
     */
    default float wrapAngleTo180_float(float value) {
        value %= 360F;

        if (value >= 180.0F)
            value -= 360.0F;

        if (value < -180.0F)
            value += 360.0F;

        return value;
    }

    /**
     * Wrap a degree measure to 180 degrees. Used for yaw calculation in checks
     * @param value The inputted yaw value
     * @return the wrapped value
     */
    default double wrapAngleTo180_double(double value) {
        value %= 360D;

        if (value >= 180D)
            value -= 360D;

        if (value < -180D)
            value += 360D;

        return value;
    }
}
