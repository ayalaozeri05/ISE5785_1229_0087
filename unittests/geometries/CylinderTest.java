package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Cylinder class
 */
class CylinderTests {

    private final Vector v1 = new Vector(0, 0, -1);
    private final Vector v2 = new Vector(0, 0, 1);
    private final Cylinder cylinder = new Cylinder(new Ray(Point.ZERO, v2), 1, 1);

    /**
     * Test method for {@link geometries.Cylinder#Cylinder(primitives.Ray, double, double)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        assertDoesNotThrow(() -> new Cylinder(new Ray(Point.ZERO, v2), 1, 1));
    }

    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {

    }



}