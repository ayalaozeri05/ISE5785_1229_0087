package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

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
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
