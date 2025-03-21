package primitives;

/**
 * Represents a point in a 3D Cartesian space.
 */
public class Point {
    /**
     * The coordinates of the point in 3D space.
     */
    protected final Double3 coordinates;

    /**
     * A constant point representing the origin (0,0,0) in 3D space.
     */
    public static final Point ZERO = new Point(new Double3(0.0, 0.0, 0.0));

    /**
     * Constructor to initialize a point using three coordinate values.
     *
     * @param x the x-coordinate of the point.
     * @param y the y-coordinate of the point.
     * @param z the z-coordinate of the point.
     */
    public Point(double x, double y, double z) {
        this.coordinates = new Double3(x, y, z);
    }

    /**
     * Constructor to initialize a point using a `Double3` object.
     *
     * @param coordinates the `Double3` object representing the coordinates.
     */
    public Point(Double3 coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Subtracts another point from this point, returning a vector pointing from
     * the other point to this point.
     *
     * @param other the point to subtract from this point.
     * @return the resulting vector.
     */
    public Vector subtract(Point other) {
        return new Vector(this.coordinates.subtract(other.coordinates));
    }

    /**
     * Adds a vector to this point, returning a new point as the result.
     *
     * @param vector the vector to add to this point.
     * @return the resulting point.
     */
    public Point add(Vector vector) {
        return new Point(this.coordinates.add(vector.coordinates));
    }

    /**
     * Calculates the squared distance between this point and another point.
     *
     * @param other the other point.
     * @return the squared distance between the two points.
     */
    public double distanceSquared(Point other) {
        double dx = this.coordinates.d1() - other.coordinates.d1();
        double dy = this.coordinates.d2() - other.coordinates.d2();
        double dz = this.coordinates.d3() - other.coordinates.d3();
        return dx * dx + dy * dy + dz * dz;
    }
    /**
     * Calculates the distance between this point and another point.
     *
     * @param other the other point.
     * @return the distance between the two points.
     */
    public double distance(Point other) {
        return Math.sqrt(distanceSquared(other));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        return (obj instanceof Point other) && this.coordinates.equals(other.coordinates);
    }

    @Override
    public String toString() {
        return coordinates.toString();
    }
}
