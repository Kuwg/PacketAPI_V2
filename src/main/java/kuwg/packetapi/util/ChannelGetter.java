package kuwg.packetapi.util;

import io.netty.channel.Channel;

import java.lang.reflect.Field;

public class ChannelGetter {
    private static final String v = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().substring(23);
    public static String getServerVersion() {
        return v;
    }

    public static io.netty.channel.Channel getChannel(org.bukkit.entity.Player player) {
        Class<?> craftPlayer = ReflectionUtil.classForName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer");
        java.lang.reflect.Method getHandle = ReflectionUtil.getMethod(craftPlayer, "getHandle");
        Object entityPlayer = ReflectionUtil.getInvokeResult(getHandle, player);
        assert entityPlayer != null:"Null Entity Player";
        Class<?> entityPlayerClass = entityPlayer.getClass();
        java.lang.reflect.Field playerConnectionField = ReflectionUtil.getDeclaredField(entityPlayerClass, "playerConnection");
        if(playerConnectionField==null){
            for(java.lang.reflect.Field field : ReflectionUtil.getFields(entityPlayerClass)){
                if(field.getType().getSimpleName().equalsIgnoreCase("PlayerConnection")){
                    playerConnectionField=field;
                }
            }
        }
        assert playerConnectionField != null:"Null player connection.";
        playerConnectionField.setAccessible(true);
        Object playerConnection = ReflectionUtil.getFieldInvocationResult(playerConnectionField, entityPlayer);
        if (playerConnection == null) {
            player.kickPlayer("Kicked for null connection.");
            throw new NullPointerException("PlayerConnection is null");
        }
        Class<?> playerConnectionClass = playerConnection.getClass();
        java.lang.reflect.Field networkManagerField = ReflectionUtil.getDeclaredField(playerConnectionClass, "networkManager");
        if(networkManagerField==null)for(Field field:ReflectionUtil.getFields(playerConnectionClass))if(field.getType().getSimpleName().contains("NetworkManager"))networkManagerField=field;



        assert networkManagerField != null:"Network Manager Null";
        networkManagerField.setAccessible(true);
        Object networkManager = ReflectionUtil.getFieldInvocationResult(networkManagerField, playerConnection);
        if (networkManager == null) {
            player.kickPlayer("Kicked for null network manager.");
            throw new NullPointerException("NetworkManager is null");
        }
        Class<?> networkManagerClass = networkManager.getClass();
        java.lang.reflect.Field channelField = ReflectionUtil.getDeclaredField(networkManagerClass, "channel");
        if(channelField==null){
            for(java.lang.reflect.Field field : ReflectionUtil.getFields(networkManagerClass)){
                if (field != null && field.getType().getSimpleName().equalsIgnoreCase("Channel")) {
                    channelField = field;
                }
            }
        }
        assert channelField != null:"Null Channel";
        channelField.setAccessible(true);
        return (io.netty.channel.Channel) ReflectionUtil.getFieldInvocationResult(channelField, networkManager);
    }
    public static Channel getFastChannel(Object networkManager){
        Class<?> networkManagerClass = networkManager.getClass();
        java.lang.reflect.Field channelField = ReflectionUtil.getDeclaredField(networkManagerClass, "channel");
        if(channelField==null){
            for(Field field : ReflectionUtil.getFields(networkManagerClass)){
                if (field != null && field.getType().getSimpleName().equalsIgnoreCase("Channel")) {
                    channelField = field;
                }
            }
        }
        assert channelField != null:"Null Channel";
        channelField.setAccessible(true);
        return (io.netty.channel.Channel) ReflectionUtil.getFieldInvocationResult(channelField, networkManager);
    }
}
