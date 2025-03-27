package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Point class.
 */
class PointTest {

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

    @Test
    void distanceSquared() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Squared distance between two distinct points
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 2, 3);
        double expected = 14; // 1^2 + 2^2 + 3^2
        assertEquals(expected, p1.distanceSquared(p2), "Squared distance is incorrect");

        // =============== Boundary Values Tests ==================

        // TC11: Squared distance between a point and itself (should be 0)
        assertEquals(0, p1.distanceSquared(p1), "Squared distance between the same point is not zero");

        // TC12: Very large squared distance between two points
        Point p3 = new Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        Point p4 = new Point(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
        double expectedLarge = Math.pow(2 * Double.MAX_VALUE, 2) * 3;
        assertEquals(expectedLarge, p3.distanceSquared(p4), "Squared distance with large values failed");
    }

    @Test
    void distance() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Distance between two distinct points
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 2, 3);
        double expected = Math.sqrt(14); // sqrt(1^2 + 2^2 + 3^2)
        assertEquals(expected, p1.distance(p2), "Distance is incorrect");

        // =============== Boundary Values Tests ==================

        // TC11: Distance between a point and itself (should be 0)
        assertEquals(0, p1.distance(p1), "Distance between the same point is not zero");

        // TC12: Very large distance between two points
        Point p3 = new Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        Point p4 = new Point(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
        double expectedLarge = Math.sqrt(Math.pow(2 * Double.MAX_VALUE, 2) * 3);
        assertEquals(expectedLarge, p3.distance(p4), "Distance with large values failed");
    }
}