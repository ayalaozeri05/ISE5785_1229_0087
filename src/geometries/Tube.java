package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
    public Vector getNormal(Point point) {
        Point p0 = this.axisRay.getPoint(0);

        //calculate the projection of the point on the axis
        double t = this.axisRay.getDirection().dotProduct(point.subtract(p0));
        if (t == 0)//if the vector is orthogonal to the axis
            return point.subtract(p0).normalize();

        //find center of the tube
        //return the normalized vector from the center of the tube to the point
        return point.subtract(this.axisRay.getPoint(t)).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
