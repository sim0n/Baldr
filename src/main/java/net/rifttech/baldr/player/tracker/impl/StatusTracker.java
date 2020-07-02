package net.rifttech.baldr.player.tracker.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.AttributeModifiable;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.player.tracker.PlayerTracker;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@Getter @Setter
public class StatusTracker extends PlayerTracker {
    // Literally taken out of nms EntityLiving.class
    private static final UUID SPRINTING_SPEED_BOOST = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");

    private double movementSpeed;
    private double jumpBoost;

    public StatusTracker(Player player, PlayerData playerData) {
        super(player, playerData);
    }

    /**
     * <p>Status effects are handled by the server, if the client is lagging and your effect is about to run out
     * then it won't until the the client has stopped lagging</p>
     */
    public void handle() {
        movementSpeed = getMovementSpeed(((CraftPlayer) player).getHandle());

        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            player.getActivePotionEffects().stream()
                    .filter(effect -> effect.getType() == PotionEffectType.JUMP)
                    .findFirst()
                    .ifPresent(effect -> jumpBoost = effect.getAmplifier() + 1);
        }
    }

    // This method is just remade from AttributeModifiable#g
    public double getMovementSpeed(EntityLiving entity) {
        AttributeModifiable attribute = (AttributeModifiable) entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);

        double baseMovementSpeed = attribute.b();

        AtomicReference<Double> value = new AtomicReference<>(baseMovementSpeed);

        IntStream.range(0, 3).forEach(i -> attribute.a(i)
                .forEach(modifier -> {
                    switch (i) {
                        case 0:
                            value.updateAndGet(v -> v + modifier.d());
                            break;

                        case 1:
                            value.updateAndGet(v -> v + modifier.d() * baseMovementSpeed);
                            break;

                        case 2:
                            // We want to handle sprinting by ourselves considering it's shit in bukkit
                            if (!modifier.a().equals(SPRINTING_SPEED_BOOST))
                                value.updateAndGet(v -> v + (v * modifier.d()));

                            break;
                    }
                }));

        return value.get();
    }
}
