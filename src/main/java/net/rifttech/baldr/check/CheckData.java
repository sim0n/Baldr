package net.rifttech.baldr.check;

import lombok.Getter;
import net.rifttech.baldr.check.type.PacketCheck;
import net.rifttech.baldr.check.type.movement.PositionCheck;
import net.rifttech.baldr.check.type.movement.RotationCheck;
import net.rifttech.baldr.manager.CheckManager;
import net.rifttech.baldr.player.PlayerData;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class CheckData {
    private final List<Check<?>> checks = new ArrayList<>();
    private final List<PacketCheck> packetChecks = new ArrayList<>();
    private final List<PositionCheck> positionChecks = new ArrayList<>();
    private final List<RotationCheck> rotationChecks = new ArrayList<>();

    public void register(PlayerData playerData) {
        // We could make multiple constructor collections but I rather just have to loop through one
        CheckManager.CONSTRUCTORS.forEach(constructor -> {
            try {
                Check<?> check = (Check<?>) constructor.newInstance(playerData);

                if (check instanceof PacketCheck) {
                    packetChecks.add((PacketCheck) check);
                } else if (check instanceof PositionCheck) {
                    positionChecks.add((PositionCheck) check);
                } else if (check instanceof RotationCheck) {
                    rotationChecks.add((RotationCheck) check);
                }

                checks.add(check);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Returns an optional with the value of the check class provided
     * @param clazz The class of the check
     * @return An optional containing the check, it will be empty if it couldn't find it
     */
    public <T extends Check<?>> Optional<T> getCheck(Class<T> clazz) {
        return Optional.ofNullable(clazz.cast(checks.stream()
                .filter(check -> check.getClass() == clazz)
                .findFirst()
                .orElse(null)));
    }
}
