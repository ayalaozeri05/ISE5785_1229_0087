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

    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     */

    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test normal for a point on the outer surface of the tube
        assertEquals(new Vector(1, 0, 0), tube.getNormal(new Point(1, 0, 1)), "Bad normal to tube");

        // =============== Boundary Values Tests =================

        // Additional Case for Boundary
        // TC02: Test normal for a point that creates a right angle with the axis of the tube
        Point boundaryPoint = new Point(0, 1, 0); // Example point "in front of the head of the ray"
        assertEquals(new Vector(0, 1, 0), tube.getNormal(boundaryPoint), "Bad normal for boundary point");
    }

}
