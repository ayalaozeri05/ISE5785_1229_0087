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

    @Override
    public Vector getNormal(Point point)
    {
double t =axisRay.

    }
}
