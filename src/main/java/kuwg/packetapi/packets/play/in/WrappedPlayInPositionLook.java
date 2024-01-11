package kuwg.packetapi.packets.play.in;

import io.netty.buffer.ByteBuf;
import kuwg.packetapi.events.PacketReceiveEvent;
import org.bukkit.entity.Player;

public class WrappedPlayInPositionLook extends WrappedPlayInFlying{
    public WrappedPlayInPositionLook(ByteBuf buffer, Player sender) {
        super(buffer, sender, true, true);

    }

    public WrappedPlayInPositionLook(PacketReceiveEvent event) {
        super(event.getByteBuf(), event.getPlayer(), true, true);
    }
}
