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

}