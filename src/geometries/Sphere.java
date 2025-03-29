package geometries;

import primitives.Point;
import primitives.Vector;


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
}
