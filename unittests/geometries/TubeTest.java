package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Tube class
 */
class TubeTests {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;
    private final Ray r1 = new Ray(Point.ZERO, new Vector(1, 0, 0));
    private final Tube tube = new Tube(new Ray(Point.ZERO, new Vector(0, 0, 1)), 1);

    /**
     * Test method for {@link geometries.Tube#Tube(primitives.Ray, double)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        assertDoesNotThrow(() -> new Tube(r1, 1), "Failed to create a proper tube");
    }

    @Test
    void testGetNormal() {
        // TC01: Point on the side surface of the tube
        Tube tube = new Tube( new Ray( new Point(0, 0, 0),new Vector(0, 0, 1)),1);
        Vector expected = new Vector(1, 0, 0);
        Vector normal = tube.getNormal(new Point(1, 0, 5));
        // Check that the normal is a unit vector
        assertEquals(1, normal.length(), 1e-10, "ERROR: Normal is not unit length");
        // Check that the normal vector is as expected
        assertEquals(expected, normal, "ERROR: Wrong normal on tube surface");
    }
}


