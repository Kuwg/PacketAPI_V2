package kuwg.packetapi.player;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.mojang.BoundingBox;
import kuwg.packetapi.mojang.GameProfile;
import kuwg.packetapi.mojang.PlayerTeleportFlag;
import kuwg.packetapi.util.ByteBufUtil;
import kuwg.packetapi.util.ChannelGetter;
import kuwg.packetapi.util.ReflectionUtil;
import kuwg.packetapi.util.StringUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

//@SuppressWarnings({"unused", "JavaReflectionInvocation", "unchecked", "rawtypes"})
public class PacketPlayer {
    private final Player player;
    private final Channel channel;
    private final Object craftPlayer;
    private final Object entityPlayer;
    private final Object playerConnection;
    private final Object networkManager;
    private final long joinTime;
    private static final String ver = ChannelGetter.getServerVersion();
    private String getServerVersion(){
        return ver;
    }
    public static final Set<Object> teleportFlags;

    static {
        Set<Object> teleportFlags1 = new HashSet<>();
        try {
            Class<?> enumTeleportFlagsClass=null;

            for(Class<?> clazz : Class.forName("net.minecraft.server." + ver + ".PacketPlayOutPosition").getDeclaredClasses())
                if(clazz.getSimpleName().contains("Flags"))
                    enumTeleportFlagsClass=clazz;
            if(enumTeleportFlagsClass==null)
                throw new RuntimeException("Enum teleport flags is null!");
            Method valuesMet = enumTeleportFlagsClass.getDeclaredMethod("values");
            valuesMet.setAccessible(true);
            teleportFlags1.addAll(Arrays.asList((Object[]) valuesMet.invoke(null)));
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }

        teleportFlags = teleportFlags1;
    }

    public PacketPlayer(Player player) {
        this.joinTime = System.currentTimeMillis();
        this.player = player;
        this.craftPlayer = Objects.requireNonNull(ReflectionUtil.classForName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer")).cast(player);
        this.entityPlayer = ReflectionUtil.getInvokeResult(ReflectionUtil.getMethod(ReflectionUtil.classForName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer"), "getHandle"), player);
        assert entityPlayer != null : "Null Entity Player";
        Class<?> entityPlayerClass = entityPlayer.getClass();
        Field playerConnectionField = ReflectionUtil.getDeclaredField(entityPlayerClass, "playerConnection");
        if (playerConnectionField == null) {
            for (final Field field : ReflectionUtil.getFields(entityPlayerClass)) {
                assert field != null;
                if (field.getType().getSimpleName().equalsIgnoreCase("PlayerConnection")) {
                    playerConnectionField = field;
                }
            }
        }
        assert playerConnectionField != null : "Null player connection.";
        playerConnectionField.setAccessible(true);
        this.playerConnection = ReflectionUtil.getFieldInvocationResult(playerConnectionField, entityPlayer);
        if (playerConnection == null) {
            player.kickPlayer("Kicked for null connection.");
            throw new NullPointerException("PlayerConnection is null");
        }
        Class<?> playerConnectionClass = playerConnection.getClass();
        Field networkManagerField = ReflectionUtil.getDeclaredField(playerConnectionClass, "networkManager");
        if (networkManagerField == null) for (Field field : ReflectionUtil.getFields(playerConnectionClass)) {
            assert field != null;
            if (field.getType().getSimpleName().contains("NetworkManager")) networkManagerField = field;
        }
        assert networkManagerField != null : "Network Manager Null";
        networkManagerField.setAccessible(true);
        this.networkManager = ReflectionUtil.getFieldInvocationResult(networkManagerField, playerConnection);
        if (networkManager == null) {
            player.kickPlayer("Kicked for null network manager.");
            throw new NullPointerException("NetworkManager is null");
        }
        this.channel = ChannelGetter.getFastChannel(networkManager);
    }

    public Player getPlayer() {
        return player;
    }
    public Object getCraftPlayer() {
        return craftPlayer;
    }

    public Object getEntityPlayer() {
        return entityPlayer;
    }

    public Object getPlayerConnection() {
        return playerConnection;
    }

    /**
     * Sends a NMS packet to a given player
     *
     * @param packetName The packet name that will get sent
     * @param params the parameters to the class for name packetName.
     */
    public boolean sendPacket(String packetName, Object... params){
        try{
            Object packet = ReflectionUtil.startClassConst(Class.forName(packetName), params);
            Class<?> packetClass = Class.forName("net.minecraft.server." + getServerVersion() + ".Packet");
            Method sendPacketMethod = playerConnection.getClass().getMethod("sendPacket", packetClass);
            sendPacketMethod.invoke(playerConnection, packet);
            return true;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                 IllegalAccessException e) {
            return false;
        }
    }
    @SuppressWarnings("ConstantConditions")
    public BoundingBox getBoundingBox(){
        Object AxisAlignedBB = ReflectionUtil.getInvokeResult(ReflectionUtil.getMethod(entityPlayer.getClass(), "getBoundingBox"), entityPlayer);
        double a= ReflectionUtil.getTField(AxisAlignedBB, "a");
        double b= ReflectionUtil.getTField(AxisAlignedBB, "b");
        double c= ReflectionUtil.getTField(AxisAlignedBB, "c");
        double d= ReflectionUtil.getTField(AxisAlignedBB, "d");
        double e= ReflectionUtil.getTField(AxisAlignedBB, "e");
        double f= ReflectionUtil.getTField(AxisAlignedBB, "f");
        return new BoundingBox(a,b,c,d,e,f);
    }
    public void setBoundingBox(BoundingBox bb){
        Object AxisAlignedBB = ReflectionUtil.getInvokeResult(ReflectionUtil.getMethod(entityPlayer.getClass(), "getBoundingBox"), entityPlayer);
        double a=bb.minX,b=bb.minY,c=bb.minZ,d=bb.maxX,e=bb.maxY,f=bb.maxZ;
        assert AxisAlignedBB != null : "AxisAlignedBB for player " + player.getName() + " is null!";
        ReflectionUtil.setField(AxisAlignedBB, "a", a);
        ReflectionUtil.setField(AxisAlignedBB, "b", b);
        ReflectionUtil.setField(AxisAlignedBB, "c", c);
        ReflectionUtil.setField(AxisAlignedBB, "d", d);
        ReflectionUtil.setField(AxisAlignedBB, "e", e);
        ReflectionUtil.setField(AxisAlignedBB, "f", f);
    }
    /**
     * Sends a NMS packet to a given player
     *
     * @param packet The packet to be sent
     */
    public void sendPacket(Object packet){
        Class<?> Packet = ReflectionUtil.classForName("net.minecraft.server." + getServerVersion() + ".Packet");
        ReflectionUtil.getInvokeResult(ReflectionUtil.getMethod(getPlayerConnection().getClass(), "sendPacket", Packet), getPlayerConnection(), packet);
    }
    /*
    public void sendPacket(PacketWrapper packet){
        if(packet.getDirection().equals(PacketDirection.SEND)){
            sendPacket(packet.getRawPacket());
        }else throw new UnsupportedOperationException("You cannot send a receiving packet!");
    }
    fix!
     */

    public void setSkin(Player onlinePlayer) {
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer");
            Class<?> gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
            Class<?> propertyMapClass = Class.forName("com.mojang.authlib.properties.PropertyMap");
            Method getHandleMethod = craftPlayerClass.getMethod("getHandle");
            Object playerHandle = entityPlayer;
            Object skinOwnerHandle = getHandleMethod.invoke(craftPlayerClass.cast(onlinePlayer));
            Object playerProfile = gameProfileClass.cast(playerHandle.getClass().getMethod("getProfile").invoke(playerHandle));
            Object skinOwnerProfile = gameProfileClass.cast(skinOwnerHandle.getClass().getMethod("getProfile").invoke(skinOwnerHandle));
            Object playerProperties = propertyMapClass.cast(playerProfile.getClass().getMethod("getProperties").invoke(playerProfile));
            Object skinOwnerProperties = propertyMapClass.cast(skinOwnerProfile.getClass().getMethod("getProperties").invoke(skinOwnerProfile));
            playerProperties.getClass().getMethod("clear").invoke(playerProperties);
            playerProperties.getClass().getMethod("putAll", String.class, Iterable.class).invoke(playerProperties, "textures", skinOwnerProperties.getClass().getMethod("get", Object.class).invoke(skinOwnerProperties, "textures"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Player on : Bukkit.getOnlinePlayers()) {
            on.hidePlayer(player);
            on.showPlayer(player);
        }
    }
    public void crash(){
        try {
            Class<?> Vec3D = Class.forName("net.minecraft.server." + getServerVersion() + ".Vec3D");
            sendPacket(
                    Class.forName("net.minecraft.server." + getServerVersion() + ".PacketPlayOutExplosion")
                            .getConstructor(double.class, double.class, double.class, float.class, List.class, Vec3D)
                            .newInstance(
                                    d(),
                                    d(),
                                    d(),
                                    f(),
                                    Collections.emptyList(),
                                    Vec3D.getConstructor(double.class, double.class, double.class).newInstance(d(), d(), d())
                            )
            );

        }catch (Exception ex){
            ex.printStackTrace();
        }


    }
    private static Double d() {
        return Double.MAX_VALUE-432.432;
    }

    private static Float f() {
        return Float.MAX_VALUE-32;
    }

    public Channel getChannel() {
        return channel;
    }

    public Object getNetworkManager() {
        return networkManager;
    }

    @Deprecated
    public void sendActionBar(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        String v = Bukkit.getServer().getClass().getPackage().getName();
        try {
            if (v.substring(v.lastIndexOf(".") + 1).equals("v1_12_R1") || v.substring(v.lastIndexOf(".") + 1).startsWith("v1_13")) {
                player.spigot().getClass().getDeclaredMethod("sendMessage", ChatMessageType.class, TextComponent.class).invoke(player.spigot(), ChatMessageType.ACTION_BAR, new TextComponent(message));
            } else {
                String className = "net.minecraft.server." + v.substring(v.lastIndexOf(".") + 1) + ".PacketPlayOutChat";
                String className1 = "org.bukkit.craftbukkit." + v.substring(v.lastIndexOf(".") + 1) + ".entity.CraftPlayer";
                String className2 = "net.minecraft.server." + v.substring(v.lastIndexOf(".") + 1) + ".Packet";
                Class<?> c1 = Class.forName(className1);
                Object p = c1.cast(player);
                String className3 = "net.minecraft.server." + v.substring(v.lastIndexOf(".") + 1) + ".IChatBaseComponent";
                if (!(v.substring(v.lastIndexOf(".") + 1).equalsIgnoreCase("v1_8_R1") || (v.substring(v.lastIndexOf(".") + 1).contains("v1_7_")))) {
                    Object packetPlayOutChat;
                    Class<?> c4 = Class.forName(className);
                    Class<?> c5 = Class.forName(className2);
                    Class<?> c2 = Class.forName("net.minecraft.server." + v.substring(v.lastIndexOf(".") + 1) + ".ChatComponentText");
                    Class<?> c3 = Class.forName(className3);
                    Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                    packetPlayOutChat = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
                    Method getHandle = c1.getDeclaredMethod("getHandle");
                    Object handle = getHandle.invoke(p);
                    Field fieldConnection = handle.getClass().getDeclaredField("playerConnection");
                    Object playerConnection = fieldConnection.get(handle);
                    Method sendPacket = playerConnection.getClass().getDeclaredMethod("sendPacket", c5);
                    sendPacket.invoke(playerConnection, packetPlayOutChat);
                } else {
                    Object packetPlayOutChat;
                    Class<?> c4 = Class.forName(className);
                    Class<?> c5 = Class.forName(className2);
                    Class<?> c2 = Class.forName("net.minecraft.server." + v.substring(v.lastIndexOf(".") + 1) + ".ChatSerializer");
                    Class<?> c3 = Class.forName(className3);
                    Method m3 = c2.getDeclaredMethod("a", String.class);
                    Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
                    packetPlayOutChat = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(cbc, (byte) 2);
                    Method getHandle = c1.getDeclaredMethod("getHandle");
                    Object handle = getHandle.invoke(p);
                    Field fieldConnection = handle.getClass().getDeclaredField("playerConnection");
                    Object playerConnection = fieldConnection.get(handle);
                    Method sendPacket = playerConnection.getClass().getDeclaredMethod("sendPacket", c5);
                    sendPacket.invoke(playerConnection, packetPlayOutChat);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    FIX

    public void sendPosition(double x, double y, double z, float yaw, float pitch){
        final PacketWrapper packet = PacketHelper.getOutPosition(player,x,y,z,yaw,pitch);
        if(packet!=null) {
            this.sendPacket(packet);
        }
    }
    */

    public void sendAs(String message){
        try{
            playerConnection.getClass().getMethod("chat", String.class, Boolean.class)
                    .invoke(playerConnection, ChatColor.translateAlternateColorCodes('&', message), false);
        }catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public long getLivedMs() {
        return System.currentTimeMillis()-joinTime;
    }
    public int getLivedTicks(){
        return NumberConversions.round(getLivedMs()/50d);
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void hidePlayer() {
        try {
            Class<?> packetClass = Class.forName("net.minecraft.server." + getServerVersion() +
                    ".PacketPlayOutPlayerInfo");
            Class<?> enumPlayerInfoActionClass = Class.forName("net.minecraft.server." + getServerVersion() +
                    ".PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
            Constructor<?> packetConstructor = packetClass.getConstructor(enumPlayerInfoActionClass,
                    entityPlayer.getClass());
            Enum<?> enumPlayerInfoAction = Enum.valueOf((Class<? extends Enum>) enumPlayerInfoActionClass, "REMOVE_PLAYER");
            sendPacket(packetConstructor.newInstance(enumPlayerInfoAction, entityPlayer));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPlayer() {
        try {
            Class<?> packetClass = Class.forName("net.minecraft.server." + getServerVersion() +
                    ".PacketPlayOutPlayerInfo");
            Class<?> enumPlayerInfoActionClass = Class.forName("net.minecraft.server." + getServerVersion() +
                    ".PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
            Constructor<?> packetConstructor = packetClass.getConstructor(enumPlayerInfoActionClass,
                    entityPlayer.getClass());
            Enum<?> enumPlayerInfoAction = Enum.valueOf((Class<Enum>) enumPlayerInfoActionClass, "ADD_PLAYER");
            sendPacket(packetConstructor.newInstance(enumPlayerInfoAction, entityPlayer));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public GameProfile getProfile() {
        final Object nmsProfile = ReflectionUtil.getInvokeResult(
                ReflectionUtil.getMethod(craftPlayer.getClass(), "getProfile"), craftPlayer
        );
        return new GameProfile((UUID) ReflectionUtil.getTField(
                nmsProfile, "id"),
                (String) ReflectionUtil.getTField(nmsProfile, "name")
        );
    }
    public void setProfile(final GameProfile profile){
        try {
            Object newGameProfile = Class.forName("com.mojang.authlib.GameProfile")
                    .getConstructor(UUID.class, String.class).newInstance(profile.getId(), profile.getName());
            Field gpf = null;
            for(Field f : newGameProfile.getClass().getDeclaredFields()) if(f.getType()
                    .getSimpleName().contains("Profile"))gpf=f;
            assert gpf!=null:"Game Profile Field doesn't exist!";
            gpf.set(entityPlayer, newGameProfile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Deprecated
    public void attack(Entity victim){
        //FIXME: player.attack(victim);
    }

    public void sendMessage(String message, kuwg.packetapi.mojang.ChatMessageType type) {
        try {
            /*
            OLD CODE:
            playerConnection.getClass().getMethod("sendPacket",
                    Class.forName("net.minecraft.server."+ver+".Packet")).invoke(playerConnection, Class.forName("net.minecraft.server."+ver+".PacketPlayOutChat")
                    .getConstructor(Class.forName("net.minecraft.server."+ver+".IChatBaseComponent"), byte.class)
                    .newInstance(Class.forName("net.minecraft.server."+ver+".ChatComponentText")
                            .getConstructor(String.class).newInstance(message), (byte) 1));

             */
            message="{\"text\": \"" + message + "\"}";
            ByteBuf byteBuf = Unpooled.buffer(message.getBytes().length);
            ByteBufUtil.writeVarInt(byteBuf, 0x02);
            ByteBufUtil.writeString(byteBuf, message);
            byteBuf.writeByte(type==kuwg.packetapi.mojang.ChatMessageType.CHAT?0x0:(type==kuwg.packetapi.mojang.ChatMessageType.ACTION_BAR?0x2:0x1));
            channel.writeAndFlush(byteBuf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the slot which the player has selected
     * @param slot 0 to 8
     */
    public void setHoldingSlot(final int slot){
        if(slot<0||slot>8)throw new IllegalArgumentException("Invalid slot(" + slot + "), must be between 0 and 8.");

        ByteBuf byteBuf = Unpooled.buffer(3);
        ByteBufUtil.writeVarInt(byteBuf, 0x09);
        byteBuf.writeByte(slot);
        channel.writeAndFlush(byteBuf);
    }

    /**
     * <h1>getCollidingBlocks</h1>
     * <p>Remember that this isn't really fast, and can lag a little bit (usually takes between 0.01 ms or less actually)</p>
     * @return the colliding blocks using some simple math
     */
    public @NotNull Block[] getCollidingBlocks() {
        Location z=player.getLocation();
        World world=z.getWorld();
        if(world==null)return null;
        int a=(int)Math.floor(z.getX()-0.3);
        int b=(int)Math.floor(z.getY()-0.3);
        int c=(int)Math.floor(z.getZ()-0.3);
        int d=(int)Math.ceil(z.getX()+0.3);
        int e=(int)Math.ceil(z.getY()+1.8);
        int f=(int)Math.ceil(z.getZ()+0.3);
        Block[] g = new Block[(d - a + 1) * (e - b + 1) * (f - c + 1)];
        int index = 0;
        for(int h=a;h<=d;h++)for(int i=b;i<=e;i++)for(int j=c;j<=f;j++)g[index++]=world.getBlockAt(h,i,j);
        return g;
    }
    public int getAmplifier(PotionEffectType effectType) {
        for(PotionEffect effect:player.getActivePotionEffects())if(effect.getType()==effectType)return effect.getAmplifier();
        return 0;
    }
    public  void randomRotation() {
        Bukkit.getScheduler().runTask(PacketAPI.getInstance(),()->player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), (float) (ThreadLocalRandom.current().nextDouble(360.0) - 180.0f), (float) (ThreadLocalRandom.current().nextDouble(180.0) - 90.0f)), PlayerTeleportEvent.TeleportCause.UNKNOWN));
    }
    public void teleport(Location location){
        internalTeleport(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    private void internalTeleport(double d0, double d1, double d2, float f, float f1) {
        if (Float.isNaN(f))
            f = 0.0F;
        if (Float.isNaN(f1))
            f1 = 0.0F;
        try {
            Class<?> packetClass = Class.forName("net.minecraft.server." + getServerVersion() + ".PacketPlayOutPosition");
            Constructor<?> packetConstructor = packetClass.getConstructor(double.class, double.class, double.class, float.class, float.class, Set.class);
            this.sendPacket(packetConstructor.newInstance(d0, d1, d2, f, f1, teleportFlags));
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ignored) {}
    }
    @SuppressWarnings("ConstantConditions")
    public int getPing() {
        return ReflectionUtil.getTField(entityPlayer, "ping");
    }
    public void swingHand(){
        ByteBuf byteBuf = Unpooled.buffer();
        ByteBufUtil.writeVarInt(byteBuf, 0x0B);
        ByteBufUtil.writeVarInt(byteBuf, player.getEntityId());
        byteBuf.writeByte(0);
        channel.writeAndFlush(byteBuf);
    }

    public void kick(final String message){
        ByteBuf byteBuf = Unpooled.buffer();
        ByteBufUtil.writeVarInt(byteBuf, 0x40);
        ByteBufUtil.writeString(byteBuf, ChatColor.translateAlternateColorCodes('&', "{\"text\": \"" + message + "\"}"));
        channel.writeAndFlush(byteBuf).addListener(ChannelFutureListener.CLOSE);
    }

}