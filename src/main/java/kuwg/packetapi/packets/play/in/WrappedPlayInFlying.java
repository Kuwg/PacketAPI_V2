package kuwg.packetapi.packets.play.in;

import io.netty.buffer.ByteBuf;
import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.events.PacketReceiveEvent;
import kuwg.packetapi.mojang.PlayerTeleportFlag;
import kuwg.packetapi.packets.EnumPacketDirection;
import kuwg.packetapi.packets.PacketWrapper;
import kuwg.packetapi.util.ByteBufUtil;
import kuwg.packetapi.util.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static kuwg.packetapi.util.ChannelGetter.getServerVersion;

@SuppressWarnings("unused")
public class WrappedPlayInFlying extends PacketWrapper<WrappedPlayInFlying> {
    public WrappedPlayInFlying(ByteBuf buffer, Player sender) {
        super(buffer, sender, EnumPacketDirection.RECEIVE);
    }

    public WrappedPlayInFlying(PacketReceiveEvent event) {
        super(event.getByteBuf(), event.getPlayer(), EnumPacketDirection.RECEIVE);
    }

    protected WrappedPlayInFlying(ByteBuf buf, Player sender, boolean pos, boolean look) {
        super(buf, sender, EnumPacketDirection.RECEIVE);
        this.look=look;
        this.pos=pos;
    }
    protected boolean onGround, pos, look;
    protected double x, feetY, z;
    protected float yaw, pitch;
    private static final boolean is1_7=PacketAPI.getVer().equals("1.7");

    @Override
    public void read() {
        if(this.pos){
            this.x=readDouble();
            this.feetY=readDouble();
            if(is1_7) {
                double ignored = readDouble();
            }
            this.z=readDouble();
        }
        if(this.look){
            this.yaw=readFloat();
            this.pitch=readFloat();
        }
        this.onGround=readBoolean();
    }


    @Override
    public void write() {

        this.prepareForWrite((this.pos && this.look) ? 0x06 : (this.look ? 0x05 : (this.pos ? 0x04 : 0x03)));

        if(this.pos) {
            writeDouble(this.x);
            if(is1_7)
                writeDouble(feetY + 1.62);
            writeDouble(this.feetY);
            writeDouble(this.z);
        }
        if(this.look){
            writeFloat(this.yaw);
            writeFloat(this.pitch);
        }
        writeBoolean(onGround);
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean hasPos() {
        return pos;
    }

    public void setHasPos(boolean pos) {
        this.pos = pos;
    }

    public boolean hasLook() {
        return look;
    }

    public void setHasLook(boolean look) {
        this.look = look;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getFeetY() {
        return feetY;
    }

    public void setFeetY(double feetY) {
        this.feetY = feetY;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public Location asLoc(){
        return new Location(getPlayer().getWorld(), x, feetY, z, yaw, pitch);
    }

    public void resetAsLoc(final Location location){
        this.x=location.getX();
        this.feetY=location.getY();
        this.z=location.getZ();
        this.yaw=location.getYaw();
        this.pitch=location.getPitch();
    }

    public void setback(){
        getPlayer().teleport(getPlayer().getLocation());
    }


}
