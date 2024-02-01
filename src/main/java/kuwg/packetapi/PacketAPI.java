package kuwg.packetapi;

import kuwg.packetapi.channel.ChannelInjector;
import kuwg.packetapi.listener.manager.PacketListenerManager;
import kuwg.packetapi.player.PingUpdater;
import kuwg.packetapi.player.PacketPlayer;
import kuwg.packetapi.util.PAPIVer;
import kuwg.packetapi.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class PacketAPI extends JavaPlugin {

    private static PacketAPI instance;
    private final Map<Player, PacketPlayer> playerMap = new HashMap<>();
    private static PAPIVer ver;
    private final PacketListenerManager manager = new PacketListenerManager();
    private long lastReloadInput = System.currentTimeMillis();
    @Override
    public void onEnable() {

        PingUpdater.start();

        instance = this;
        Bukkit.getOnlinePlayers().forEach(player -> playerMap.put(player, new PacketPlayer(player)));
        ver = new PAPIVer(ReflectionUtil.v.replaceAll("_", ".").replace("v", "").replace("R3", "").replace("R2", "").replace("R1", ""));
        getLogger().log(Level.INFO, "Detected version " + ver + "!");
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.HIGHEST)
            public void onJoin(final PlayerJoinEvent event) {
                state(event.getPlayer(), true);
                ChannelInjector.inject(getPacketPlayer(event.getPlayer()));
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void onQuit(final PlayerQuitEvent event) {
                ChannelInjector.remove(getPacketPlayer(event.getPlayer()));
                PacketAPI.getInstance().state(event.getPlayer(), false);
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void onPlayerCommand(final PlayerCommandPreprocessEvent event){
                if(event.getPlayer().hasPermission("bukkit.command.reload")&&(event.getMessage().equalsIgnoreCase("/reload")||
                        event.getMessage().toLowerCase().startsWith("/reload ")))
                    lastReloadInput=System.currentTimeMillis();
            }
            @EventHandler(priority = EventPriority.HIGHEST)
            public void onConsoleCommand(final ServerCommandEvent event){
                if(event.getCommand().equalsIgnoreCase("reload")||event.getCommand().toLowerCase().startsWith("reload "))
                    lastReloadInput=System.currentTimeMillis();
            }
        }, this);
        for (final Player on : Bukkit.getOnlinePlayers()) {
            this.state(on, true);
            ChannelInjector.inject(this.getPacketPlayer(on));
        }
    }

    @Override
    public void onDisable() {
        PingUpdater.stop();
        /*
        if(System.currentTimeMillis()-this.lastReloadInput<2000) {
            // reloading
        }
        else Bukkit.getOnlinePlayers().forEach(on -> {
            ChannelInjector.remove(this.getPacketPlayer(on));
            this.state(on, false);
        });
         */
        if(System.currentTimeMillis()-this.lastReloadInput<2000) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN +
                    "Reloading... Remember that many plugins do " + ChatColor.UNDERLINE
                    + "NOT" + ChatColor.RESET + ChatColor.GREEN + " support reloading!"
            );
        }
        getLogger().log(Level.INFO, "Disabled the plugin.");
    }

    public void state(Player player, boolean join) {
        if (!join)
            playerMap.remove(player);
        else playerMap.put(player, new PacketPlayer(player));
    }

    public PacketPlayer getPacketPlayer(Player player) {
        return playerMap.get(player);
    }

    public static PacketAPI getInstance() {
        return instance;
    }

    public static PAPIVer getVer() {
        return ver;
    }

    public PacketListenerManager getPacketListenerManager() {
        return manager;
    }


    //public PluginReflectionHandler getReflectionHandler() {return reflectionHandler;}
}
