package kuwg.packetapi.ids;

import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.util.PAPIVer;

public interface PacketType<X, Y>{

    X getSendPacketFrom(int value);
    Y getReceivePacketFrom(int value);
    final PacketType_1_8 v_1_8_8 = new PacketType_1_8();
    static kuwg.packetapi.ids.PacketType<?, ?> thisVersionPacketType(){
        final PAPIVer version = PacketAPI.getVer();
        switch (version.toString()){
            case "1.8":
                return v_1_8_8;
            default:
                return null;
        }
    }
}
