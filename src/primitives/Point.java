package primitives;

public class Point
{
    protected final Double3 xyz; // Final to ensure immutability
    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * Constructor for Point.
     * @param xyz The Double3 object representing the coordinates.
     */
    public Point(Double3 xyz)
    {
        this.xyz = xyz;
    }
    public Point(double x, double y, double z )
    {
        this.xyz = new Double3(x, y, z);
    }


    /**
     * Checks if this point is equal to another object.
     * @param obj The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check if same object
        return (obj instanceof Point other )&&(this.xyz.equals(other.xyz)); // Check for null or different class

    }

    @Override
    public String toString() {
        return xyz.toString();
    }
    public Vector subtract(Point other)
    {
        return new Vector(this.xyz.subtract(other.xyz));
    }
    public Point add(Point other)
    {
        return new Point(this.xyz.add(other.xyz));
    }
    public double distanceSquared(Point other)
    {
        double dx = this.xyz.d1() - other.xyz.d1();
        double dy = this.xyz.d2() - other.xyz.d2();
        double dz = this.xyz.d3() - other.xyz.d3();
        return dx * dx + dy * dy + dz * dz;
    }
    public double distance (Point other)
    {
        return Math.sqrt(this.distanceSquared(other));
    }

}
