package renderer;

import geometries.Polygon;
import geometries.Sphere;
import lighting.AmbientLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

/**
 * A test class that renders a scene containing three shapes—a red sphere, a green pyramid,
 * and a yellow cube—placed on a blue translucent floor. The background is set to black
 * so that the floor and shapes stand out. A dedicated spotlight is added to illuminate
 * all three shapes uniformly.
 */
public class OurImageTest {
    /**
     * Default constructor to satisfy JavaDoc generator/**
     */
    OurImageTest() { /* to satisfy JavaDoc generator */ }

    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Reflective Shapes on Blue Box");
    /**
     * Camera builder for the tests
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE);

    /**
     * Camera for the tests
     */
    @Test
    void threeShapesInBoxTest() {
        // 1) Set the background to black for maximum contrast
        scene.background = new Color(0, 0, 0);

        // 2) Add a subtle ambient light to softly illuminate the entire scene
        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        // 3) Floor: light blue, partially transparent and reflective
        scene.geometries.add(
                new Polygon(
                        new Point(-200, -50, -200),
                        new Point(200, -50, -200),
                        new Point(200, -50, -600),
                        new Point(-200, -50, -600)
                )
                        .setEmission(new Color(0, 100, 200))  // Light blue emission
                        .setMaterial(new Material()
                                .setKD(0.1)      // Low diffuse
                                .setKS(0.8)      // High specular for glossy finish
                                .setKR(0.4)      // Partial reflection
                                .setKT(0.3)      // Slight transparency
                                .setShininess(150)
                        )
        );

        // 4) Ceiling: dark purple, minimal shine
        scene.geometries.add(
                new Polygon(
                        new Point(-220, 120, -200),
                        new Point(220, 120, -200),
                        new Point(220, 120, -600),
                        new Point(-220, 120, -600)
                )
                        .setEmission(new Color(20, 0, 50))  // Dark purple emission
                        .setMaterial(new Material()
                                .setKS(0.1)      // Very low specular
                                .setShininess(20)
                        )
        );

        // 5) Left wall: dark blue
        scene.geometries.add(
                new Polygon(
                        new Point(-220, -50, -200),
                        new Point(-220, 120, -200),
                        new Point(-220, 120, -600),
                        new Point(-220, -50, -600)
                )
                        .setEmission(new Color(0, 0, 120))  // Dark blue emission
                        .setMaterial(new Material()
                                .setKS(0.1)
                                .setShininess(20)
                        )
        );

        // 6) Right wall: dark teal
        scene.geometries.add(
                new Polygon(
                        new Point(220, -50, -200),
                        new Point(220, 120, -200),
                        new Point(220, 120, -600),
                        new Point(220, -50, -600)
                )
                        .setEmission(new Color(0, 80, 120))  // Dark teal emission
                        .setMaterial(new Material()
                                .setKS(0.1)
                                .setShininess(20)
                        )
        );

        // 7) --------- Three shapes: sphere, pyramid, and cube ---------

        // 7.1) Red sphere, radius 30, centered at (-80, -20, -400), resting on the floor (y = -50)
        scene.geometries.add(
                new Sphere( new Point(-80, -20, -400),30)
                        .setEmission(new Color(255, 0, 0))   // Bright red emission
                        .setMaterial(new Material()
                                .setKT(0.0)      // Opaque
                                .setKD(0.1)      // Low diffuse
                                .setKS(0.9)      // Very shiny
                                .setKR(0.2)      // Slight reflection
                                .setShininess(300)
                        )
        );

        // 7.2) Green pyramid with square base, total height 60, base on y = -50
        //      Base is a square of size 60 × 60 centered at (0, -50, -400)
        Point p1 = new Point(-30, -50, -350);
        Point p2 = new Point(30, -50, -350);
        Point p3 = new Point(30, -50, -450);
        Point p4 = new Point(-30, -50, -450);
        Point apex = new Point(0, 10, -400);  // Apex at y = 10 (60 units above base)

        Material pyramidMaterial = new Material()
                .setKT(0.0)
                .setKD(0.1)
                .setKS(0.9)
                .setKR(0.2)
                .setShininess(300);

        // Four triangular faces of the pyramid
        scene.geometries.add(
                // Front face
                new Polygon(p1, p2, apex)
                        .setEmission(new Color(0, 200, 0))   // Bright green emission
                        .setMaterial(pyramidMaterial),
                // Right face
                new Polygon(p2, p3, apex)
                        .setEmission(new Color(0, 200, 0))
                        .setMaterial(pyramidMaterial),
                // Back face
                new Polygon(p3, p4, apex)
                        .setEmission(new Color(0, 200, 0))
                        .setMaterial(pyramidMaterial),
                // Left face
                new Polygon(p4, p1, apex)
                        .setEmission(new Color(0, 200, 0))
                        .setMaterial(pyramidMaterial)
        );

        // 7.3) Yellow cube of size 60 × 60 × 60, resting on the floor:
        //      X ∈ [50, 110], Y ∈ [−50, 10], Z ∈ [−430, −370]
        double halfSize = 30; // half of 60
        double cx = 80, cy = -20, cz = -400;

        // Eight vertices of the cube
        Point v1 = new Point(cx - halfSize, cy + halfSize, cz - halfSize);
        Point v2 = new Point(cx + halfSize, cy + halfSize, cz - halfSize);
        Point v3 = new Point(cx + halfSize, cy - halfSize, cz - halfSize);
        Point v4 = new Point(cx - halfSize, cy - halfSize, cz - halfSize);
        Point v5 = new Point(cx - halfSize, cy + halfSize, cz + halfSize);
        Point v6 = new Point(cx + halfSize, cy + halfSize, cz + halfSize);
        Point v7 = new Point(cx + halfSize, cy - halfSize, cz + halfSize);
        Point v8 = new Point(cx - halfSize, cy - halfSize, cz + halfSize);

        Material cubeMaterial = new Material()
                .setKT(0.0)
                .setKD(0.1)
                .setKS(0.9)
                .setKR(0.3)
                .setShininess(200);

        // Front face (z = cz - halfSize)
        scene.geometries.add(
                new Polygon(v1, v2, v3, v4)
                        .setEmission(new Color(255, 255, 0))  // Bright yellow emission
                        .setMaterial(cubeMaterial)
        );
        // Back face (z = cz + halfSize)
        scene.geometries.add(
                new Polygon(v5, v6, v7, v8)
                        .setEmission(new Color(255, 255, 0))
                        .setMaterial(cubeMaterial)
        );
        // Left face (x = cx - halfSize)
        scene.geometries.add(
                new Polygon(v1, v5, v8, v4)
                        .setEmission(new Color(255, 255, 0))
                        .setMaterial(cubeMaterial)
        );
        // Right face (x = cx + halfSize)
        scene.geometries.add(
                new Polygon(v2, v6, v7, v3)
                        .setEmission(new Color(255, 255, 0))
                        .setMaterial(cubeMaterial)
        );
        // Top face (y = cy + halfSize)
        scene.geometries.add(
                new Polygon(v1, v2, v6, v5)
                        .setEmission(new Color(255, 255, 0))
                        .setMaterial(cubeMaterial)
        );
        // Bottom face (y = cy - halfSize) - invisible since floor covers it,
        // but define with dark gray if needed
        scene.geometries.add(
                new Polygon(v4, v3, v7, v8)
                        .setEmission(new Color(30, 30, 30))
                        .setMaterial(new Material()
                                .setKS(0.2)
                                .setShininess(20)
                        )
        );

        // 8) --------- Lighting: Spotlights to highlight all three shapes ---------

        // 8.1) Central overhead spotlight to brighten all three shapes
        scene.lights.add(
                new SpotLight(
                        new Color(800, 800, 800),      // White-ish light
                        new Point(0, 150, 0),          // Positioned above center
                        new Vector(0, -1, -1)          // Directed downward and forward
                )
                        .setKl(0.0001)
                        .setKq(0.00005)
        );

        // 8.2) Side spotlight to the left to bring out the pyramid’s faces
        scene.lights.add(
                new SpotLight(
                        new Color(500, 500, 800),      // Cool blueish light
                        new Point(-100, 50, -350),     // To the left-front of pyramid
                        new Vector(1, -1, -1)          // Directed toward pyramid
                )
                        .setKl(0.0001)
                        .setKq(0.00004)
        );

        // 8.3) Side spotlight to the right to accentuate the cube’s edges
        scene.lights.add(
                new SpotLight(
                        new Color(500, 800, 500),      // Warm greenish light
                        new Point(180, 50, -380),      // To the right-front of cube
                        new Vector(-1, -1, -1)         // Directed toward cube
                )
                        .setKl(0.0001)
                        .setKq(0.00004)
        );

        // 8.4) Bottom spotlight shining upwards to reduce harsh shadows under the sphere
        scene.lights.add(
                new SpotLight(
                        new Color(300, 300, 600),      // Subtle bluish light
                        new Point(0, -100, -400),      // Directly below sphere
                        new Vector(0, 1, 0)            // Directed straight up
                )
                        .setKl(0.0003)
                        .setKq(0.00005)
        );

        // 9) --------- Camera setup and render ---------
        cameraBuilder
                .setLocation(new Point(0, 0, 500))             // Camera position
                .setDirection(new Point(0, 0, -400))           // Looking toward center of box
                .setVpDistance(1000)                           // Viewplane distance
                .setVpSize(500, 500)                           // Viewplane size
                .setResolution(800, 800)                       // Image resolution
                .build()
                .renderImage()
                .writeToImage("threeShapesOnBlueBox");
    }
}