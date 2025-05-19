package geometries;

import lighting.LightSource;
import primitives.*;

import java.util.List;

/**
 * The Intersectable interface defines a contract for geometric shapes
 * that can be intersected by rays.
 * It provides a method to find the intersection points of a ray with the shape.
 */
public abstract class Intersectable {
    /**
     * Finds the intersection points of a given ray with the shape.
     *
     * @param ray The ray to check for intersections.
     * @return A list of intersection points, or an empty list if there are no intersections.
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }

    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    public static class Intersection {
        public final Geometry geometry;
        public final Point point;
        public final Material material;
        public Vector normal;
        public Vector v;
        public double vNormal;
        public LightSource light;
        public Vector l;
        public double lNormal;

        /**
         * Constructor for the Intersection class.
         * Initializes an intersection with a given geometry and point of intersection.
         * If the geometry is not null, the material is taken from the geometry;
         * otherwise, a default material is used.
         *
         * @param geometry the geometry object that was intersected
         * @param point    the point at which the intersection occurred
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            if (geometry != null) this.material = geometry.getMaterial();
            else this.material = new Material();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return obj instanceof Intersection other &&
                    geometry == (other.geometry) && point.equals(other.point);
        }

        @Override
        public String toString() {
            return "GeoPoint{" + "geometry=" + geometry + ", point=" + point + '}';
        }

    }
}