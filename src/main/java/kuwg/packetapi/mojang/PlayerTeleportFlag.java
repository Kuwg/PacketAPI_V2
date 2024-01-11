package kuwg.packetapi.mojang;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public enum PlayerTeleportFlag {
    X_POSITION(0),
    Y_POSITION(1),
    Z_POSITION(2),
    Y_ROTATION(3),
    X_ROTATION(4);

    private final int flagValue;

    PlayerTeleportFlag(int value) {
        this.flagValue = value;
    }

    private int getBitMask() {
        return 1 << this.flagValue;
    }

    private boolean isFlagSet(int flags) {
        return (flags & this.getBitMask()) == this.getBitMask();
    }

    public static Set<PlayerTeleportFlag> fromBitMask(int flags) {
        EnumSet<PlayerTeleportFlag> result = EnumSet.noneOf(PlayerTeleportFlag.class);
        for (PlayerTeleportFlag flag : values()) {
            if (flag.isFlagSet(flags)) {
                result.add(flag);
            }
        }
        return result;
    }

    public static int toBitMask(Set<PlayerTeleportFlag> flags) {
        int result = 0;
        for (PlayerTeleportFlag flag : flags) {
            result |= flag.getBitMask();
        }
        return result;
    }
    public static final Set<PlayerTeleportFlag> allTeleportFlags =
            new HashSet<>(Arrays.asList(X_ROTATION, Y_POSITION, Z_POSITION));
    public static final int allTeleportFlagsToBitMask =
            PlayerTeleportFlag.toBitMask(PlayerTeleportFlag.allTeleportFlags);
}