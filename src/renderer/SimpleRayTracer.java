package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable. Intersection;

import java.util.List;

/**
 * SimpleRayTracer class is a basic implementation of a ray tracer
 */
public class SimpleRayTracer extends RayTracerBase {
    /**
     * Constructor to initialize the scene
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        // Use the new method that returns a list of Intersection objects
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        if (intersections == null) return scene.background;

        // Find the closest intersection to the ray origin
        Intersection closest = ray.findClosestIntersection(intersections);
        return calcColor(closest);
    }

    /**
     * Calculate the color at an intersection point,
     * combining ambient light and the geometry's emission color
     * @param intersection the intersection point and the intersected geometry
     * @return the resulting color
     */
    private Color calcColor(Intersection intersection) {
        return scene.ambientLight.getIntensity()
                .add(intersection.geometry.getEmission());
    }
}


