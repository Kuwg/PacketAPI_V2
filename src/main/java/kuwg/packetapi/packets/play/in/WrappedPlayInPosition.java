package kuwg.packetapi.packets.play.in;

import io.netty.buffer.ByteBuf;
import kuwg.packetapi.events.PacketReceiveEvent;
import org.bukkit.entity.Player;

public class WrappedPlayInPosition extends WrappedPlayInFlying{
    public WrappedPlayInPosition(ByteBuf buffer, Player sender) {
        super(buffer, sender, true, false);
    }

    public WrappedPlayInPosition(PacketReceiveEvent event) {
        super(event.getByteBuf(), event.getPlayer(), true, false);
    }
}
