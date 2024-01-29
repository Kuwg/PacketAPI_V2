package kuwg.packetapi.listener.manager;

import kuwg.packetapi.events.PacketEvent;
import kuwg.packetapi.events.PacketReceiveEvent;
import kuwg.packetapi.events.PacketSendEvent;
import kuwg.packetapi.exceptions.PacketException;
import kuwg.packetapi.listener.PacketListener;
import kuwg.packetapi.listener.priority.PacketListenerPriority;
import kuwg.packetapi.listener.priority.EnumPacketListenerPriority;
import kuwg.packetapi.packets.EnumPacketDirection;
import kuwg.packetapi.packets.play.in.WrappedPlayInKeepAlive;

import java.util.*;

@SuppressWarnings("unused")
public class PacketListenerManager {
    private final Map<EnumPacketListenerPriority, Set<PacketListener>> listeners = new HashMap<>();

    public void registerListener(PacketListener listener) {
        listeners.computeIfAbsent(getListenerPriority(listener), ignored -> new HashSet<>()).add(listener);
    }

    public void unregisterListener(PacketListener listener) {
        listeners.computeIfPresent(getListenerPriority(listener), (k, v) -> {
            v.remove(listener);
            return v.isEmpty() ? null : v;
        });
    }

    private EnumPacketListenerPriority getListenerPriority(PacketListener listener) {
        return listener.getClass().isAnnotationPresent(PacketListenerPriority.class)?
                listener.getClass().getAnnotation(PacketListenerPriority.class).priority():
                EnumPacketListenerPriority.NORMAL;
    }

    public void call(PacketEvent raw) {
        try {
            if(raw.getEnumPacketDirection().equals(EnumPacketDirection.RECEIVE)) {
                final PacketReceiveEvent event = (PacketReceiveEvent) raw;
                if(event.getPacketID()==0x00)
                    event.getPacketPlayer().onKeepAlive(new WrappedPlayInKeepAlive(event));
                for (int i = 0; i <= 6; i += 2) {
                    for (final PacketListener listener :
                            listeners.getOrDefault(EnumPacketListenerPriority.of(i), Collections.emptySet())) {
                        listener.onPacket(raw);
                        listener.onPacketReceive(event);
                    }
                }
            }else {
                final PacketSendEvent event = (PacketSendEvent) raw;
                for (int i = 0; i <= 6; i += 2) {
                    for (final PacketListener listener :
                            listeners.getOrDefault(EnumPacketListenerPriority.of(i), Collections.emptySet())) {
                        listener.onPacket(raw);
                        listener.onPacketSend(event);
                    }
                }
            }
        } catch (PacketException e) {
            e.printStackTrace(System.out);
        }
    }
}
