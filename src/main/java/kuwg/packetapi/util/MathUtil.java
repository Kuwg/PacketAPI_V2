package kuwg.packetapi.util;

import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.mojang.BoundingBox;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings({"unused", "ManualMinMaxCalculation"})
public class MathUtil{
    public static double calculateAngleDifference(final double angle1, final double angle2) {
        return Math.abs(angle1 % 360.0 - angle2 % 360.0);
    }
    public static double getAverage(final Collection<? extends Number> data) {
        return (data!=null&&!data.isEmpty())?data.stream().mapToDouble(Number::doubleValue).sum()/data.size():0.0;
    }
    public static double sum(final Collection<? extends Number> data){
        return data == null || data.isEmpty() ? 0.0 : data.stream().mapToDouble(Number::doubleValue).sum();
    }
    public static double getPreciseDistance(Player player, Player target){
        final BoundingBox targetBox = PacketAPI.getInstance().getPacketPlayer(target).getBoundingBox()
                .expand(0.08, 0, 0.08);
        return new Vector(MathUtil.clamp(new Vector(player.getLocation().getX(),
                player.getLocation().getY()+1.62, player.getLocation().getZ()).getX(),
                targetBox.minX, targetBox.maxX),
                MathUtil.clamp(new Vector(player.getLocation().getX(),
                        player.getLocation().getY()+1.62, player.getLocation().getZ()).getY(),
                        targetBox.minY, targetBox.maxY),
                MathUtil.clamp(new Vector(player.getLocation().getX(),
                        player.getLocation().getY()+1.62, player.getLocation().getZ()).getZ(),
                        targetBox.minZ, targetBox.maxZ)).distance(new Vector(player.getLocation().getX(),
                                player.getLocation().getY()+1.62, player.getLocation().getZ()));
    }
    public static double getAngle(final double minX, final double minZ, final double maxX, final double maxZ) {
        final double degrees = Math.toDegrees(Math.atan2(minZ - maxZ, maxX - minX));
        return (degrees<0.0)?(360.0+degrees):degrees;
    }
    public static double hypotenuse(final double x, final double z) {
        return Math.sqrt(x*x+z*z);
    }
    public static double calculateModifiedAngleDifference(final double angle1, final double angle2) {
        double absoluteDifference = Math.abs(angle1 % 360.0 - angle2 % 360.0);

        return Math.abs((360.0 - absoluteDifference != 360.0 - absoluteDifference) ?
                360.0 - absoluteDifference :
                ((360.0 - absoluteDifference == 0.0d) && (absoluteDifference == 0.0d) &&
                        (Double.doubleToRawLongBits(absoluteDifference) == negativeZeroDoubleBits)) ?
                        absoluteDifference :
                        (360.0 - absoluteDifference <= absoluteDifference) ? 360.0 - absoluteDifference : absoluteDifference);
    }

    public static double clamp(double d, double d2, double d3) {
        return d < d2 ? d2 : (d != d) ? d :
                ((d == 0.0d) && (d3 == 0.0d) && (Double.doubleToRawLongBits(d3) == negativeZeroDoubleBits))
                        ? d3 :
                        (d<=d3)?d:d3;
    }

    private static final long negativeZeroDoubleBits = Double.doubleToRawLongBits(-0.0d);

    public static int round(double num){
        return (int) Math.round(num);
    }
    public static int floor(double num){
        return (int) Math.floor(num);
    }
    public static int ceil(double num){
        return (int) Math.ceil(num);
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
     *     <pre>In simple terms that is √2/(1+√5)/2</pre>
     * </p>
     * <p>
     *     <b>Example usage in games:</b>
     *
     *     <pre>
     *         {@code
     *         double length = 100;
     *         double[] sections = new double[2];
     *         sections[0] = length * KuwgConstant;
     *         sections[1] = length * (1 - KuwgConstant);
     *         }
     *     </pre>
     *
     *     <p>Example usage in a Spigot plugin:</p>
     *     <pre>
     *         {@code
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
     *         }
     *     </pre>
     * </p>
     * @since PacketAPI 1.6
     */
    public static final double KuwgConstant = 0.21850801222441055;

















    private MathUtil(){
        throw new UnsupportedOperationException("You can't instantiate this class!");
    }
}
