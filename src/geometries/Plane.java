package geometries;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;
import static primitives.Util.isZero;

/**
 * Class representing a plane in 3D space.
 */
public class Plane extends Geometry {
    private final Point point;
    private final Vector normal;

    /**
     * Constructor for a plane defined by three points.
     *
     * @param p1 first point
     * @param p2 second point
     * @param p3 third point
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.point = p1;
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        this.normal = v1.crossProduct(v2).normalize();    }

    /**
     * Constructor for a plane defined by a point and a normal vector.
     *
     * @param point  a point on the plane
     * @param normal the normal vector of the plane
     */
    public Plane(Point point, Vector normal) {
        this.point = point;
        this.normal = normal.normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        // Check if the ray origin is the same as the plane point
        if (point.equals(ray.getOrigin())) return null;

        double nv = normal.dotProduct(ray.getDirection());

        // Check if the ray is parallel to the plane (dot product = 0)
        if (isZero(nv)) return null;

        // Calculate intersection point's parameter t
        double t = normal.dotProduct(point.subtract(ray.getOrigin())) / nv;

        // If t is negative or zero, the intersection is behind the ray origin
        if (t <= 0 || isZero(t)) return null;

        // Compute intersection point
        Point intersectionPoint = ray.getPoint(t);

        // Return the intersection wrapped in an Intersection object
        return List.of(new Intersection(this, intersectionPoint));
    }

}