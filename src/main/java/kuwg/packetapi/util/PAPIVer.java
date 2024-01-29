package kuwg.packetapi.util;

import java.util.Arrays;

public class PAPIVer {
    private final int[] versionIntArray;
    public PAPIVer(final int... version) {
        this.versionIntArray = version;
    }
    public PAPIVer(final String version) {
        String[] versionIntegers = version.split("\\.");
        int length = versionIntegers.length;
        this.versionIntArray = new int[length];
        for (int i = 0; i < length; i++) {
            versionIntArray[i] = Integer.parseInt(versionIntegers[i]);
        }
    }
    public int compareTo(PAPIVer version) {
        int localLength = versionIntArray.length;
        int oppositeLength = version.versionIntArray.length;
        int length = Math.max(localLength, oppositeLength);
        for (int i = 0; i < length; i++) {
            int localInteger = i < localLength ? versionIntArray[i] : 0;
            int oppositeInteger = i < oppositeLength ? version.versionIntArray[i] : 0;
            if (localInteger > oppositeInteger) {
                return 1;
            } else if (localInteger < oppositeInteger) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Does the {@link #compareTo(PAPIVer)} return 1?
     *
     * @param version Compared version.
     * @return Is this newer than the compared version.
     */
    public boolean isNewerThan(PAPIVer version) {
        return compareTo(version) == 1;
    }

    /**
     * Does the {@link #compareTo(PAPIVer)} return -1?
     *
     * @param version Compared version.
     * @return Is this older than the compared version.
     */
    public boolean isOlderThan(PAPIVer version) {
        return compareTo(version) == -1;
    }

    /**
     * Represented as an array.
     *
     * @return Array version.
     */
    public int[] asArray() {
        return versionIntArray;
    }

    /**
     * Is this version equal to the compared object.
     * The object must be a PAPIVer and the array values must be equal.
     *
     * @param obj Compared object.
     * @return Are they equal?
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof PAPIVer) {
            return Arrays.equals(versionIntArray, ((PAPIVer) obj).versionIntArray);
        }
        return false;
    }
    public boolean equals(String s){
        return this.toString().equals(s);
    }
    /**
     * Returns if a version is newer or equal to the compared version.
     * Like 1.8=1.8 so true.
     *
     * @param ver Compared version.
     * @return if equals
     */
    public boolean isNewerOrEquals(String ver){
        final PAPIVer version = new PAPIVer(ver);
        return this.equals(version)||this.isNewerThan(version);
    }

    /**
     * Represent the version as a string.
     *
     * @return String representation.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(versionIntArray.length * 2 - 1).append(versionIntArray[0]);
        for (int i = 1; i < versionIntArray.length; i++) {
            sb.append(".").append(versionIntArray[i]);
        }
        return sb.toString();
    }
}