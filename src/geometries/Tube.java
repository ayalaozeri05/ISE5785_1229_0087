package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Class representing a tube in 3D space.
 */
public class Tube extends RadialGeometry {
    private final Ray axisRay;

    /**
     * Constructor for a tube.
     *
     * @param axisRay the central axis ray of the tube
     * @param radius  the radius of the tube
     */
    public Tube(Ray axisRay, double radius) {
        super(radius);
        this.axisRay = axisRay;
    }

    /**
     *  Calculates the normal vector to the tube at a given point on its surface.
     * @param point the point on the geometry surface where the normal is to be computed
     * @return the normal vector to the surface at the given point (currently returns null)
     */
    public Vector getNormal(Point point) {
        Point p0 = axisRay.getOrigin();
        Vector dir = axisRay.getDirection();

        double t = dir.dotProduct(point.subtract(p0));
        Point point1 = p0.add(dir.scale(t));

        return point.subtract(point1).normalize();
    }
}
