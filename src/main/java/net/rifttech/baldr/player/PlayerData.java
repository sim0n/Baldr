package net.rifttech.baldr.player;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R3.PacketListenerPlayOut;
import net.rifttech.baldr.check.CheckData;
import net.rifttech.baldr.player.tracker.PlayerTracker;
import net.rifttech.baldr.player.tracker.impl.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public class PlayerData {
    private static final Map<Field, Constructor<?>> CONSTRUCTORS = new HashMap<>();

    static {
        Arrays.stream(PlayerData.class.getDeclaredFields())
                .filter(field -> PlayerTracker.class.isAssignableFrom(field.getType()))
                .forEach(field -> {
                    Class<? extends PlayerTracker> clazz = (Class<? extends PlayerTracker>) field.getType();

                    try {
                        CONSTRUCTORS.put(field, clazz.getConstructor(Player.class, PlayerData.class));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
    }

    private final int id;

    private StatusTracker statusTracker;

    private MovementTracker movementTracker;
    private ConnectionTracker connectionTracker;
    private ActionTracker actionTracker;

    private PacketTracker packetTracker; // needs to be updated last

    private final CheckData checkData = new CheckData();

    public PlayerData(Player player) {
        id = player.getEntityId();

        CONSTRUCTORS.forEach((field, constructor) -> {
            try {
                field.set(this, constructor.newInstance(player, this));
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        // this is required to be registered last
        checkData.register(this);
    }

    public void handleInboundPacket(Packet<PacketListenerPlayIn> packet) {
        packetTracker.handleInbound(packet);
    }

    public void handleOutboundPacket(Packet<PacketListenerPlayOut> packet) {
        packetTracker.handleOutbound(packet);
    }

}
