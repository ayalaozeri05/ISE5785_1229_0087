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
     * getter of the direction
     * @return the direction
     */
    public Vector getDirection() {
        return direction;
    }
    /**
     * getter of the head
     * @return the head
     */
    public Point getOrigin() {
        return origin;
    }

    /**
     * Computes the point on the ray based on the given parameter.
     *
     * @param t The parameter determining the position of the point along the ray.
     * @return The point on the ray corresponding to the given parameter.
     */
    public Point getPoint(double t){
        if(Util.isZero(t))
            return getOrigin();
        return getOrigin().add(getDirection().scale(t));
    }
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
