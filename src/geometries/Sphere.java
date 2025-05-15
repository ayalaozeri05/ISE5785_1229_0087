package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;
import primitives.Util;
import static primitives.Util.isZero;


/**
 * Class representing a sphere in 3D space.
 */
public class Sphere extends RadialGeometry {
    private final Point center;

    /**
     * Constructor for a sphere.
     *
     * @param center the center point of the sphere
     * @param radius the radius of the sphere
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }


    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Point p0 = ray.getOrigin();
        Vector dir = ray.getDirection();

        // Special case: ray starts at the center of the sphere
        if (center.equals(p0)) {
            Point point = p0.add(dir.scale(radius));
            return List.of(new Intersection(this, point));
        }

        Vector u = center.subtract(p0);
        double tm = dir.dotProduct(u);
        double dSquared = u.lengthSquared() - tm * tm;

        // If the distance from the ray to the center is greater than the radius, no intersection
        double radiusSquared = radius * radius;
        if (dSquared >= radiusSquared)
            return null;

        double th = Math.sqrt(radiusSquared - dSquared);
        double t1 = Util.alignZero(tm - th);
        double t2 = Util.alignZero(tm + th);

        // Compute intersection points based on t values
        Point p1 = null, p2 = null;
        if (t1 > 0)
            p1 = ray.getPoint(t1);
        if (t2 > 0)
            p2 = ray.getPoint(t2);

        // Return appropriate results based on how many valid intersection points found
        if (p1 != null && p2 != null)
            return List.of(new Intersection(this, p1), new Intersection(this, p2));
        if (p1 != null)
            return List.of(new Intersection(this, p1));
        if (p2 != null)
            return List.of(new Intersection(this, p2));

        return null;
    }
}
