package kuwg.packetapi.mojang;

public enum GameMode {
    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR;
    private static final GameMode[] VALUES = values();

    public int getId() {
        return ordinal();
    }

    public static GameMode getById(final int id) {
        return id<0||id>=VALUES.length?GameMode.SURVIVAL:VALUES[id];
    }
}

