package geometries;

import primitives.Point;
import primitives.Vector;

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
}
