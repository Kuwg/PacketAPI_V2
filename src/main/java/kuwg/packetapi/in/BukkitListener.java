package kuwg.packetapi.in;

import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.channel.ChannelInjector;
import kuwg.packetapi.player.PacketPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent event){
        final Player player = event.getPlayer();
        PacketAPI.getInstance().state(player, false);
        final PacketPlayer packetPlayer = PacketAPI.getInstance().getPacketPlayer(player);
        assert packetPlayer!=null:"Invalid null packet player for player " + player.getName();
        ChannelInjector.inject(packetPlayer);
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(final PlayerQuitEvent event){
        final Player player = event.getPlayer();
        final PacketPlayer packetPlayer = PacketAPI.getInstance().getPacketPlayer(player);
        assert packetPlayer!=null:"Invalid null packet player for player " + player.getName();
        ChannelInjector.remove(packetPlayer);
        PacketAPI.getInstance().state(event.getPlayer(), true);
    }
}
