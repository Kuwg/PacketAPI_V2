package kuwg.packetapi.events;

import io.netty.buffer.ByteBuf;
import kuwg.packetapi.exceptions.PacketProcessException;
import kuwg.packetapi.packets.EnumPacketDirection;
import kuwg.packetapi.util.ByteBufUtil;
import org.bukkit.entity.Player;

public class PacketSendEvent extends PacketEvent{
    public PacketSendEvent(ByteBuf buffer, Player player) throws PacketProcessException{
        super(buffer, player, EnumPacketDirection.SEND);

    }

}
