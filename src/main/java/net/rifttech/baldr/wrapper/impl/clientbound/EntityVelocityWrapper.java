package net.rifttech.baldr.wrapper.impl.clientbound;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.rifttech.baldr.wrapper.ClassWrapper;

public class EntityVelocityWrapper extends ClassWrapper<PacketPlayOutEntityVelocity> {
    public EntityVelocityWrapper(PacketPlayOutEntityVelocity instance) {
        super(instance, PacketPlayOutEntityVelocity.class);
    }

    public int getId() {
        return getField(0);
    }

    public int getX() {
        return getField(1);
    }

    public int getY() {
        return getField(2);
    }

    public int getZ() {
        return getField(3);
    }

}
