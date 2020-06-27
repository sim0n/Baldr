package net.rifttech.baldr.wrapper.impl.clientbound;

import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import net.rifttech.baldr.wrapper.ClassWrapper;

public class PositionWrapper extends ClassWrapper<PacketPlayOutPosition> {
    public PositionWrapper(PacketPlayOutPosition instance) {
        super(instance, PacketPlayOutPosition.class);
    }

    public double getX() {
        return getField(0);
    }

    public double getY() {
        return getField(1);
    }

    public double getZ() {
        return getField(2);
    }
}
