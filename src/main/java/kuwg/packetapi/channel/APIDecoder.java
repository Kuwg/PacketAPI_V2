package kuwg.packetapi.channel;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.events.PacketReceiveEvent;
import kuwg.packetapi.exceptions.PacketProcessException;
import kuwg.packetapi.player.PacketPlayer;

import java.util.List;

// server bound
public class APIDecoder extends MessageToMessageDecoder<ByteBuf> {
    private final PacketPlayer player;
    public APIDecoder(PacketPlayer player) {
        this.player = player;
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (buffer.isReadable()) {
            transform(buffer);
            out.add(buffer.retain());
        }
    }
    private void transform(ByteBuf buf) throws PacketProcessException {
        int preProcessIndex = buf.readerIndex();
        PacketReceiveEvent event = new PacketReceiveEvent(buf, player.getPlayer());
        PacketAPI.getInstance().getPacketListenerManager().call(event);
        buf.readerIndex(preProcessIndex);
        if(event.isCancelled())
            buf.clear();
    }

}
