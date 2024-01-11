package kuwg.packetapi.mojang;

import kuwg.packetapi.util.MathUtil;
import kuwg.packetapi.util.ReflectionUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Objects;

import static kuwg.packetapi.util.ChannelGetter.getServerVersion;

public class BoundingBox {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public BoundingBox(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }
    @SuppressWarnings("ConstantConditions")
    public BoundingBox(Entity entity){
        Object craftEntity= Objects.requireNonNull(ReflectionUtil.classForName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftEntity")).cast(entity);
        Object objectEntity=ReflectionUtil.getInvokeResult(ReflectionUtil.getMethod(craftEntity.getClass(), "getHandle"), craftEntity);
        assert objectEntity != null;
        Object AxisAlignedBB = ReflectionUtil.getInvokeResult(ReflectionUtil.getMethod(objectEntity.getClass(), "getBoundingBox"), objectEntity);
        double a= ReflectionUtil.getTField(AxisAlignedBB, "a");
        double b= ReflectionUtil.getTField(AxisAlignedBB, "b");
        double c= ReflectionUtil.getTField(AxisAlignedBB, "c");
        double d= ReflectionUtil.getTField(AxisAlignedBB, "d");
        double e= ReflectionUtil.getTField(AxisAlignedBB, "e");
        double f= ReflectionUtil.getTField(AxisAlignedBB, "f");
        this.minX = Math.min(a, d);
        this.minY = Math.min(b, e);
        this.minZ = Math.min(c, f);
        this.maxX = Math.max(a, d);
        this.maxY = Math.max(b, e);
        this.maxZ = Math.max(c, f);
    }

    @SuppressWarnings("ConstantConditions")
    public BoundingBox(Player player){
        Object craftEntity=Objects.requireNonNull(ReflectionUtil.classForName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftEntity")).cast(player);
        Object objectEntity=ReflectionUtil.getInvokeResult(ReflectionUtil.getMethod(craftEntity.getClass(), "getHandle"), craftEntity);
        assert objectEntity != null;
        Object AxisAlignedBB = ReflectionUtil.getInvokeResult(ReflectionUtil.getMethod(objectEntity.getClass(), "getBoundingBox"), objectEntity);
        double a= ReflectionUtil.getTField(AxisAlignedBB, "a");
        double b= ReflectionUtil.getTField(AxisAlignedBB, "b");
        double c= ReflectionUtil.getTField(AxisAlignedBB, "c");
        double d= ReflectionUtil.getTField(AxisAlignedBB, "d");
        double e= ReflectionUtil.getTField(AxisAlignedBB, "e");
        double f= ReflectionUtil.getTField(AxisAlignedBB, "f");
        this.minX = Math.min(a, d);
        this.minY = Math.min(b, e);
        this.minZ = Math.min(c, f);
        this.maxX = Math.max(a, d);
        this.maxY = Math.max(b, e);
        this.maxZ = Math.max(c, f);
    }

    /**
     * <h1>distance</h1>
     * <p>Returns the distance between the entity and the bounding box using min/max coordinates.</p>
     * @param entity the vector 'to'
     * @return "reach" between the Bounding Box and the entity.
     */
    public double distance(Entity entity){
        Vector from = new Vector(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ());
        Vector to = new Vector(MathUtil.clamp(from.getX(), this.minX, this.maxX),
                MathUtil.clamp(from.getY(), this.minY, this.maxY),
                MathUtil.clamp(from.getZ(), this.minZ, this.maxZ));
        return to.distance(from);
    }

    /**
     * <h1>distance</h1>
     * <p>Returns the distance between the world coordinates and the bounding box using min/max coordinates.</p>
     * @param x coord
     * @param y coord
     * @param z coord
     * @return "reach" between the Bounding Box and the world coordinates.
     */
    public double distance(double x, double y, double z){
        Vector from = new Vector(x,y,z);
        return Math.min(6, new Vector(MathUtil.clamp(from.getX(), this.minX, this.maxX),
                MathUtil.clamp(from.getY(), this.minY, this.maxY),
                MathUtil.clamp(from.getZ(), this.minZ, this.maxZ)).distance(from));
    }
    /**
     * Adds the coordinates to the bounding box extending it if the point lies outside the current ranges. Args: x, y, z
     */
    public BoundingBox addCoord(double x, double y, double z)
    {
        double d0 = this.minX;
        double d1 = this.minY;
        double d2 = this.minZ;
        double d3 = this.maxX;
        double d4 = this.maxY;
        double d5 = this.maxZ;

        if (x < 0.0D)
        {
            d0 += x;
        }
        else if (x > 0.0D)
        {
            d3 += x;
        }

        if (y < 0.0D)
        {
            d1 += y;
        }
        else if (y > 0.0D)
        {
            d4 += y;
        }

        if (z < 0.0D)
        {
            d2 += z;
        }
        else if (z > 0.0D)
        {
            d5 += z;
        }

        return new BoundingBox(d0, d1, d2, d3, d4, d5);
    }

    /**
     * Returns a bounding box expanded by the specified vector (if negative numbers are given it will shrink). Args: x,
     * y, z
     */
    public BoundingBox expand(double x, double y, double z)
    {
        double d0 = this.minX - x;
        double d1 = this.minY - y;
        double d2 = this.minZ - z;
        double d3 = this.maxX + x;
        double d4 = this.maxY + y;
        double d5 = this.maxZ + z;
        return new BoundingBox(d0, d1, d2, d3, d4, d5);
    }

    public BoundingBox union(BoundingBox other)
    {
        double d0 = Math.min(this.minX, other.minX);
        double d1 = Math.min(this.minY, other.minY);
        double d2 = Math.min(this.minZ, other.minZ);
        double d3 = Math.max(this.maxX, other.maxX);
        double d4 = Math.max(this.maxY, other.maxY);
        double d5 = Math.max(this.maxZ, other.maxZ);
        return new BoundingBox(d0, d1, d2, d3, d4, d5);
    }

    /**
     * returns an AABB with corners x1, y1, z1 and x2, y2, z2
     */
    public static BoundingBox fromBounds(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        double d0 = Math.min(x1, x2);
        double d1 = Math.min(y1, y2);
        double d2 = Math.min(z1, z2);
        double d3 = Math.max(x1, x2);
        double d4 = Math.max(y1, y2);
        double d5 = Math.max(z1, z2);
        return new BoundingBox(d0, d1, d2, d3, d4, d5);
    }

    /**
     * Offsets the current bounding box by the specified coordinates. Args: x, y, z
     */
    public BoundingBox offset(double x, double y, double z)
    {
        return new BoundingBox(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public BoundingBox offsetAndUpdate(double d1, double d2, double d3) {
        this.minX += d1;
        this.minY += d2;
        this.minZ += d3;
        this.maxX += d1;
        this.maxY += d2;
        this.maxZ += d3;
        return this;
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and Z dimensions, calculate the offset between them
     * in the X dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise, return the calculated offset.
     */
    public double calculateXOffset(BoundingBox other, double offsetX)
    {
        if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ)
        {
            if (offsetX > 0.0D && other.maxX <= this.minX)
            {
                double d1 = this.minX - other.maxX;

                if (d1 < offsetX)
                {
                    offsetX = d1;
                }
            }
            else if (offsetX < 0.0D && other.minX >= this.maxX)
            {
                double d0 = this.maxX - other.minX;

                if (d0 > offsetX)
                {
                    offsetX = d0;
                }
            }

        }
        return offsetX;
    }

    /**
     * if instance and the argument bounding boxes overlap in the X and Z dimensions, calculate the offset between them
     * in the Y dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise, return the calculated offset.
     */
    public double calculateYOffset(BoundingBox other, double offsetY)
    {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            if (offsetY > 0.0D && other.maxY <= this.minY)
            {
                double d1 = this.minY - other.maxY;

                if (d1 < offsetY)
                {
                    offsetY = d1;
                }
            }
            else if (offsetY < 0.0D && other.minY >= this.maxY)
            {
                double d0 = this.maxY - other.minY;

                if (d0 > offsetY)
                {
                    offsetY = d0;
                }
            }

        }
        return offsetY;
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and X dimensions, calculate the offset between them
     * in the Z dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise, return the calculated offset.
     */
    public double calculateZOffset(BoundingBox other, double offsetZ)
    {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY)
        {
            if (offsetZ > 0.0D && other.maxZ <= this.minZ)
            {
                double d1 = this.minZ - other.maxZ;

                if (d1 < offsetZ)
                {
                    offsetZ = d1;
                }
            }
            else if (offsetZ < 0.0D && other.minZ >= this.maxZ)
            {
                double d0 = this.maxZ - other.minZ;

                if (d0 > offsetZ)
                {
                    offsetZ = d0;
                }
            }

        }
        return offsetZ;
    }

    /**
     * Returns whether the given bounding box intersects with this one. Args: axisAlignedBB
     */
    public boolean intersectsWith(BoundingBox other)
    {
        return other.maxX > this.minX && other.minX < this.maxX && (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ);
    }

    /**
     * Returns the average length of the edges of the bounding box.
     */
    public double getAverageEdgeLength()
    {
        double d0 = this.maxX - this.minX;
        double d1 = this.maxY - this.minY;
        double d2 = this.maxZ - this.minZ;
        return (d0 + d1 + d2) / 3.0D;
    }

    /**
     * Returns a bounding box that is inset by the specified amounts
     */
    public BoundingBox contract(double x, double y, double z)
    {
        double d0 = this.minX + x;
        double d1 = this.minY + y;
        double d2 = this.minZ + z;
        double d3 = this.maxX - x;
        double d4 = this.maxY - y;
        double d5 = this.maxZ - z;
        return new BoundingBox(d0, d1, d2, d3, d4, d5);
    }

    @Override
    public String toString()
    {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }
}