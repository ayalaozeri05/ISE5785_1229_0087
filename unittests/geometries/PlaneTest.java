package geometries;

import primitives.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Plane class.
 */
class PlaneTest {
    /**
     * A small delta value for comparing floating-point numbers.
     */
    private final double DELTA = 0.000001;

    /**
     * Test case for constructing a plane using three points.
     */
    @Test
    public void testConstructor() {
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


    }
