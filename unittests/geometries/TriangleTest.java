package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


/**
 * Unit tests for the Triangle class.
 */
class TriangleTest {
    private final Vector v1 = new Vector(0, 0, -1);
    private final Vector v2 = new Vector(0, 0, 1);
    private final Point p1 = new Point(0, 0, 1);
    private final Point p2 = new Point(0, 1, 0);
    private final Point p3 = new Point(1, 0, 0);
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.00001;

    /**
     * Test case for getting the normal vector of a triangle.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test the normal vector calculation of a triangle defined by three points
        Triangle triangle = new Triangle(new Point(0, 0, 0), new Point(1, 0, 0),
                new Point(0, 1, 0));
        Vector result = triangle.getNormal(new Point(0, 0, 0));
        // Test that the length of the normal is 1
        assertEquals(1, result.length(), DELTA, "ERROR: the length of the normal is not 1");
        // Check that the normal vector is orthogonal to the plane
        assertEquals(0.0, result.dotProduct(new Vector(1, 0, 0)), DELTA,
                "ERROR: the normal vector is not orthogonal to the vector (1, 0, 0)");
        assertEquals(0.0, result.dotProduct(new Vector(0, 1, 0)), DELTA,
                "ERROR: the normal vector is not orthogonal to the vector (0, 1, 0)");
        // Check that the normal vector is as expected
        assertEquals(new Vector(0, 0, 1), result,
                "getNormal() did not return the expected normal vector.");
    }
    /**
     * Test method for {@link geometries.Triangle#Triangle(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(new Point(1, 1, 0), p3, p2);
        // ============ Equivalence Partitions Tests ==============
        // TC01: the intersection point is inside the triangle
        assertEquals(1, triangle.findIntersections(
                        new Ray(new Point(1.8, 1.8, 1), new Vector(-1, -1, -1))).size(),
                "Failed to find the intersection point when the intersection point is inside the triangle");

        // TC02: the intersection point is outside the triangle and against an edge
        assertNull(triangle.findIntersections(
                        new Ray(new Point(0.5, 2, 1), v1)),
                "Failed to find the intersection point when the intersection point is outside the triangle and against an edge");

        // TC03: the intersection point is outside the triangle and against a vertex
        assertNull(triangle.findIntersections(
                        new Ray(new Point(2, 2, 1), v1)),
                "Failed to find the intersection point when the intersection point is outside the triangle and against an edge");

        // ================= Boundary Values Tests =================
        // TC04: the intersection point is on the edge of the triangle
        assertNull(triangle.findIntersections(
                        new Ray(new Point(0.5, 1, -1), v2)),
                "Failed to find the intersection point when the intersection point is on the edge of the triangle");

        // TC05: the intersection point is on the vertex of the triangle
        assertNull(triangle.findIntersections(
                        new Ray(new Point(1, 1, 1), new Vector(0, 0, -1))),
                "Failed to find the intersection point when the intersection point is on the vertex of the triangle");

        // TC06: the intersection point is outside the triangle but in the path of the edge
        assertNull(triangle.findIntersections(
                        new Ray(new Point(2, 1, -1), v2)),
                "Failed to find the intersection point when the intersection point is outside the triangle but in the path of the edge");

        // ================= external Tests =================
        // TC07: the triangle is in an angle
        Triangle triangle2 = new Triangle(p3, p2, p1);
        assertEquals(1, triangle2.findIntersections(
                        new Ray(new Point(-1, -1, -1), new Vector(1, 1, 1))).size(),
                "Failed to find the intersection point when the intersection point is inside the triangle");
    }
}