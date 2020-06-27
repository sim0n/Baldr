package net.rifttech.baldr.wrapper.impl.clientbound;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.rifttech.baldr.wrapper.ClassWrapper;

public class EntityDestroyWrapper extends ClassWrapper<PacketPlayOutEntityDestroy> {
    public EntityDestroyWrapper(PacketPlayOutEntityDestroy instance) {
        super(instance, PacketPlayOutEntityDestroy.class);
    }

    public int[] getIds() {
        return getField(0);
    }
}
