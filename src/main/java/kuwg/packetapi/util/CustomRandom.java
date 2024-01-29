package kuwg.packetapi.util;

@SuppressWarnings({"unused", "unchecked"})
public class CustomRandom {
    private static long seed = System.nanoTime();
    private static long obfuscationKey = 987589067340954331L;

    public static boolean randomBool() {
        seed ^= (seed << 21);
        seed ^= (seed >>> 35);
        seed ^= (seed << 4);
        return (seed & 1) == 0;
    }

    public static <T extends Number> T getRandom(T from, T to) {
        if (from.doubleValue() > to.doubleValue())
            throw new IllegalArgumentException("Invalid range: 'from' should be less than or equal to 'to'!");
        long seed1 = seed;
        obfuscationKey ^= (obfuscationKey << 13);
        obfuscationKey ^= (obfuscationKey >>> 17);
        obfuscationKey ^= (obfuscationKey << 5);
        seed1 ^= (seed1 << 21);
        seed1 ^= (seed1 >>> 35);
        seed1 ^= (seed1 << 4);
        long randomLong = seed1 ^ obfuscationKey;
        randomLong = Math.abs(randomLong % (to.longValue() - from.longValue() + 1)) + from.longValue();

        if (from instanceof Integer)
            return (T) Integer.valueOf((int) randomLong);

        if (from instanceof Long)
            return (T) Long.valueOf(randomLong);

        if (from instanceof Double)
            return (T) Double.valueOf(randomLong);

        if (from instanceof Float)
            return (T) Float.valueOf(randomLong);

        throw new IllegalArgumentException("Unsupported numeric type: " + from.getClass().getSimpleName() +
                ". Supported types are Integer, Long, Double, Float");
    }

}
