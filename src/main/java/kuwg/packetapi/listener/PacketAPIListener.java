package kuwg.packetapi.listener;

import kuwg.packetapi.events.PacketReceiveEvent;
import kuwg.packetapi.listener.priority.EnumPacketListenerPriority;
import kuwg.packetapi.listener.priority.PacketListenerPriority;
import kuwg.packetapi.packets.play.in.WrappedPlayInKeepAlive;

@PacketListenerPriority(priority = EnumPacketListenerPriority.HIGHEST)
public class PacketAPIListener implements PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(event.getPacketID()==0x00)
            event.getPacketPlayer().onKeepAlive(new WrappedPlayInKeepAlive(event));
    }
}
