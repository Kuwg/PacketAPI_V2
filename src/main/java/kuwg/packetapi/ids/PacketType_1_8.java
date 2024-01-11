package kuwg.packetapi.ids;

public class PacketType_1_8 implements PacketType<PacketType_1_8.ClientBound, PacketType_1_8.ServerBound> {

    @Override
    public ClientBound getSendPacketFrom(int value){
        return ClientBound.VALUES.length>=value?ClientBound.VALUES[value]:null;
    }

    @Override
    public ServerBound getReceivePacketFrom(int value){
        return ServerBound.VALUES.length>=value?ServerBound.VALUES[value]:null;
    }

    public static enum ClientBound implements EnumPacketType{
        KEEP_ALIVE(0x00),
        JOIN_GAME(0x01),
        CHAT_MESSAGE(0x02),
        TIME_UPDATE(0x03);
        private final int value;
        ClientBound(final int value){
            this.value=value;
        }
        private static final ClientBound[] VALUES = values();
        public int getValue() {
            return value;
        }
    }
    public static enum ServerBound implements EnumPacketType{
        e(1);
        private final int value;
        ServerBound(final int value){
            this.value=value;
        }
        private static final ServerBound[] VALUES = values();
        public int getValue() {
            return value;
        }
    }
}
