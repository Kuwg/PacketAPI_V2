package kuwg.packetapi.events;

import io.netty.buffer.ByteBuf;
import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.exceptions.PacketBufferException;
import kuwg.packetapi.exceptions.PacketException;
import kuwg.packetapi.exceptions.PacketProcessException;
import kuwg.packetapi.packets.EnumPacketDirection;
import kuwg.packetapi.player.PacketPlayer;
import kuwg.packetapi.util.ByteBufUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class PacketEvent implements Cancellable {
    private final Player player;
    private final long timestamp;
    protected int size;
    protected final ByteBuf byteBuf;
    protected int packetID;
    private final EnumPacketDirection direction;
    private final PacketPlayer packetPlayer;
    protected PacketEvent(ByteBuf buffer, Player player, EnumPacketDirection direction) throws PacketException {
        try {
            this.byteBuf = buffer;
            this.size = byteBuf.readableBytes();
            if (size == 0)
                throw new PacketBufferException("Size of packet is 0.");
            timestamp = System.currentTimeMillis();
            this.player = player;
            this.direction = direction;
            this.packetPlayer = PacketAPI.getInstance().getPacketPlayer(player);
            this.packetID = ByteBufUtil.readVarInt(byteBuf);
        }catch (Exception ex){
            throw new PacketBufferException(ex.getMessage());
        }
    }

    public final Player getPlayer() {
        return player;
    }


    public final long getTimestamp() {
        return timestamp;
    }

    public final int getPacketID() {
        return packetID;
    }

    public final EnumPacketDirection getEnumPacketDirection() {
        return direction;
    }
    private boolean cancel;

    @Override
    public final void setCancelled(boolean cancel) {
        this.cancel=cancel;
    }

    @Override
    public final boolean isCancelled() {
        return cancel;
    }
    public ByteBuf getByteBuf(){
        return byteBuf;
    }

    public final PacketPlayer getPacketPlayer() {
        return packetPlayer;
    }

}
