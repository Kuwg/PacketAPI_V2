package kuwg.packetapi.channel;

import io.netty.channel.Channel;
import kuwg.packetapi.player.PacketPlayer;


public class ChannelInjector {

    public static void inject(final PacketPlayer player){
        final Channel channel = player.getChannel();
        try {
            channel.pipeline().addFirst("papi_handler", new ChannelInitializer(player));
        }catch (Exception ignored){}
    }
    public static void remove(final PacketPlayer player){
        assert player!=null;
        final Channel channel = player.getChannel();
        try {
            channel.pipeline().remove("papi_handler");
            channel.pipeline().remove("papi_encoder");
            channel.pipeline().remove("papi_decoder");
        }catch (Exception ignored){}
    }
}
