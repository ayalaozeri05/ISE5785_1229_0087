package primitives;

/**
 * Represents a ray in 3D space.
 */
public class Ray {
    /**
     * The starting point of the ray.
     */
    private final Point origin;

    /**
     * The direction vector of the ray.
     */
    private final Vector direction;

    /**
     * Constructor to initialize a ray with a given origin point and direction vector.
     *
     * @param origin    the origin point of the ray.
     * @param direction the direction vector of the ray (will be normalized).
     */
    public Ray(Point origin, Vector direction) {
        this.origin = origin;
        this.direction = direction.normalize(); // Ensures the direction is normalized
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.origin.equals(other.origin)
                && this.direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return "Ray [origin=" + origin + ", direction=" + direction + "]";
    }
}
