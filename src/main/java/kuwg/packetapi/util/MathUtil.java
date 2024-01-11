package kuwg.packetapi.util;

import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.mojang.BoundingBox;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings({"unused", "unchecked"})
public class MathUtil{

    public static double getAverage(final Collection<? extends Number> data) {
        return (data!=null&&!data.isEmpty())?sum(data)/data.size():0.0;
    }
    public static double sum(final Collection<? extends Number> data){
        if (data==null||data.isEmpty()) return 0.0;
        double sum = 0.0;
        for (final Number number : data) {
            sum += number.doubleValue();
        }
        return sum;
    }
    public static double getPreciseDistance(Player player, Player target){
        BoundingBox targetBox = PacketAPI.getInstance().getPacketPlayer(target).getBoundingBox().expand(0.08, 0, 0.08);
        Vector from = new Vector(player.getLocation().getX(),
                player.getLocation().getY()+1.62, player.getLocation().getZ());
        Vector to = new Vector(MathUtil.clamp(from.getX(), targetBox.minX, targetBox.maxX),
                MathUtil.clamp(from.getY(), targetBox.minY, targetBox.maxY),
                MathUtil.clamp(from.getZ(), targetBox.minZ, targetBox.maxZ));
        return to.distance(from);
    }
    public static double getAngle(final double minX, final double minZ, final double maxX, final double maxZ) {
        final double degrees = Math.toDegrees(Math.atan2(minZ - maxZ, maxX - minX));
        return (degrees<0.0)?(360.0+degrees):degrees;
    }
    public static double hypotenuse(final double x, final double z) {
        return Math.sqrt(x*x+z*z);
    }
    public static double da(final double alpha, final double beta) {
        final double abs = Math.abs(alpha % 360.0 - beta % 360.0);
        return Math.abs(Math.min(360.0 - abs, abs));
    }
    public static double clamp(double d, double d2, double d3) {
        if (d < d2) {
            return d2;
        }
        return Math.min(d, d3);
    }


    public static int round(double num){
        return (int) Math.round(num);
    }
    public static int floor(double num){
        return (int) Math.floor(num);
    }
    public static int ceil(double num){
        return (int) Math.ceil(num);
    }
    public double hyp(double x, double y){
        return Math.sqrt(x*x+y+y);
    }

    /**
     * <h1>Kuwg's Constant</h1>
     * <p>
     *     I made this constant for working in 2D graphics transformations.
     *     It's useful for dividing a screen into sections.
     *     <pre>
     *            √2
     *         ________
     *
     *  K =     (1+√5)
     *         ________
     *
     *             2
     *     </pre>
     *
     * </p>
     * <p>
     *     <b>Example usage in games:</b>
     *     <pre>
     *         double length = 100;
     *         double[] sections = new double[2];
     *         sections[0] = length * KuwgConstant;
     *         sections[1] = length * (1 - KuwgConstant);
     *     </pre>
     *     <b>Example usage in a Spigot plugin:</b>
     *     <pre>
     *         public Inventory createCustomInventory(Player player) {
     *             Inventory inv = Bukkit.createInventory(player, 9, "Custom Inventory");
     *
     *             int section1Size = (int) (9 * KuwgConstant);
     *             int section2Size = 9 - section1Size;
     *
     *             // Add items to the inventory based on the sizes of the sections...
     *
     *             return inv;
     *         }
     *     </pre>
     * </p>
     * @since 1.6
     */
    public static final double KuwgConstant = 0.8740320488976422;

















    public static <T extends Number> T getRandom(T from, T to) {
        if (from.doubleValue() > to.doubleValue())throw new IllegalArgumentException("Invalid range: 'from' should be less than or equal to 'to'");
        long randomLong = ThreadLocalRandom.current().nextLong(from.longValue(), to.longValue() + 1);
        if (from instanceof Integer)
            return (T) Integer.valueOf((int) randomLong);
        if (from instanceof Long)
            return (T) Long.valueOf(randomLong);
        if (from instanceof Double)
            return (T) Double.valueOf(randomLong);
        if (from instanceof Float)
            return (T) Float.valueOf(randomLong);

        throw new IllegalArgumentException("Unsupported numeric type");

    }

    private MathUtil(){
        throw new UnsupportedOperationException("You can't instantiate this class!");
    }
}
