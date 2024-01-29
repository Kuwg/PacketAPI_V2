package kuwg.packetapi.player;

import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.util.CustomRandom;
import org.bukkit.Bukkit;

import java.util.Timer;
import java.util.TimerTask;

public class PingUpdater {

    private static Timer mTimer;
    private static TimerTask mTimerTask;

    /**
     * Every how many ms the ping of every player needs to be updated.
     * Be aware that reducing this can cause the thread to crash if there are too many players online.
     */
    public static long ms = 2000;
    public static void start() {

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                final int id = CustomRandom.getRandom(1000, 999999);
                Bukkit.getOnlinePlayers().forEach((on) -> {
                    final PacketPlayer player = PacketAPI.getInstance().getPacketPlayer(on);
                    if(player!=null)
                        player.sendKeepAlivePacket(id);
                });
            }
        };
        mTimer.schedule(mTimerTask, 0, ms);
    }
    public synchronized static void stop() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }
        if (mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
        }
    }
}
