package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Point class.
 */
class PointTest {
    /**
     * A small delta value for comparing floating-point numbers.
     */
    private static final double DELTA = 0.00001;

    @Test
    void subtract() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Subtracting two distinct points
        Point p1 = new Point(4, 5, 6);
        Point p2 = new Point(1, 2, 3);
        Vector expected = new Vector(3, 3, 3);
        assertEquals(expected, p1.subtract(p2), "Subtraction result is incorrect");

        // =============== Boundary Values Tests ==================

        // TC11: Subtracting a point from itself (should return zero vector)
        assertEquals(new Vector(0, 0, 0), p1.subtract(p1), "Subtraction of a point from itself failed");

        // TC12: Subtracting a point with very large values from another point
        Point p3 = new Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        Vector expectedLarge = new Vector(Double.MAX_VALUE - 4, Double.MAX_VALUE - 5, Double.MAX_VALUE - 6);
        assertEquals(expectedLarge, p3.subtract(p1), "Subtraction with large values failed");
    }

    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Adding a vector to a point
        Point p = new Point(1, 2, 3);
        Vector v = new Vector(4, 5, 6);
        Point expected = new Point(5, 7, 9);
        assertEquals(expected, p.add(v), "Addition of vector to point failed");

        // =============== Boundary Values Tests ==================

        // TC11: Adding a zero vector (result should be the same point)
        Vector zeroVector = new Vector(0, 0, 0);
        assertEquals(p, p.add(zeroVector), "Adding zero vector did not return the same point");

        // TC12: Adding a vector with very large values
        Vector largeVector = new Vector(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        Point expectedLarge = new Point(Double.MAX_VALUE + 1, Double.MAX_VALUE + 2, Double.MAX_VALUE + 3);
        assertEquals(expectedLarge, p.add(largeVector), "Adding large vector failed");
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
