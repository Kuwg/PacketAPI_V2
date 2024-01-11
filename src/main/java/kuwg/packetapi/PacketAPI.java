package kuwg.packetapi;

import kuwg.packetapi.channel.ChannelInjector;
import kuwg.packetapi.in.BukkitListener;
import kuwg.packetapi.listener.PacketListenerManager;
import kuwg.packetapi.player.PacketPlayer;
import kuwg.packetapi.test.TestPacketListener;
import kuwg.packetapi.util.PAPIVer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static kuwg.packetapi.util.ChannelGetter.getServerVersion;

public final class PacketAPI extends JavaPlugin {

    private static PacketAPI instance;
    private final Map<Player, PacketPlayer> playerMap = new HashMap<>();
    private static PAPIVer ver;
    private final PacketListenerManager manager = new PacketListenerManager();
    //private final PluginReflectionHandler reflectionHandler = new PluginReflectionHandler();
    @Override
    public void onEnable() {
        instance=this;
        Bukkit.getOnlinePlayers().forEach(player -> playerMap.put(player, new PacketPlayer(player)));
        ver=new PAPIVer(getServerVersion().replaceAll("_",".").replace("v","").replace("R3","").replace("R2","").replace("R1",""));
        getLogger().log(Level.INFO, "Detected version \"" + ver + "\".");
        getServer().getPluginManager().registerEvents(new BukkitListener(), this);
        for(final Player on : Bukkit.getOnlinePlayers()){
            this.state(on, false);
            ChannelInjector.inject(this.getPacketPlayer(on));
        }

        getPacketListenerManager().registerListener(new TestPacketListener());
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Disabling the plugin, deleting cache for every player...");
        for(final Player on : Bukkit.getOnlinePlayers()){
            ChannelInjector.remove(this.getPacketPlayer(on));
            this.state(on, true);
        }
        getLogger().log(Level.INFO, "Disabled the plugin.");
    }
    public void state(Player player, boolean rem){
        if(rem)
            playerMap.remove(player);
        else playerMap.put(player, new PacketPlayer(player));
    }
    public PacketPlayer getPacketPlayer(Player player){
        return playerMap.get(player);
    }
    public static PacketAPI getInstance() {
        return instance;
    }
    public static PAPIVer getVer(){return ver;}
    public PacketListenerManager getPacketListenerManager(){
        return manager;
    }


    //public PluginReflectionHandler getReflectionHandler() {return reflectionHandler;}
}
