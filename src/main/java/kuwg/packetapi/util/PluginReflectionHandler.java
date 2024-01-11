package kuwg.packetapi.util;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Field;
import java.util.Objects;

import static kuwg.packetapi.util.ChannelGetter.getServerVersion;

public class PluginReflectionHandler {
    private Object serverConnection;
    private Object minecraftServer;
    private Object craftServer;
    public PluginReflectionHandler() {
        try {
            Server server = Bukkit.getServer();
            this.craftServer = Objects.requireNonNull(ReflectionUtil.classForName("org.bukkit.craftbukkit." + getServerVersion() + "CraftServer")).cast(server);
            this.minecraftServer = ReflectionUtil.invokeMethod(craftServer, "getServer");
            assert minecraftServer != null;
            for (Field f : minecraftServer.getClass().getDeclaredFields()) {
                if (f.getType().getSimpleName().contains("ServerConnection"))
                    serverConnection = f.get(minecraftServer);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Object getCraftServer() {
        return craftServer;
    }

    public Object getServerConnection() {
        return serverConnection;
    }

    public Object getMinecraftServer() {
        return minecraftServer;
    }
}
