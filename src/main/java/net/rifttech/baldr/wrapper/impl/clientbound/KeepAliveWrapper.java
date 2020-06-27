package net.rifttech.baldr.wrapper.impl.clientbound;

import net.minecraft.server.v1_8_R3.PacketPlayOutKeepAlive;
import net.rifttech.baldr.wrapper.ClassWrapper;

public class KeepAliveWrapper extends ClassWrapper<PacketPlayOutKeepAlive> {
    public KeepAliveWrapper(PacketPlayOutKeepAlive instance) {
        super(instance, PacketPlayOutKeepAlive.class);
    }

    public int getId() {
        return getField(0);
    }
}
