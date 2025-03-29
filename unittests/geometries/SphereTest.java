package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
//import geometries.Intersectable.GeoPoint;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Sphere class.
 */
class SphereTest {


    /**
     * Test case for the getNormal method of Sphere.
     * This test ensures that the normal vector at a point on the sphere's surface is calculated correctly.
     */
    @Test
    public void testGetNormal() {
        // Create a sphere centered at the origin with a radius of 1
        Sphere sphere = new Sphere(new Point(0, 0, 0), 1.0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that the normal at the point is as expected
        assertEquals(new Vector(1, 0, 0).normalize(), sphere.getNormal(new Point(1, 0, 0)), "getNormal() for Sphere did not return the expected normal");
    }


    }










