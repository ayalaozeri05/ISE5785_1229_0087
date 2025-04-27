package renderer;

import geometries.Geometry;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import renderer.Camera;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests between camera ray construction and geometry intersections.
 */
public class CameraIntersectionsIntegrationTests {

    private final Vector yAxis = new Vector(0, -1, 0);
    private final Vector zAxis = new Vector(0, 0, -1);

    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(zAxis, yAxis)
            .setVpDistance(1)
            .setVpSize(3, 3);

    private final Camera camera = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();

    /**
     * Helper function to count the number of intersections between camera rays and a geometry.
     *
     * @param camera         the camera generating the rays
     * @param geometry       the geometry to test intersections with
     * @param expectedAmount expected number of intersections
     */
    private void countIntersections(Camera camera, Geometry geometry, int expectedAmount) {
        int intersections = 0;
        for (int j = 0; j < 3; j++)
            for (int i = 0; i < 3; i++) {
                List<Point> intersectionsList = geometry.findIntersections(camera.constructRay(3, 3, j, i));
                intersections += intersectionsList != null ? intersectionsList.size() : 0;
            }

        assertEquals(expectedAmount, intersections, "Wrong amount of intersections");
    }

    /**
     * Tests intersection of camera rays with a sphere.
     */
    @Test
    void testSphereIntersection() {
        // TC01: Small sphere in front of the camera (2 intersections)
        countIntersections(cameraBuilder.setLocation(Point.ZERO).build(), new Sphere( new Point(0, 0, -3),1), 2);

        // TC02: Large sphere enclosing the view plane (18 intersections)
        countIntersections(camera, new Sphere( new Point(0, 0, -2.5) ,2.5), 18);

        // TC03: Medium sphere (10 intersections)
        countIntersections(camera, new Sphere( new Point(0, 0, -2),2), 10);

        // TC04: Even larger sphere positioned closer (9 intersections)
        countIntersections(camera, new Sphere( new Point(0, 0, -1),4), 9);

        // TC05: Tiny sphere behind the camera (0 intersections)
        countIntersections(camera, new Sphere( new Point(0, 0, 1),0.5), 0);
    }

    /**
     * Tests intersection of camera rays with a plane.
     */
    @Test
    void testPlaneIntersection() {
        // TC01: Parallel plane in front of the camera (9 intersections)
        countIntersections(camera, new geometries.Plane(new Point(0, 0, -1), new Vector(0, 0, -1)), 9);

        // TC02: Slightly tilted plane facing upwards (9 intersections)
        countIntersections(camera, new geometries.Plane(new Point(0, 0, -1), new Vector(0, 1, -10)), 9);

        // TC03: Inclined plane (6 intersections)
        countIntersections(camera, new geometries.Plane(new Point(0, 0, -1), new Vector(0, -1, -1)), 6);
    }

    /**
     * Tests intersection of camera rays with a triangle.
     */
    @Test
    void testTriangleIntersection() {
        // TC01: Small triangle centered in view (1 intersection)
        countIntersections(camera, new geometries.Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 1);

        // TC02: Large triangle extending beyond the view plane (2 intersections)
        countIntersections(camera, new geometries.Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 2);
    }
}