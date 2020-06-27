package net.rifttech.baldr.wrapper.impl.clientbound;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.rifttech.baldr.wrapper.ClassWrapper;

public class EntityTeleportWrapper extends ClassWrapper<PacketPlayOutEntityTeleport> {
    public EntityTeleportWrapper(PacketPlayOutEntityTeleport instance) {
        super(instance, PacketPlayOutEntityTeleport.class);
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

    public byte getYaw() {
        return getField(4);
    }

    public byte getPitch() {
        return getField(5);
    }

}
