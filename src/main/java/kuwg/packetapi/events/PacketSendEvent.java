package kuwg.packetapi.events;

import io.netty.buffer.ByteBuf;
import kuwg.packetapi.exceptions.PacketBufferException;
import kuwg.packetapi.exceptions.PacketException;
import kuwg.packetapi.exceptions.PacketProcessException;
import kuwg.packetapi.packets.EnumPacketDirection;
import kuwg.packetapi.util.ByteBufUtil;
import org.bukkit.entity.Player;

public class PacketSendEvent extends PacketEvent{
    public PacketSendEvent(ByteBuf buffer, Player player) throws PacketException {
        super(buffer, player, EnumPacketDirection.SEND);
    }
}
