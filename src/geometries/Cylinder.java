package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;


/**
 * Class representing a cylinder in 3D space.
 */
public class Cylinder extends Tube {
    private final double height;

    /**
     * Constructor for a cylinder.
     *
     *
     * @param axisRay the central axis ray of the cylinder
     * @param radius  the radius of the cylinder
     * @param height  the height of the cylinder
     */
    public Cylinder(Ray axisRay, double radius, double height) {
        super(axisRay, radius);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        return super.getNormal(point);
    }


}
