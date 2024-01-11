package kuwg.packetapi.packets.play.in;

import io.netty.buffer.ByteBuf;
import kuwg.packetapi.events.PacketReceiveEvent;
import kuwg.packetapi.mojang.Vec3D;
import kuwg.packetapi.packets.EnumPacketDirection;
import kuwg.packetapi.packets.PacketWrapper;
import org.bukkit.entity.Player;

public class WrappedPlayInUseEntity extends PacketWrapper<WrappedPlayInUseEntity> {

    public WrappedPlayInUseEntity(ByteBuf buffer, Player sender, EnumPacketDirection direction) {
        super(buffer, sender, direction);
    }
    public WrappedPlayInUseEntity(PacketReceiveEvent event){
        super(event.getByteBuf(), event.getPlayer(), EnumPacketDirection.RECEIVE);
    }

    private int entityID;
    private EnumUseEntityAction enumUseEntityAction;
    private Vec3D targetPos;


    @Override
    public void read() {
        this.entityID=readVarInt();
        this.enumUseEntityAction=EnumUseEntityAction.VALUES[readVarInt()];
        if(enumUseEntityAction==EnumUseEntityAction.INTERACT_AT)
            this.targetPos=new Vec3D(readFloat(), readFloat(), readFloat());
        else
            this.targetPos=new Vec3D(0,0,0);
    }

    @Override
    public void write() {
        prepareForWrite(0x02);
        writeVarInt(entityID);
        writeVarInt(enumUseEntityAction.ordinal());
        writeDouble(targetPos.x);
        writeDouble(targetPos.y);
        writeDouble(targetPos.z);
    }

    public EnumUseEntityAction getEnumUseEntityAction() {
        return enumUseEntityAction;
    }

    public Vec3D getTargetPos() {
        return targetPos;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public void setEnumUseEntityAction(EnumUseEntityAction enumUseEntityAction) {
        this.enumUseEntityAction = enumUseEntityAction;
    }

    public void setTargetPos(Vec3D targetPos) {
        this.targetPos = targetPos;
    }

    public enum EnumUseEntityAction {
        INTERACT,
        ATTACK,
        INTERACT_AT;
        public static final EnumUseEntityAction[] VALUES = values();
    }
}
