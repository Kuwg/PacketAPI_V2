package kuwg.packetapi.listener;

import kuwg.packetapi.events.PacketReceiveEvent;
import kuwg.packetapi.events.PacketSendEvent;

public interface PacketListener {
    default void onPacketReceive(PacketReceiveEvent event)throws Exception{}
    default void onPacketSend(PacketSendEvent event)throws Exception{}
}
