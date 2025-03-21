package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Abstract class for geometric bodies.
 */
public abstract class Geometry {
    /**
     * Get the normal (perpendicular vector) to the geometry at a given point.
     *
     * @param point the point on the geometry.
     * @return the normal vector to the geometry.
     */
    public abstract Vector getNormal(Point point);
}
