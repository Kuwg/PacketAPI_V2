package kuwg.packetapi.listener;

public enum EnumPacketListenerPriority {
    LOWEST(0),
    REALLY_LOW(1),
    LOW(2),
    NORMAL(3),
    HIGH(4),
    REALLY_HIGH(5),
    HIGHEST(6);
    private final int value;
    EnumPacketListenerPriority(int value){
        this.value=value;
    }
    public int value(){return value;}
    public static EnumPacketListenerPriority of(int i){
        return values()[i];
    }
}
