package kuwg.packetapi.mojang;

@SuppressWarnings("unused")
public class Vec3D {
    public final double x;
    public final double y;
    public final double z;

    public Vec3D(double x, double y, double z) {
        if (x == -0.0) {
            x = 0.0;
        }

        if (y == -0.0) {
            y = 0.0;
        }

        if (z == -0.0) {
            z = 0.0;
        }

        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Vec3D normalize() {
        double length = Math.sqrt(x * x + y * y + z * z);
        return length < 1.0E-4 ? new Vec3D(0.0, 0.0, 0.0) : new Vec3D(x / length, y / length, z / length);
    }

    public double dot(Vec3D other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public Vec3D subtract(Vec3D other) {
        return add(-other.x, -other.y, -other.z);
    }

    public Vec3D add(Vec3D other) {
        return add(other.x, other.y, other.z);
    }

    public Vec3D add(double x, double y, double z) {
        return new Vec3D(this.x + x, this.y + y, this.z + z);
    }

    public double distanceSquared(Vec3D other) {
        double dx = other.x - x;
        double dy = other.y - y;
        double dz = other.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3D scale(Vec3D other, double factor) {
        double dx = other.x - x;
        double dy = other.y - y;
        double dz = other.z - z;
        if (dx * dx < 1.0000000116860974E-7) {
            return null;
        } else {
            double scale = (factor - x) / dx;
            return !(scale < 0.0) && !(scale > 1.0) ? new Vec3D(x + dx * scale, y + dy * scale, z + dz * scale) : null;
        }
    }

    //public String toString() {return "(" + x + ", " + y + ", " + z + ")";}


    @Override
    public String toString() {
        return "Vec3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public Vec3D rotateX(float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        double newY = y * cos + z * sin;
        double newZ = z * cos - y * sin;
        return new Vec3D(x, newY, newZ);
    }

    public Vec3D rotateY(float angle) {
        return new Vec3D(this.x * Math.cos(angle) + this.z * Math.sin(angle), this.y,
                this.z * Math.cos(angle) - this.x * Math.sin(angle));
    }
}
