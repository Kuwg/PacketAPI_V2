package kuwg.packetapi.listener;

import kuwg.packetapi.events.PacketEvent;
import kuwg.packetapi.events.PacketReceiveEvent;
import kuwg.packetapi.events.PacketSendEvent;
import kuwg.packetapi.exceptions.PacketException;

public interface PacketListener {
    default void onPacketReceive(PacketReceiveEvent event)throws PacketException {}
    default void onPacketSend(PacketSendEvent event)throws PacketException{}
    default void onPacket(PacketEvent event)throws PacketException{}
}
