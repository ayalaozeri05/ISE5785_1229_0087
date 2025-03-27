package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Vector class.
 */
class VectorTest {

    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Adding two vectors
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 5, 6);
        Vector expected = new Vector(5, 7, 9);
        assertEquals(expected, v1.add(v2), "Addition of vectors failed");

        // =============== Boundary Values Tests ==================

        // TC11: Adding a zero vector
        Vector zero = new Vector(0, 0, 0);
        assertEquals(v1, v1.add(zero), "Adding a zero vector failed");
    }

    @Test
    void scale() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Scaling a vector by a positive scalar
        Vector v = new Vector(1, 2, 3);
        double scalar = 2.0;
        Vector expected = new Vector(2, 4, 6);
        assertEquals(expected, v.scale(scalar), "Scaling by positive scalar failed");

        // =============== Boundary Values Tests ==================

        // TC11: Scaling a vector by zero (should return a zero vector)
        Vector zeroExpected = new Vector(0, 0, 0);
        assertEquals(zeroExpected, v.scale(0), "Scaling by zero failed");

        // TC12: Scaling a vector by a negative scalar
        double negativeScalar = -2.0;
        Vector expectedNegative = new Vector(-2, -4, -6);
        assertEquals(expectedNegative, v.scale(negativeScalar), "Scaling by negative scalar failed");
    }

    @Test
    void dotProduct() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Dot product of two vectors
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, -5, 6);
        double expected = 4 + 2 * -5 + 3 * 6; // 4 - 10 + 18 = 12// assertEquals(expected, v1.dotProduct(v2), "Dot product calculation failed");

        // =============== Boundary Values Tests ==================

        // TC11: Dot product with a zero vector (should be 0)
        Vector zero = new Vector(0, 0, 0);
        assertEquals(0, v1.dotProduct(zero), "Dot product with zero vector failed");
    }

    @Test
    void crossProduct() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Cross product of two orthogonal vectors
        Vector v1 = new Vector(1, 0, 0);
        Vector v2 = new Vector(0, 1, 0);
        Vector expected = new Vector(0, 0, 1);
        assertEquals(expected, v1.crossProduct(v2), "Cross product of orthogonal vectors failed");

        // =============== Boundary Values Tests ==================

        // TC11: Cross product of parallel vectors (should throw exception)
        Vector v3 = new Vector(2, 2, 2);
        Vector v4 = new Vector(-4, -4, -4);
        assertThrows(IllegalArgumentException.class, () -> v3.crossProduct(v4), "Cross product of parallel vectors did not throw an exception");
    }

    @Test
    void lengthSquared() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Length squared of a vector
        Vector v = new Vector(1, 2, 2);
        double expected =  1 + 2 * 2 + 2 * 2; // 1 + 4 + 4 = 9
        assertEquals(expected, v.lengthSquared(), "Length squared calculation failed");
    }

    @Test
    void length() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Length of a vector
        Vector v = new Vector(1, 2, 2);
        double expected = Math.sqrt(9); // sqrt(1 + 4 + 4) = 3
        assertEquals(expected, v.length(), "Length calculation failed");
    }

    @Test
    void normalize() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Normalizing a non-zero vector
        Vector v = new Vector(0, 3, 4);
        Vector expected = new Vector(0, 0.6, 0.8);
        assertEquals(expected, v.normalize(), "Normalization of non-zero vector failed");

        // =============== Boundary Values Tests ==================

        // TC11: Normalizing a zero vector (should throw exception)
        Vector zeroVector = new Vector(0, 0, 0);
        assertThrows(IllegalArgumentException.class, zeroVector::normalize, "Normalization of zero vector did not throw an exception");
    }
}