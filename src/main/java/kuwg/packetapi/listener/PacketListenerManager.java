package kuwg.packetapi.listener;

import kuwg.packetapi.events.PacketEvent;
import kuwg.packetapi.events.PacketReceiveEvent;
import kuwg.packetapi.events.PacketSendEvent;
import kuwg.packetapi.packets.EnumPacketDirection;

import java.util.*;

public class PacketListenerManager {
    private final Map<EnumPacketListenerPriority, Set<PacketListener>> listeners = new HashMap<>();

    public void registerListener(PacketListener listener) {
        EnumPacketListenerPriority priority = getListenerPriority(listener);
        listeners.computeIfAbsent(priority, k -> new HashSet<>()).add(listener);
    }

    public void unregisterListener(PacketListener listener) {
        EnumPacketListenerPriority priority = getListenerPriority(listener);
        listeners.computeIfPresent(priority, (k, v) -> {
            v.remove(listener);
            return v.isEmpty() ? null : v;
        });
    }

    private EnumPacketListenerPriority getListenerPriority(PacketListener listener) {
        if (listener.getClass().isAnnotationPresent(PacketPriority.class)) {
            return listener.getClass().getAnnotation(PacketPriority.class).priority();
        } else {
            return EnumPacketListenerPriority.NORMAL;
        }
    }

    public void call(PacketEvent raw){
        if(raw.getEnumPacketDirection().equals(EnumPacketDirection.RECEIVE)){
            final PacketReceiveEvent event = (PacketReceiveEvent) raw;
            for(int i = 0; i <= 6; i++){
                final EnumPacketListenerPriority current = EnumPacketListenerPriority.of(i);
                for (PacketListener listener : listeners.getOrDefault(current, new HashSet<>())) {
                    try {
                        listener.onPacketReceive(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            final PacketSendEvent event = (PacketSendEvent) raw;
            for(int i = 0; i <= 6; i++){
                final EnumPacketListenerPriority current = EnumPacketListenerPriority.of(i);
                for (PacketListener listener : listeners.getOrDefault(current, new HashSet<>())) {
                    try {
                        listener.onPacketSend(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
