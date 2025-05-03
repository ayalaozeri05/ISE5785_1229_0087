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
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getPoint(0);
        Vector dir = ray.getDirection();

        // if the ray starts at the center of the sphere
        if (center.equals(p0))
            return List.of(p0.add(dir.scale(radius)));

        Vector u = (center.subtract(p0));
        double tm = dir.dotProduct(u);
        double d = Util.alignZero(Math.sqrt(u.lengthSquared() - tm * tm));
        if (d >= radius)
            return null;

        double th = Math.sqrt(radius * radius - d * d);
        double t1 = Util.alignZero(tm - th);
        double t2 = Util.alignZero(tm + th);

        // if the ray starts before the sphere
        if (t1 > 0 && t2 > 0)
            return List.of(p0.add(dir.scale(t1)), p0.add(dir.scale(t2)));

        // if the ray starts inside the sphere
        if (t1 > 0)
            return List.of(p0.add(dir.scale(t1)));
        if (t2 > 0)
            return List.of(p0.add(dir.scale(t2)));

        return null;
    }
}
