package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Point class.
 */
class PointTest {
    private final double DELTA = 0.00001;
    private final Vector v1 = new Vector(1, 2, 3);
    private final Point p1 = new Point(1, 2, 3);

    /**
     * A small delta value for comparing floating-point numbers.
     */

    @Test

    void testSubtract() {

        // ============ Equivalence Partitions Tests ==============

        // TC01: Subtract two points

        assertEquals(v1, new Point(2, 4, 6).subtract(p1), "Subtract two points does not work correctly");



        // ============ Boundary Values Tests ==================

        // TC02: Subtract equal points

        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "Subtract equal points does not work correctly");

    }

    @Test
    void add() {

        // ============ Equivalence Partitions Tests ==============
        // TC01: Add a vector to a point
        assertEquals(new Point(2, 4, 6), p1.add(v1), "Add a vector to a point does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Add a vector to a point
        assertEquals(Point.ZERO, p1.add(new Vector(-1, -2, -3)), "Add a vector to a point does not work correctly");
    }

    /**
     * Test the distanceSquared method of the Point class.
     */
    @Test
    public void testDistanceSquared() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 5);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Calculate squared distance between two different points
        assertEquals(9, p1.distanceSquared(p2), DELTA,
                "ERROR: squared distance between points is wrong");
        assertEquals(9, p2.distanceSquared(p1), DELTA,
                "ERROR: squared distance between points is wrong");

        // =============== Boundary Values Tests ==================

        // TC10: Calculate squared distance between a point and itself
        assertEquals(0, p1.distanceSquared(p1), DELTA,
                "ERROR: point squared distance to itself is not zero");
    }

    /**
     * Test the distance method of the Point class.
     */
    @Test
    public void testDistance() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 5);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Calculate distance between two different points
        assertEquals(3, p1.distance(p2), DELTA,
                "ERROR: distance between points is wrong");
        assertEquals(3, p2.distance(p1), DELTA,
                "ERROR: distance between points is wrong");

        // =============== Boundary Values Tests ==================

        // TC10: Calculate distance between a point and itself
        assertEquals(0, p1.distance(p1), DELTA,
                "ERROR: point distance to itself is not zero");
    }
}
