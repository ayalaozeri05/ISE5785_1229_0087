package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Abstract base class for all geometries that can be intersected by a ray
 */
public abstract class Intersectable {

    /**
     * Passive data structure representing an intersection point and its geometry
     */
    public static class Intersection {
        public final Geometry geometry;
        public final Point point;

        /**
         * Constructor for Intersection
         * @param geometry the intersected geometry
         * @param point the intersection point
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Intersection other = (Intersection) obj;
            return geometry == other.geometry && point.equals(other.point);
        }

        @Override
        public String toString() {
            return "Intersection{geometry=" + geometry + ", point=" + point + "}";
        }
    }

    /**
     * NVI method: public method that uses protected helper
     * @param ray the ray to intersect with
     * @return list of intersection objects (geometry + point)
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    /**
     * Helper method to be implemented in derived classes
     * @param ray the ray to intersect with
     * @return list of intersection objects (geometry + point)
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

    /**
     * Returns only the intersection points from the full intersection data
     * @param ray the ray to intersect with
     * @return list of intersection points (or null if none)
     */
    public final List<Point> findIntersections(Ray ray) {
        var intersections = calculateIntersections(ray);
        return intersections == null ? null
                : intersections.stream().map(intersection -> intersection.point).toList();
    }
}
