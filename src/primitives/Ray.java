package primitives;

import java.util.List;
import geometries.Intersectable.Intersection;



/**
 * Represents a ray in 3D space.
 */
public class Ray {

    /**
     * A small value used to determine if a value is close to zero.
     */
    private static final double DELTA = 0.1;

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

    /**
     * Constructs a Ray from a point and direction, offset by DELTA in the direction of the normal.
     *
     * @param head      The origin point of the ray.
     * @param direction The direction vector.
     * @param normal    The surface normal at the origin point.
     */
    public Ray(Point head, Vector direction, Vector normal) {
        this.direction = direction.normalize();
        double nv = direction.dotProduct(normal);
        Vector delta = normal.scale(nv > 0 ? DELTA : -DELTA);
        this.origin= head.add(delta);
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

    /**
     *  method to find the closest point to the head of the ray
     * @param points list of points
     * @return the closest point to the head of the ray
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null ? null
                : findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
    }

    /**
     *  method to find the closest point to the head of the ray
     * @param points list of geo points
     * @return the closest point to the head of the ray
     */
    public Intersection findClosestIntersection(List<Intersection> points) {
        if (points == null || points.isEmpty())
            return new Intersection(null, null);
        Intersection closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Intersection point : points) {
            double distance = origin.distance(point.point);
            if (distance < minDistance) {
                minDistance = distance;
                closest = point;
            }
        }
        return closest;
    }
    }


