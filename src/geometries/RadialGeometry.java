package geometries;

/**
 * Abstract class for radial geometries (e.g., Sphere, Tube).
 */
public abstract class RadialGeometry extends Geometry {
    protected final double radius;

    /**
     * Constructor for radial geometry.
     *
     * @param radius the radius of the geometry.
     */
    public RadialGeometry(double radius) {
        if (radius <= 0)
            throw new IllegalArgumentException("Radius must be positive");
        this.radius = radius;
    }
}