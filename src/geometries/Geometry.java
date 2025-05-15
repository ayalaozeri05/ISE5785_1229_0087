package geometries;

import primitives.*;
import java.util.List;

/**
 * Interface Geometry is the basic interface for all geometries in the scene
 */
public abstract class Geometry extends Intersectable {

    /**
     * Emission color of the geometry (default is black)
     */
    protected Color emission = Color.BLACK;

    /**
     * Get the normal to the geometry at a given point
     * @param point point to get the normal at
     * @return the normal to the geometry at the given point
     */
    public abstract Vector getNormal(Point point);

    /**
     * Get the emission color of the geometry
     * @return emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Set the emission color of the geometry
     * @param emission the emission color to set
     * @return the Geometry object itself (for method chaining)
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

   /* @Override
    public abstract List<Point> findIntersections(Ray ray);*/
}
