package net.rifttech.baldr.wrapper.impl.serverbound;

import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.rifttech.baldr.wrapper.ClassWrapper;

public class UseEntityWrapper extends ClassWrapper<PacketPlayInUseEntity> {
    public UseEntityWrapper(PacketPlayInUseEntity instance) {
        super(instance, PacketPlayInUseEntity.class);
    }

    public int getId() {
        return getField(0);
    }
}
