package kuwg.packetapi.ids;

public class ExamplePacketID implements PacketType<ExamplePacketID.ClientBound, ExamplePacketID.ServerBound> {


    @Override
    public ClientBound getSendPacketFrom(int value) {
        return null;
    }

    @Override
    public ServerBound getReceivePacketFrom(int value) {
        return null;
    }

    public static enum ClientBound{
        e(1);
        private final int value;
        ClientBound(final int value){
            this.value=value;
        }
        public int getValue() {
            return value;
        }
    }
    public static enum ServerBound{
        e(1);
        private final int value;
        ServerBound(final int value){
            this.value=value;
        }

        public int getValue() {
            return value;
        }
    }
}
