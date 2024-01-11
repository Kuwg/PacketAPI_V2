package kuwg.packetapi.mojang;

public enum EnumDimension {
    NETHER(-1),
    OVERWORLD(0),
    END(1);

    private final int r;
    EnumDimension(int r) {
        this.r=r;
    }

    public int getValue() {
        return r;
    }

    public static EnumDimension from(int r){
        return r==0?OVERWORLD:(r==-1?NETHER:END);
    }
}
