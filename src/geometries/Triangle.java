package geometries;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;
import java.util.ArrayList;

import static primitives.Util.isZero;


/**
 * Class representing a triangle.
 */
public class Triangle extends Polygon {
    /**
     * Constructor for a triangle.
     *
     * @param p1 first vertex
     * @param p2 second vertex
     * @param p3 third vertex
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        // Use the old findIntersections method from Plane (which returns List<Point>)
        List<Point> listPoint = plane.findIntersections(ray);

        if (listPoint == null)
            return null;

        // Calculate vectors from ray origin to each vertex
        Vector v1 = vertices.get(0).subtract(ray.getOrigin());
        Vector v2 = vertices.get(1).subtract(ray.getOrigin());
        Vector v3 = vertices.get(2).subtract(ray.getOrigin());

        // Compute normals for the edges
        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        // Compute dot products between normals and ray direction
        double t1 = n1.dotProduct(ray.getDirection());
        double t2 = n2.dotProduct(ray.getDirection());
        double t3 = n3.dotProduct(ray.getDirection());

        // Check if all dot products have the same sign (ray hits inside triangle)
        if (isZero(t1) || isZero(t2) || isZero(t3)) return null;
        boolean sameSign = (t1 > 0 && t2 > 0 && t3 > 0) || (t1 < 0 && t2 < 0 && t3 < 0);

        if (!sameSign) return null;

        // Convert the intersection point to Intersection object
        List<Intersection> result = new ArrayList<>();
        for (Point p : listPoint) {
            result.add(new Intersection(this, p));
        }

        return result;
    }
}