package lighting;

import primitives.Color;

/**
 * Class representing ambient light (global background illumination).
 */
public class AmbientLight {

    /** Constant instance representing no ambient light (black) */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /** The intensity (color) of the ambient light */
    private final Color intensity;

    /**
     * Constructor for AmbientLight.
     *
     * @param Ia the intensity (RGB color) of the ambient light
     */
    public AmbientLight(Color Ia) {
        this.intensity = Ia;
    }

    /**
     * Getter for the intensity of the ambient light.
     *
     * @return the color representing the intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}