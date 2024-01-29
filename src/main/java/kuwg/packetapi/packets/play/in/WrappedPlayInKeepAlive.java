package kuwg.packetapi.packets.play.in;

import io.netty.buffer.ByteBuf;
import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.events.PacketReceiveEvent;
import kuwg.packetapi.packets.EnumPacketDirection;
import kuwg.packetapi.packets.PacketWrapper;
import kuwg.packetapi.util.ByteBufUtil;
import org.bukkit.entity.Player;

public class WrappedPlayInKeepAlive extends PacketWrapper<WrappedPlayInKeepAlive> {
    public WrappedPlayInKeepAlive(ByteBuf buffer, Player sender) {
        super(buffer, sender, EnumPacketDirection.SEND);
        read();
    }
    public WrappedPlayInKeepAlive(PacketReceiveEvent event){
        super(event.getByteBuf(), event.getPlayer(), EnumPacketDirection.SEND);
        read();
    }

    private long id;
    @Override
    public void read() {
        if (PacketAPI.getVer().isNewerOrEquals("1.12")) {
            this.id = buffer.readLong();
        } else if (PacketAPI.getVer().isNewerOrEquals("1.8")) {
            this.id = ByteBufUtil.readVarInt(buffer);
        } else {
            this.id = buffer.readInt();
        }
    }

    @Override
    public void write() {
        prepareForWrite(0x00);
        if (PacketAPI.getVer().isNewerOrEquals("1.12")) {
            buffer.writeLong(id);
        } else if (PacketAPI.getVer().isNewerOrEquals("1.8")) {
            ByteBufUtil.writeVarInt(buffer, (int)id);
        } else {
            buffer.writeInt((int)id);
        }
    }
    public long getID(){
        return id;
    }
    public void setID(long id){
        this.id=id;
    }
}
