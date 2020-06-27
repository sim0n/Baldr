package net.rifttech.baldr.util.update;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.rifttech.baldr.util.location.CustomLocation;

@Getter
@RequiredArgsConstructor
public class MovementUpdate {
    private final CustomLocation from, to;
}
