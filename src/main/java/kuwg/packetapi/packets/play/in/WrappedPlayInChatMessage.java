package kuwg.packetapi.packets.play.in;

import io.netty.buffer.ByteBuf;
import kuwg.packetapi.events.PacketReceiveEvent;
import kuwg.packetapi.packets.EnumPacketDirection;
import kuwg.packetapi.packets.PacketWrapper;
import org.bukkit.entity.Player;

public class WrappedPlayInChatMessage extends PacketWrapper<WrappedPlayInChatMessage> {
    public WrappedPlayInChatMessage(ByteBuf buffer, Player sender) {
        super(buffer, sender, EnumPacketDirection.RECEIVE);
    }
    public WrappedPlayInChatMessage(PacketReceiveEvent event){
        super(event.getByteBuf(), event.getPlayer(), EnumPacketDirection.SEND);
    }
    private String message;

    @Override
    public void write() {
        prepareForWrite(0x01);
        int maxMessageLength = version.isNewerOrEquals("1.11") ? 256 : 100;
        writeString(this.message, maxMessageLength);
    }

    @Override
    public void read() {
        int maxMessageLength = version.isNewerOrEquals("1.11") ? 256 : 100;
        this.message = readString(maxMessageLength);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCommand(){
        return message.startsWith("/");
    }
}
