package kuwg.packetapi.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.events.PacketSendEvent;
import kuwg.packetapi.exceptions.PacketException;
import kuwg.packetapi.exceptions.PacketProcessException;
import kuwg.packetapi.player.PacketPlayer;

import java.util.List;

// client bound
public class APIEncoder extends MessageToMessageEncoder<ByteBuf> {
    private final PacketPlayer player;

    public APIEncoder(PacketPlayer player) {
        this.player = player;
    }
    @Override
    public void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.isReadable()) {
            transform(byteBuf);
            list.add(byteBuf.retain());
        }
    }
    private void transform(ByteBuf buf) throws PacketException {
        final int preProcessIndex = buf.readerIndex();
        PacketSendEvent event = new PacketSendEvent(buf, player.getPlayer());
        PacketAPI.getInstance().getPacketListenerManager().call(event);
        buf.readerIndex(preProcessIndex);
        if(event.isCancelled())
            buf.clear();
    }
}
