package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Plane class.
 */
class PlaneTest {

    @Test
    void constructorThreePoints() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Normal vector is orthogonal to two different vectors between points and has length 1
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane plane = new Plane(p1, p2, p3);

        Vector v1 = p2.subtract(p1); // Vector between p1 and p2
        Vector v2 = p3.subtract(p1); // Vector between p1 and p3
        Vector normal = plane.getNormal();

        // Check that the normal vector is orthogonal to v1 and v2
        assertEquals(0, normal.dotProduct(v1), "Normal is not orthogonal to the first vector");
        assertEquals(0, normal.dotProduct(v2), "Normal is not orthogonal to the second vector");

        // Check that the normal vector has length 1
        assertEquals(1, normal.length(), "Normal vector is not of length 1");

        // =============== Boundary Values Tests ==================

        // TC11: First and second points are identical
        Point identicalP1P2 = new Point(0, 0, 0);
        assertThrows(IllegalArgumentException.class, () -> new Plane(identicalP1P2, identicalP1P2, p3),
                "Constructor did not throw an exception for identical first and second points");

        // TC12: First and third points are identical
        Point identicalP1P3 = new Point(0, 0, 0);
        assertThrows(IllegalArgumentException.class, () -> new Plane(identicalP1P3, p2, identicalP1P3),
                "Constructor did not throw an exception for identical first and third points");

        // TC13: Second and third points are identical
        Point identicalP2P3 = new Point(1, 0, 0);
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, identicalP2P3, identicalP2P3),
                "Constructor did not throw an exception for identical second and third points");

        // TC14: All three points are identical
        Point identicalAll = new Point(1, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> new Plane(identicalAll, identicalAll, identicalAll),
                "Constructor did not throw an exception for all identical points");

        // TC15: All three points are collinear (but not identical)
        Point collinearP1 = new Point(0, 0, 0);
        Point collinearP2 = new Point(1, 1, 1);
        Point collinearP3 = new Point(2, 2, 2);
        assertThrows(IllegalArgumentException.class, () -> new Plane(collinearP1, collinearP2, collinearP3),
                "Constructor did not throw an exception for collinear points");
    }

    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Verify the normal of the plane is correct
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane plane = new Plane(p1, p2, p3);

        Vector normal = plane.getNormal() ;
        Vector expectedNormal = new Vector(0, 0, 1); // Expected normal for these points
        assertEquals(expectedNormal, normal.normalize(), "Normal vector calculation failed");
    }
}