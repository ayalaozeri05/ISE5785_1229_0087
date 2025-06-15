package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * SimpleRayTracer is a basic implementation of a ray tracer that calculates the color of pixels in a scene.
 * It handles local and global effects such as reflection and refraction, and supports transparency.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Maximum recursion level for color calculation.
     * This limits the depth of reflection and refraction calculations.
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;

    /**
     * Minimum value for color calculation to avoid unnecessary computations.
     * If the accumulated color is below this threshold, it is considered negligible.
     */
    private static final double MIN_CALC_COLOR_K = 0.001;

    /**
     * Initial value for the color attenuation factor.
     * This is used to start the color calculation process.
     */
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * Constructs a new SimpleRayTracer with the given scene.
     *
     * @param scene the scene that will be rendered using this ray tracer
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Checks if the intersection point is unshaded by any geometry.
     *
     * @param intersection the intersection point to check
     * @return true if the intersection point is unshaded, false otherwise
     */
    private boolean unshaded(Intersection intersection) {
        Vector pointToLight = intersection.l.scale(-1); // from the point to the light source
        // Create a ray from the point to the light source
        Ray ray = new Ray(intersection.point, pointToLight, intersection.normal);
        var intersections = scene.geometries.
                calculateIntersections(ray);
        if (intersections == null)
            return true;
        else {
            for (Intersection i : intersections)
                if (i.material.kT.lowerThan(MIN_CALC_COLOR_K))
                    return false;
        }
        return true;
    }

    /**
     * Calculates the transparency factor (ktr) from the intersection point toward the light source.
     *
     * @param intersection the intersection point being evaluated
     * @return the accumulated transparency factor along the path to the light source
     */
    private Double3 transparency(Intersection intersection) {
        Vector pointToLight = intersection.l.scale(-1); // from the point to the light source
        Ray ray = new Ray(intersection.point, pointToLight, intersection.normal); // create a ray from the point to the light source
        var intersections = scene.geometries.calculateIntersections(ray);
        Double3 ktr = Double3.ONE;
        if (intersections == null)
            return ktr;
        double maxDistance = intersection.light.getDistance(intersection.point);
        for (Intersection i : intersections) {
            if (alignZero(i.point.distance(intersection.point) - maxDistance) <= 0) {
                ktr = ktr.product(i.material.kT);
                if (ktr.lowerThan(MIN_CALC_COLOR_K))
                    return Double3.ZERO;
            }
        }
        return ktr;
    }

    /**
     * Preprocesses the intersection by setting the view vector and normal vector.
     *
     * @param intersection The intersection object to preprocess.
     * @param v            The view vector.
     * @return True if the view vector is not parallel to the normal vector, false otherwise.
     */
    private boolean preprocessIntersection(Intersection intersection, Vector v) {
        intersection.v = v;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = intersection.v.dotProduct(intersection.normal);
        return !isZero(intersection.vNormal);
    }

    /**
     * Sets the light source for the intersection and calculates the light vector.
     *
     * @param intersection The intersection object to set the light source for.
     * @param light        The light source to set.
     * @return True if the light vector is not parallel to the normal vector, false otherwise.
     */
    private boolean setLightSource(Intersection intersection, LightSource light) {
        intersection.light = light;
        intersection.l = light.getL(intersection.point);
        intersection.lNormal = intersection.l.dotProduct(intersection.normal);
        return alignZero(intersection.lNormal * intersection.vNormal) > 0;
    }

    /**
     * Calculates the global lighting effects (reflection and refraction) at the intersection point.
     *
     * @param intersection the intersection point being evaluated
     * @param level        the recursion level for global lighting
     * @param k            the accumulated transparency/reflection coefficient
     * @return the resulting color from global lighting effects
     */
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        return calcGlobalEffect(constructRefractedRay(intersection), level, k, intersection.material.kT)
                .add(calcGlobalEffect(constructReflectedRay(intersection), level, k, intersection.material.kR));
    }

    /**
     * Calculates the global color contribution from a single reflected or refracted ray.
     *
     * @param ray   the reflected or refracted ray
     * @param level the current recursion level
     * @param k     the accumulated attenuation factor so far
     * @param kx    the reflection or refraction coefficient for the current step
     * @return the color contribution from this global effect
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) return scene.background.scale(kx);
        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level - 1, kkx).scale(kx) : Color.BLACK;
    }

    /**
     * Calculates the refracted ray starting from the intersection point, with the vector of the intersecting ray.
     *
     * @param intersection the intersection point
     * @return the refracted ray
     */
    private Ray constructRefractedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.v, intersection.normal);
    }

    /**
     * Calculates the reflected ray starting from the intersection point.
     *
     * @param intersection the intersection point
     * @return the reflected ray(mirror)
     */
    private Ray constructReflectedRay(Intersection intersection) {
        Vector r = intersection.v.add((intersection.normal.scale(-2 * intersection.vNormal)));
        return new Ray(intersection.point, r, intersection.normal);
    }

    /**
     * Finds the closest intersection point along the given ray.
     *
     * @param ray the ray to trace
     * @return the closest intersection, or null if there are no intersections
     */
    private Intersection findClosestIntersection(Ray ray) {
        var intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null ? null : ray.findClosestIntersection(intersections);
    }

    /**
     * Calculates the color at the intersection point based on local effects.
     *
     * @param intersection The intersection object to calculate the color for.
     * @param k            The accumulated transparency/reflection coefficient.
     * @return The color at the intersection point.
     */
    private Color calcColorLocalEffects(Intersection intersection, Double3 k) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : scene.lights) {
            // also checks if sign(lNormal) == sign(vNormal)) and if the intersection is unshaded
            if (!setLightSource(intersection, lightSource))
                continue;

            Double3 ktr = transparency(intersection);
            if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                Color iL = lightSource.getIntensity(intersection.point).scale(ktr);
                color = color
                        .add(iL.scale(calcDiffusive(intersection)
                                .add(calcSpecular(intersection))));
            }
        }
        return color;
    }

    /**
     * Calculates the specular component of the color at the intersection point.
     *
     * @param intersection The intersection object to calculate the specular component for.
     * @return The specular component of the color at the intersection point.
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.l.subtract(intersection.normal.scale(2d * intersection.lNormal));
        double vr = alignZero(intersection.v.dotProduct(r));
        if (vr >= 0d) return Double3.ZERO;
        return intersection.material.kS.scale(Math.pow(-vr, intersection.material.nsh));
    }

    /**
     * Calculates the diffusive component of the color at the intersection point.
     *
     * @param intersection The intersection object to calculate the diffusive component for.
     * @return The diffusive component of the color at the intersection point.
     */
    private Double3 calcDiffusive(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.lNormal));
    }

    /**
     * Calculates the color of the pixel based on the intersection point and the ray.
     *
     * @param intersection The intersection point of the ray with the geometry.
     * @param ray          The ray being traced.
     * @return The calculated color for the pixel.
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        return preprocessIntersection(intersection, ray.getDirection()) ?
                calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity().scale(intersection.geometry.getMaterial().kA)) : Color.BLACK;

    }

    /**
     * Recursively calculates the color at the intersection point, considering local and global effects.
     *
     * @param intersection The intersection point of the ray with the geometry.
     * @param level        The current recursion level for global effects.
     * @param k            The accumulated transparency/reflection coefficient.
     * @return The calculated color for the pixel at the intersection point.
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcColorLocalEffects(intersection, k);
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, level, k));
    }

    @Override
    public Color traceRay(Ray ray) {
        Intersection closestIntersection = findClosestIntersection(ray);
        return closestIntersection == null ? scene.background : calcColor(closestIntersection, ray);
    }
}