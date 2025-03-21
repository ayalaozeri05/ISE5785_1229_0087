package primitives;

/**
 * Represents a vector in 3D Cartesian space.
 */
public class Vector extends Point {

    /**
     * Constructor to initialize a vector using three coordinate values.
     *
     * @param x the x-coordinate of the vector.
     * @param y the y-coordinate of the vector.
     * @param z the z-coordinate of the vector.
     * @throws IllegalArgumentException if the vector is a zero vector.
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (this.coordinates.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be the zero vector");
        }
    }

    /**
     * Constructor to initialize a vector using a `Double3` object.
     *
     * @param coordinates the `Double3` object representing the vector's coordinates.
     * @throws IllegalArgumentException if the vector is a zero vector.
     */
    public Vector(Double3 coordinates) {
        super(coordinates);
        if (this.coordinates.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be the zero vector");
        }
    }

    /**
     * Scales (multiplies) the vector by a scalar value.
     *
     * @param scalar the scalar to scale the vector.
     * @return a new scaled vector.
     */
    public Vector scale(double scalar) {
        return new Vector(this.coordinates.scale(scalar));
    }

    /**
     * Computes the dot product of this vector with another vector.
     *
     * @param other the other vector.
     * @return the result of the dot product.
     */
    public double dotProduct(Vector other) {
        return this.coordinates.d1() * other.coordinates.d1() +
                this.coordinates.d2() * other.coordinates.d2() +
                this.coordinates.d3() * other.coordinates.d3();
    }



    /**
     * Computes the cross product of this vector with another vector.
     *
     * @param other the other vector.
     * @return a new vector that is perpendicular to both this vector and the other vector.
     */
    public Vector crossProduct(Vector other) {
        return new Vector(
                this.coordinates.d2() * other.coordinates.d3() - this.coordinates.d3() * other.coordinates.d2(),
                this.coordinates.d3() * other.coordinates.d1() - this.coordinates.d1() * other.coordinates.d3(),
                this.coordinates.d1() * other.coordinates.d2() - this.coordinates.d2() * other.coordinates.d1()
        );
    }

    /**
     * Calculates the squared length (magnitude) of the vector.
     *
     * @return the squared length of the vector.
     */
    public double lengthSquared() {
        return this.dotProduct(this);
    }

    /**
     * Calculates the length (magnitude) of the vector.
     *
     * @return the length of the vector.
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes the vector (scales it to have a length of 1).
     *
     * @return a new normalized vector with the same direction as this vector.
     */
    public Vector normalize() {
        double length = length();
        return new Vector(this.coordinates.scale(1 / length));
    }

    @Override
    public String toString()
    {
        return  super.toString();
        //return xyz.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return super.equals(obj);
    }
}
