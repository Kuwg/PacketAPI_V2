package kuwg.packetapi.packets.play.in;

import io.netty.buffer.ByteBuf;
import kuwg.packetapi.events.PacketReceiveEvent;
import org.bukkit.entity.Player;

public class WrappedPlayInLook extends WrappedPlayInFlying{
    public WrappedPlayInLook(ByteBuf buffer, Player sender) {
        super(buffer, sender, false, true);
    }

    public WrappedPlayInLook(PacketReceiveEvent event) {
        super(event.getByteBuf(), event.getPlayer(), false, true);
    }
}
