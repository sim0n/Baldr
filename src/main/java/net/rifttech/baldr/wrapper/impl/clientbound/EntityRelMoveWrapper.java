package net.rifttech.baldr.wrapper.impl.clientbound;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.rifttech.baldr.wrapper.ClassWrapper;

public class EntityRelMoveWrapper extends ClassWrapper<PacketPlayOutEntity> {
    public EntityRelMoveWrapper(PacketPlayOutEntity instance) {
        super(instance, PacketPlayOutEntity.class);
    }

    public int getId() {
        return getField(0);
    }

    public byte getX() {
        return getField(1);
    }

    public byte getY() {
        return getField(2);
    }

    public byte getZ() {
        return getField(3);
    }

    public byte getYaw() {
        return getField(4);
    }

    public byte getPitch() {
        return getField(5);
    }

}

