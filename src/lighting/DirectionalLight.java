package lighting;

import primitives.*;

/**
 * The LightSource interface represents a light source in a 3D scene.
 * It provides methods to get the intensity and direction of the light at a given point.
 */
public class DirectionalLight extends Light implements LightSource {
    /**
     * Represents the direction of the directional light source.
     */
    private final Vector direction;

    /**
     * Constructor to create a PointLight object with a specified intensity and position.
     *
     * @param intensity The intensity of the light as a Color object.
     * @param direction The position of the light as a Point object.
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }

}
