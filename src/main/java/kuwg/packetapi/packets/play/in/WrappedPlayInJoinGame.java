package kuwg.packetapi.packets.play.in;

import io.netty.buffer.ByteBuf;
import kuwg.packetapi.events.PacketReceiveEvent;
import kuwg.packetapi.mojang.EnumDifficulty;
import kuwg.packetapi.mojang.EnumDimension;
import kuwg.packetapi.mojang.GameMode;
import kuwg.packetapi.packets.EnumPacketDirection;
import kuwg.packetapi.packets.PacketWrapper;
import org.bukkit.entity.Player;

public class WrappedPlayInJoinGame extends PacketWrapper<WrappedPlayInJoinGame> {
    public WrappedPlayInJoinGame(ByteBuf buffer, Player sender) {
        super(buffer, sender, EnumPacketDirection.RECEIVE);
    }
    public WrappedPlayInJoinGame(PacketReceiveEvent event){
        super(event.getByteBuf(), event.getPlayer(), EnumPacketDirection.RECEIVE);
    }
    private int entityID;
    private GameMode gameMode;
    private EnumDimension dimension;
    private EnumDifficulty difficulty;
    private int max_players;

}
