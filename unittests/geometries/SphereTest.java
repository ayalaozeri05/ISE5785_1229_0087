package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the Sphere class.
 */
class SphereTest {
    //The value 0.75 comes from the geometric relationship between the ray and sphere, calculated during intersection in 3D space.
    private final double sqrt075 = Math.sqrt(0.75);

    private final Vector v1 = new Vector(1, 0, 1);
    private final Vector v2 = new Vector(0, -1, 0);
    private final Vector v3 = new Vector(0, 1, 0);
    private final Vector v4 = new Vector(1, 1, 1);
    private final Point p2 = new Point(0, 1, 1);
    private final Point p3 = new Point(0, 2, 1);
    private final Point p4 = new Point(0, -1, 1);
    private final Point p5 = new Point(0, 0.5, 1);
    private final Point p7 = new Point(0, sqrt075, 1.5);
    private final Point p8 = new Point(0, 0, 1);
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

    @Test
    void testFindIntersections() {
        Sphere sphere = new Sphere( p8,1);

        // ============ Equivalence Partitions Tests ==============

        // **** Group 1: The ray starts inside the sphere
        // TC01: The ray starts inside the sphere
        assertEquals(
                List.of(p7),
                sphere.findIntersections(new Ray(new Point(0, 0, 1.5), v3)),
                "Failed to find the intersection point when the ray starts inside the sphere"
        );

        // **** Group 2: The ray never intersects the sphere
        // TC02: The ray never intersects the sphere
        assertNull(
                sphere.findIntersections(new Ray(new Point(0, 0, 3), v4)),
                "Failed to find the intersection point when the ray never intersects the sphere"
        );

        // **** Group 3: The ray starts outside the sphere and intersects twice
        // TC03: The ray starts outside and intersects twice
        final var result1 = sphere.findIntersections(new Ray(new Point(0, 2, 1.5), v2));
        assertNotNull(result1, "Can't be empty list");
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(
                List.of(p7, new Point(0, -sqrt075, 1.5)),
                result1,
                "Failed to find the intersection points when the ray starts outside the sphere and intersects twice"
        );

        // **** Group 4: The ray starts outside but does not intersect
        // TC04: The ray starts outside and does not intersect
        assertNull(
                sphere.findIntersections(new Ray(new Point(0, -2, 1.5), v2)),
                "Failed to find the intersection points when the ray starts outside and does not intersect the sphere"
        );

        // =============== Boundary Values Tests ==================

        // **** Group 1: Orthogonal rays
        // TC05: Orthogonal ray starts before the sphere
        assertNull(
                sphere.findIntersections(new Ray(p3, new Vector(0, 0, 1))),
                "Failed to find the intersection point when the ray never intersects the sphere"
        );
        // TC06: Orthogonal ray starts inside the sphere
        assertEquals(
                List.of(new Point(0, 0.5, 1 - sqrt075)),
                sphere.findIntersections(new Ray(p5, new Vector(0, 0, -1))),
                "Failed to find the intersection point when the ray starts inside the sphere"
        );

        // **** Group 2: Tangential rays
        // TC07: Tangential ray starts before the sphere
        assertNull(
                sphere.findIntersections(new Ray(new Point(-1, 1, 0), v1)),
                "Failed to find the intersection point when the ray never intersects the sphere"
        );
        // TC08: Tangential ray starts on the sphere
        assertNull(
                sphere.findIntersections(new Ray(p2, v1)),
                "Failed to find the intersection point when the ray never intersects the sphere"
        );
        // TC09: Tangential ray starts after the sphere
        assertNull(
                sphere.findIntersections(new Ray(new Point(1, 1, 2), v1)),
                "Failed to find the intersection point when the ray never intersects the sphere"
        );

        // **** Group 3: Rays not orthogonal nor tangential
        // TC10: Ray starts on the sphere and intersects the sphere
        assertEquals(
                List.of(new Point(-2.0 / 3, 1.0 / 3, 1.0 / 3)),
                sphere.findIntersections(new Ray(p2, new Vector(-1, -1, -1))),
                "Failed to find the intersection point when the ray starts on the sphere and intersects it"
        );
        // TC11: Ray starts on the sphere but does not intersect
        assertNull(
                sphere.findIntersections(new Ray(p2, v4)),
                "Failed to find the intersection point when the ray starts on the sphere and doesn't intersect"
        );

        // **** Group 4: Rays reaching the middle of the sphere
        // TC12: Ray starts on the sphere and reaches the middle
        assertEquals(
                List.of(p4),
                sphere.findIntersections(new Ray(p2, v2)),
                "Failed to find the intersection point when the ray starts on the sphere and reaches the middle"
        );
        // TC13: Ray starts before the sphere and reaches the middle
        final var result2 = sphere.findIntersections(new Ray(p3, v2));
        assertNotNull(result2, "Can't be empty list");
        assertEquals(2, result2.size(), "Wrong number of points");
        assertEquals(
                List.of(p2, p4).stream().sorted(Comparator.comparingDouble(p -> p.distance(new Point(-1, 0, 0)))).toList(),
                result2,
                "Failed to find the intersection points when the ray starts before the sphere and reaches the middle"
        );
        // TC14: Ray starts in the middle of the sphere
        assertEquals(
                List.of(p2),
                sphere.findIntersections(new Ray(p8, v3)),
                "Failed to find the intersection point when the ray starts in the middle of the sphere"
        );
        // TC15: Ray starts on the sphere but in the opposite direction
        assertNull(
                sphere.findIntersections(new Ray(p2, v3)),
                "Failed to find the intersection point when the ray starts on the sphere but doesn't reach the middle"
        );
        // TC16: Ray starts after the sphere, opposite direction
        assertNull(
                sphere.findIntersections(new Ray(p3, v3)),
                "Failed to find the intersection point when the ray starts after the sphere and doesn't reach the middle"
        );
        // TC17: Ray starts inside the sphere, opposite direction
        assertEquals(
                List.of(p2),
                sphere.findIntersections(new Ray(p5, v3)),
                "Failed to find the intersection point when the ray starts inside the sphere but doesn't reach the middle"
        );
    }

    }










