package kuwg.packetapi.test;

import kuwg.packetapi.events.PacketReceiveEvent;
import kuwg.packetapi.listener.PacketListener;

public class TestPacketListener implements PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) throws Exception {
        if(event.getPacketID()==0x01){
            event.getPacketPlayer().kick("lmfao L");
        }
    }
}
