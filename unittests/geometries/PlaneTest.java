package geometries;

import primitives.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Plane class.
 */
class PlaneTest {
    private final Point p1 = new Point(0, 1, 1);
    private final Point p3 = new Point(0, 0, 1);

    /**
     * A small delta value for comparing floating-point numbers.
     */
    private final double DELTA = 0.000001;

    /**
     * Test case for constructing a plane using three points.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        assertDoesNotThrow(() -> new Plane(p1, new Point(0, 1, 0), p3), "Failed to create a proper plane");


        // ============ Boundary Values Tests ==================

        // TC10: Two identical points (first and second)
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(1, 1, 1),
                        new Point(1, 1, 1), new Point(0, 0, 0)),
                "Plane constructor does not throw an exception for identical points (first and second)");

        // TC11: Two identical points (first and third)
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(1, 1, 1),
                        new Point(0, 0, 0), new Point(1, 1, 1)),
                "Plane constructor does not throw an exception for identical points (first and third)");

        // TC12: Two identical points (second and third)
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(1, 1, 1),
                        new Point(0, 0, 0), new Point(0, 0, 0)),
                "Plane constructor does not throw an exception for identical points (second and third)");

        // TC13: All three points are identical
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(1, 1, 1),
                        new Point(1, 1, 1), new Point(1, 1, 1)),
                "Plane constructor does not throw an exception when all points are identical");

        // TC14: Three collinear points (but not identical)
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(1, 1, 1),
                        new Point(2, 2, 2), new Point(3, 3, 3)),
                "Plane constructor does not throw an exception for collinear points");
    }


    /**
     * Test case for getNormal() method of Plane class.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test the normal vector calculation of a plane defined by three points
        Plane p = new Plane(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0));
        Vector result = p.getNormal(new Point(0, 0, 1));
        // Test that the length of the normal is 1
        assertEquals(1, result.length(), DELTA, "ERROR: the length of the normal is not 1");
        // Check that the normal vector is orthogonal to the plane
        assertEquals(0.0, result.dotProduct(new Vector(0, -1, 1)), DELTA, "ERROR: the normal vector is not orthogonal to the vector (0, -1, 1)");
        assertEquals(0.0, result.dotProduct(new Vector(-1, 1, 0)), DELTA, "ERROR: the normal vector is not orthogonal to the vector (-1, 1, 0)");
    }
    @Test
    void testFindIntersections() {
        Plane pl1 = new Plane(new Point(0, 0, 0), new Vector(1, 0, 0));
        Vector vRegular = new Vector(1, 0, 1);
        Vector vParallel = new Vector(0, 0, 1);
        Vector vPerpendicular = new Vector(1, 0, 0);

        // ============ Equivalence Partitions Tests ==============

        // **** Group 1: Regular vector (not parallel and not perpendicular) intersecting the plane
        // TC01: Intersecting the plane
        assertEquals(
                List.of(new Point(0, 0, 1)),
                pl1.findIntersections(new Ray(new Point(-1, 0, 0), vRegular)),
                "ERROR: findIntersections when a regular vector (not parallel not perpendicular) is intersecting the plane"
        );
        // TC02: Not intersecting the plane
        assertEquals(
                null,
                pl1.findIntersections(new Ray(new Point(3, 2, 4), vRegular)),
                "ERROR: findIntersections when a regular vector (not parallel not perpendicular) isn't intersecting the plane"
        );

        // =============== Boundary Values Tests ==================

        // **** Group 2: Ray is parallel to the plane
        // TC03: Parallel and outside the plane
        assertEquals(null,
                pl1.findIntersections(new Ray(new Point(1, 0, 0), vParallel)),
                "ERROR: No intersection expected when the ray is parallel to the plane and outside it (TC03)"
        );
        // TC04: Parallel and inside the plane
        assertEquals(
                null,
                pl1.findIntersections(new Ray(new Point(0, 0, 0), vParallel)),
                "ERROR: No intersection expected when the ray is parallel to the plane and inside it (TC04)"
        );

        // **** Group 3: Ray is perpendicular to the plane
        // TC05: Starts before the plane
        assertEquals(
                List.of(new Point(0, 0, 0)),
                pl1.findIntersections(new Ray(new Point(-1, 0, 0), vPerpendicular)),
                "ERROR: Expected intersection point when the ray is perpendicular to the plane and starts before it (TC05)"
        );
        // TC06: Starts inside the plane
        assertEquals(
                null,
                pl1.findIntersections(new Ray(new Point(0, 0, 0), vPerpendicular)),
                "ERROR: No intersection expected when the ray is perpendicular to the plane and starts inside it (TC06)"
        );
        // TC07: Starts after the plane
        assertEquals(
                null,
                pl1.findIntersections(new Ray(new Point(1, 0, 0), vPerpendicular)),
                "ERROR: No intersection expected when the ray is perpendicular to the plane and starts after it (TC07)"
        );

        // **** Group 4: Special cases with vectors starting on the plane
        // TC08: Regular vector starts on the plane
        assertEquals(
                null,
                pl1.findIntersections(new Ray(new Point(0, 3, 5), vRegular)),
                "ERROR: findIntersections when a regular vector (not parallel not perpendicular) starts on the plane"
        );
        // TC09: Regular vector starts at the point stored in the plane
        assertEquals(
                null,
                pl1.findIntersections(new Ray(new Point(0, 0, 0), vRegular)),
                "ERROR: findIntersections when a regular vector (not parallel not perpendicular) starts on the plane on the point that stored in the plane"
        );
    }
    }
