package net.rifttech.baldr.check.impl.timer;

import net.minecraft.server.v1_8_R3.*;
import net.rifttech.baldr.check.type.PacketCheck;
import net.rifttech.baldr.player.PlayerData;
import org.bukkit.entity.Player;

import java.util.Deque;
import java.util.LinkedList;

/**
 * This is probably the most generic timer check ever, it has issues with lag which you'll have to solve yourself
 */
public class TimerA extends PacketCheck {
    private final Deque<Long> delays = new LinkedList<>();

    private Long lastMovePacket;

    public TimerA(PlayerData playerData) {
        super(playerData, "Timer A");
    }

    @Override
    public void handle(Player player, Packet<PacketListenerPlayIn> packet) {
        long now = System.currentTimeMillis();

        if (packet instanceof PacketPlayInFlying && playerData.getConnectionTracker().isReceivedKeepAlive()) {
            if (lastMovePacket != null) {
                long delay = now - lastMovePacket;

                delays.add(delay);

                if (delays.size() >= 40) {
                    double average = getAverage(delays);
                    double timerSpeed = 50D / average;

                    if (timerSpeed >= 1.01) {
                        if ((violations += 10) > 35) {
                            alert(player, String.format("AVG %.1f, TS %.2f", average, timerSpeed));
                        }
                    } else {
                        decreaseVl(6);
                    }

                    delays.clear();
                }
            }

            lastMovePacket = now;
        }
    }

    @Override
    public void handleOutbound(Player player, Packet<PacketListenerPlayOut> packet) {
        if (packet instanceof PacketPlayOutPosition) {
            delays.add(105L); // magic value, change it
        }
    }
}
