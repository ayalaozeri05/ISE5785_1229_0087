package geometries;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;
import java.util.ArrayList;

import static primitives.Util.alignZero;
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
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Point> intersections = plane.findIntersections(ray);
        // the ray is not intersecting the plane
        if (intersections == null)
            return null;
        // the ray is intersecting the plane
        Point head = ray.getOrigin();//the start ray point
        Vector dir = ray.getDirection();

        Vector v1 = vertices.get(0).subtract(head);
        Vector v2 = vertices.get(1).subtract(head);
        double s1 = alignZero(dir.dotProduct(v1.crossProduct(v2)));
        //checks the point is on the 1st edge
        if (s1 == 0) return null;

        Vector v3 = vertices.get(2).subtract(head);
        double s2 = alignZero(dir.dotProduct(v2.crossProduct(v3)));
        //checks the point is out of triangle or on the 2nd edge
        if (s1 * s2 <= 0) return null;

        double s3 = alignZero(dir.dotProduct(v3.crossProduct(v1)));
        //checks the point is out of triangle or on the 3rd edge
        if (s1 * s3 <= 0) return null;

        return List.of(new Intersection(this, intersections.getFirst()));
    }
}