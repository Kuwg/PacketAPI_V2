package kuwg.packetapi.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.player.PacketPlayer;
import kuwg.packetapi.util.AsyncUtil;
import kuwg.packetapi.util.PAPIVer;

public class ChannelInitializer extends ChannelInboundHandlerAdapter {
    private final PacketPlayer player;
    boolean injected;
    public ChannelInitializer(PacketPlayer player) {
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(!injected)
            AsyncUtil.executorAsync(()->{
                Channel channel = ctx.channel();
                if (PacketAPI.getVer().isNewerThan(new PAPIVer("1.12"))) {
                    try {
                        if (channel.pipeline().get("splitter") == null)
                            channel.close();
                        else
                            channel.pipeline()
                                    .addBefore("decoder", "papi_decoder", new APIDecoder(player))
                                    .addBefore("encoder", "papi_encoder", new APIEncoder(player));

                    } catch (Throwable t) {
                        try {
                            exceptionCaught(ctx, t);
                        } catch (Exception ignored) {}
                    } finally {
                        ChannelPipeline pipeline = ctx.pipeline();
                        if (pipeline.context(this) != null) {
                            pipeline.remove(this);
                        }
                    }
                    ctx.pipeline().fireChannelRegistered();
                } else {
                    channel.pipeline().addBefore("decoder", "papi_decoder", new APIDecoder(player));
                    channel.pipeline().addBefore("encoder", "papi_encoder", new APIEncoder(player));
                }
                this.injected=true;
            });

        super.channelRead(ctx, msg);
    }
}
