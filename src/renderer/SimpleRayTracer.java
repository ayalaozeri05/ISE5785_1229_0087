package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;
//

/**
 * SimpleRayTracer is a basic implementation of a ray tracer that extends the
 * RayTracerBase class. It is responsible for tracing rays in a scene and
 * calculating the color at the intersection points.
 *
 * @author Odeya and Atara
 */
public class SimpleRayTracer extends RayTracerBase {
    private static final double DELTA = 0.1;



    /**
     * Constructor for SimpleRayTracer.
     *
     * @param scene The scene to be rendered.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        var intersections = scene.geometries.calculateIntersections(ray);
        // If no intersections found, return the background color of the scene
        return intersections == null ? scene.background //
                : calcColor(ray.findClosestIntersection(intersections), ray);
    }

    /**
     * Calculates the color at a given point.
     * In this simple ray tracer, it only returns the ambient light intensity.
     *
     * @param intersection The point at which to calculate the color.
     * @return The color at the given point.
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection())) return Color.BLACK;
        Color ambient = scene.ambientLight.getIntensity().scale(intersection.material.kA);
        Color localEffects = calcColorLocalEffects(intersection);
        return ambient.add(localEffects);
    }

    /**
     * Preprocesses the intersection by computing the normal at the intersection point,
     * the direction vector from the ray, and the dot product between them.
     * This helps determine if the intersection is valid for further shading calculations.
     *
     * @param intersection the intersection to preprocess
     * @param v            the direction vector (usually the ray direction)
     * @return true if the dot product between the direction and normal is non-zero; false otherwise
     */
    private boolean preprocessIntersection(Intersection intersection, Vector v) {
        intersection.v = v;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = alignZero(intersection.v.dotProduct(intersection.normal));
        return intersection.vNormal != 0;
    }

    /**
     * Sets the light source for the given intersection and computes the light direction vector (l),
     * as well as the dot product between the light direction and the surface normal at the intersection point.
     * This is used to determine if the light contributes to the shading at that point.
     *
     * @param intersection the intersection where the light is being set
     * @param light        the light source affecting the intersection
     * @return true if the squared dot product between light direction and normal is positive, false otherwise
     */
    private boolean setLightSource(Intersection intersection, LightSource light) {
        intersection.light = light;
        intersection.l = light.getL(intersection.point);
        intersection.lNormal = alignZero(intersection.l.dotProduct(intersection.normal));
        return (intersection.lNormal * intersection.lNormal) > 0;
    }

    /**
     * Calculates the local lighting effects (diffuse and specular) at the intersection point.
     * The result includes contributions from all light sources in the scene.
     * Each light's effect is computed only if it contributes to the shading (i.e., its direction
     * is not perpendicular to the surface normal).
     *
     * The calculation includes:
     * - Emission color of the intersected geometry
     * - Diffuse reflection based on Lambert's cosine law
     * - Specular reflection based on the Phong reflection model
     *
     * @param intersection the intersection information including point, geometry, normal, etc.
     * @return the resulting color after applying all local lighting effects
     */
    private Color calcColorLocalEffects(Intersection intersection) {
        // Start with the emission color of the geometry
        Color color = intersection.geometry.getEmission();

        for (LightSource light : scene.lights) {
            // Update light source data in the intersection object
            if (!setLightSource(intersection, light)) {
                continue; // Skip to the next light source if setLightSource returns false

            }
            if (!unshaded(intersection)) continue;
            // Calculate the light intensity at the intersection point
            Color lightIntensity = light.getIntensity(intersection.point);

            // Add the contribution of the light source to the color
            Double3 diffusive = calcDiffusive(intersection);
            Double3 specular = calcSpecular(intersection);
            color = color.add(lightIntensity.scale(diffusive.add(specular)));
        }

        return color;
    }

    /**
     * Calculates the specular reflection component at the intersection point using the Phong model.
     * The specular component simulates the bright spot of light that appears on shiny surfaces.
     *
     * The calculation is based on the angle between the view vector and the reflection direction.
     * If the view vector and reflection direction form an angle greater than 90 degrees,
     * there is no specular contribution.
     *
     * @param intersection the intersection containing light direction, normal, material properties, and view vector
     * @return the specular reflection coefficient as a Double3 (RGB intensity multiplier)
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.l.subtract(intersection.normal.scale(2d * intersection.lNormal));
        double vr = alignZero(intersection.v.dotProduct(r));
        if (vr >= 0d) return Double3.ZERO;
        return intersection.material.kS.scale(Math.pow(-vr, intersection.material.nsh));
    }

    /**
     * Calculates the diffusive reflection component at the intersection point using the Lambertian model.
     * The diffusive component represents the scattered light from a rough surface,
     * which depends on the angle between the light direction and the surface normal.
     *
     * The intensity is proportional to the cosine of the angle between the light direction and the normal,
     * which is stored in the intersection's lNormal field.
     *
     * @param intersection the intersection containing the light angle and material properties
     * @return the diffusive reflection coefficient as a Double3 (RGB intensity multiplier)
     */
    private Double3 calcDiffusive(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.lNormal));
    }


    /**
     * Checks whether a given intersection point is illuminated (not in shadow)
     * by verifying that no geometry blocks the light before it reaches the point.
     *
     * @param intersection the intersection containing light direction and geometry
     * @return true if the point is illuminated (not in shadow), false otherwise
     */
    private boolean unshaded(Intersection intersection) {
        Vector pointToLight = intersection.l.scale(-1);
        Vector delta = intersection.normal.scale(intersection.lNormal < 0 ? DELTA : -DELTA);
        Ray shadowRay = new Ray(intersection.point.add(delta), pointToLight);
        List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay);
        if (intersections == null) return true;
        for (Intersection i : intersections) {
            if (i.geometry != intersection.geometry) {
                return false;
            }
        }
        return true;
    }

}