package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.BlackBoard;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

public class AdaptiveAntiAliasingTest {


    /**
     * Helper function to build a test scene for anti-aliasing and multithreading experiments.
     * @return a {@link Scene} object populated with geometries and lights
     */
    private Scene createScene() {
        Scene scene = new Scene("Improved Anti-Aliasing Scene");


        Material cylinderMaterial = new Material().setKD(0.4).setKS(0.5).setShininess(150).setKR(0.2);

        // room
        scene.geometries.add(
                new Plane(new Point(0, -50, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(5, 5, 10))
                        .setMaterial(new Material().setKD(0.3).setKS(0.2).setShininess(100).setKR(0.1)),
                new Plane(new Point(0, 150, 0), new Vector(0, -1, 0))
                        .setEmission(new Color(5, 5, 10))
                        .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(60).setKR(0.1)),
                new Plane(new Point(0, 0, -150), new Vector(0, 0, 1))
                        .setEmission(new Color(5, 5, 10))
                        .setMaterial(new Material().setKD(0.4).setKS(0.15).setShininess(80).setKR(0.1)),
                new Plane(new Point(-100, 0, 0), new Vector(1, 0, 0)),
                new Plane(new Point(100, 0, 0), new Vector(-1, 0, 0))
        );

        Color[] coolColors = {
                new Color(52, 41, 119), new Color(45, 85, 175), new Color(0, 140, 200),
                new Color(22, 140, 130), new Color(40, 130, 90), new Color(90, 84, 200),
                new Color(76, 60, 180), new Color(50, 110, 160)
        };

        addFlowerSpheres(scene, 50, 0, 30, 8, 20, coolColors);

        scene.geometries.add(
                new Triangle(new Point(-70, 20, -50), new Point(-30, 40, -40), new Point(-50, 20, -30))
                        .setEmission(new Color(20, 180, 180))
                        .setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(200).setKR(0.15)),
                new Triangle(new Point(70, 20, -50), new Point(30, 40, -40), new Point(50, 20, -30))
                        .setEmission(new Color(200, 60, 150))
                        .setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(200).setKR(0.15))
        );
        Cylinder cylinder1 = new Cylinder(new Ray(new Point(-30, -50, 0), new Vector(0, 1, 0)), 10, 60);
        cylinder1.setEmission(new Color(50, 100, 150)).setMaterial(cylinderMaterial);
        Cylinder cylinder2 = new Cylinder(new Ray(new Point(30, -50, 40), new Vector(0, 1, 0)), 6, 40);
        cylinder2.setEmission(new Color(80, 80, 120)).setMaterial(cylinderMaterial);
        scene.geometries.add(cylinder1, cylinder2);
        scene.setAmbientLight(new AmbientLight(new Color(10, 10, 15)));
        scene.lights.add(new SpotLight(new Color(200, 120, 200), new Point(0, 130, 50), new Vector(0, -1, -1))
                .setKl(0.0007).setKq(0.00007));
        scene.lights.add(new PointLight(new Color(150, 200, 150), new Point(-60, 120, 40))
                .setKl(0.0007).setKq(0.00007));
        scene.lights.add(new PointLight(new Color(150, 150, 200), new Point(60, 120, 40))
                .setKl(0.0007).setKq(0.00007));

        return scene;
    }



    /**
     * Helper function to add a "flower" of spheres into the scene.
     * <p>
     * Places one central sphere at (centerX, centerY, centerZ) and surrounds it
     * with 8 additional spheres arranged in a circular pattern at equal angles.
     * Each surrounding sphere is assigned a color from the provided array.
     *
     * @param scene    the scene to which the spheres are added
     * @param centerX  x-coordinate of the central sphere
     * @param centerY  y-coordinate of the central sphere
     * @param centerZ  z-coordinate of the central sphere
     * @param radius   radius of each sphere
     * @param distance distance from the center to the surrounding spheres
     * @param colors   array of colors to assign to the spheres
     */
    private void addFlowerSpheres(Scene scene, double centerX, double centerY, double centerZ,
                                  double radius, double distance, Color[] colors) {
        Material mat = new Material().setKD(0.4).setKS(0.5).setShininess(300).setKR(0.3);
        scene.geometries.add(new Sphere(new Point(centerX, centerY, centerZ), radius)
                .setEmission(colors[0]).setMaterial(mat));
        for (int k = 0; k < 8; k++) {
            double angle = k * Math.PI / 4;
            double x = centerX + distance * Math.cos(angle);
            double y = centerY + distance * Math.sin(angle);
            Color color = colors[k % colors.length];
            scene.geometries.add(new Sphere(new Point(x, y, centerZ), radius)
                    .setEmission(color).setMaterial(mat));
        }
    }


    /**
     * Helper function to build a {@link Camera} for rendering tests.
     * <p>
     * Configures a camera positioned at (0,0,200) looking toward (0,0,-50) with
     * a fixed view-plane size and resolution. The function optionally enables
     * Adaptive Anti-Aliasing (AAA) and/or multithreading (MT) depending on the
     * boolean flags provided.
     *
     * @param scene     the {@link Scene} the camera will render
     * @param enableAAA true to enable Adaptive Anti-Aliasing (depth=4, threshold=3), false otherwise
     * @param enableMT  true to enable multithreading (6 threads), false otherwise
     * @return a configured {@link Camera} instance
     */
    private Camera createCamera(Scene scene, boolean enableAAA, boolean enableMT) {
        Camera.Builder builder = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 200))
                .setDirection(new Point(0, 0, -50), Vector.AXIS_Y)
                .setVpDistance(200)
                .setVpSize(250, 250)
                .setResolution(800, 800)
                // חשוב: תמיד BlackBoard כדי למנוע NPE כשה-AAA כבוי
                .setBlackBoard(new BlackBoard(1).setIsAntiAliasingEnabled(false));

        if (enableAAA) {
            builder.enableAdaptiveAntiAliasing(4, 3); // עומק 4, סף 3
        }

        Camera camera = builder.build();

        if (enableMT) {
            camera.setMultithreading(true).setThreadsCount(6);
        }

        return camera;
    }

    /**
     * Test method for rendering with AAA off and multithreading off.
     * Measures baseline render time without any acceleration or threads.
     */
    @Test
    void AAA_OFF_MT_OFF() {
        Scene scene = createScene();
        Camera cam = createCamera(scene, false, false);
        long t1 = System.currentTimeMillis();
        cam.renderImage().writeToImage("AAA_OFF_MT_OFF");
        long t2 = System.currentTimeMillis();
        System.out.println("Time (AAA OFF, MT OFF): " + (t2 - t1) + " ms");
    }


    /**
     * Test method for rendering with AAA off and multithreading on.
     * Measures the effect of multithreading alone on render time.
     */
    @Test
    void AAA_OFF_MT_ON() {
        Scene scene = createScene();
        Camera cam = createCamera(scene, false, true);
        long t1 = System.currentTimeMillis();
        cam.renderImage().writeToImage("AAA_OFF_MT_ON");
        long t2 = System.currentTimeMillis();
        System.out.println("Time (AAA OFF, MT ON): " + (t2 - t1) + " ms");
    }


    /**
     * Test method for rendering with AAA on (depth=4, threshold=3) and multithreading off.
     * Measures the effect of Adaptive Anti-Aliasing alone on render time.
     */
    @Test
    void AAA_ON_MT_OFF() {
        Scene scene = createScene();
        Camera cam = createCamera(scene, true, false);
        long t1 = System.currentTimeMillis();
        cam.renderImage().writeToImage("AAA_ON_MT_OFF");
        long t2 = System.currentTimeMillis();
        System.out.println("Time (AAA ON, MT OFF): " + (t2 - t1) + " ms");
    }


    /**
     * Test method for rendering with AAA on (depth=4, threshold=3) and multithreading on.
     * Measures the combined effect of AAA and multithreading on render time.
     */
    @Test
    void AAA_ON_MT_ON() {
        Scene scene = createScene();
        Camera cam = createCamera(scene, true, true);
        long t1 = System.currentTimeMillis();
        cam.renderImage().writeToImage("AAA_ON_MT_ON");
        long t2 = System.currentTimeMillis();
        System.out.println("Time (AAA ON, MT ON): " + (t2 - t1) + " ms");
    }

    /**
     * Test method for comparing MP1 grid-AA 9x9 with AAA depth=4, threshold=3.
     * Checks if AAA provides similar image quality to MP1 while reducing render time.
     */
    @Test
    void compare_MP1_gridAA_9x9_vs_AAA_depth4() {
        Scene scene = createScene();

        // MP1 grid-AA 9x9 (ללא AAA)
        Camera camMP1 = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 200))
                .setDirection(new Point(0, 0, -50), Vector.AXIS_Y)
                .setVpDistance(200)
                .setVpSize(250, 250)
                .setResolution(800, 800)
                .setBlackBoard(new BlackBoard(9).setIsAntiAliasingEnabled(true))
                .build();

        long b1 = System.currentTimeMillis();
        camMP1.renderImage().writeToImage("BASELINE_MP1_gridAA_9x9");
        long b2 = System.currentTimeMillis();
        System.out.println("Baseline MP1 grid-AA 9x9: " + (b2 - b1) + " ms");

        // AAA depth=4 (ללא grid AA)
        Camera camAAA = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 200))
                .setDirection(new Point(0, 0, -50), Vector.AXIS_Y)
                .setVpDistance(200)
                .setVpSize(250, 250)
                .setResolution(800, 800)
                .setBlackBoard(new BlackBoard(1).setIsAntiAliasingEnabled(false))
                .enableAdaptiveAntiAliasing(4, 3)
                .build();

        long a1 = System.currentTimeMillis();
        camAAA.renderImage().writeToImage("AAA_depth4_threshold3");
        long a2 = System.currentTimeMillis();
        System.out.println("AAA depth=4, threshold=3: " + (a2 - a1) + " ms");
    }


    /**
     * Test method for comparing MP1 grid-AA 9x9 with AAA depth=4, threshold=3.
     * Checks if AAA provides similar image quality to MP1 while reducing render time.
     */
    @Test
    void tune_ASS_threshold_depth4() {
        Scene scene = createScene();
        int res = 800;
        int depth = 4;
        int[] thresholds = {2, 3, 4, 6, 8, 10};

        for (int th : thresholds) {
            Camera cam = Camera.getBuilder()
                    .setRayTracer(scene, RayTracerType.SIMPLE)
                    .setLocation(new Point(0, 0, 200))
                    .setDirection(new Point(0, 0, -50), Vector.AXIS_Y)
                    .setVpDistance(200)
                    .setVpSize(250, 250)
                    .setResolution(res, res)
                    .setBlackBoard(new renderer.BlackBoard(1).setIsAntiAliasingEnabled(false))
                    .enableAdaptiveAntiAliasing(depth, th)
                    .build()
                    .setMultithreading(true).setThreadsCount(6);

            long t1 = System.currentTimeMillis();
            cam.renderImage().writeToImage("AAA_d" + depth + "_th" + th + "_MT6_" + res);
            long t2 = System.currentTimeMillis();
            System.out.println("[ASS] depth=" + depth + ", th=" + th + ", MT=6, " + res + "x" + res + " : " + (t2 - t1) + " ms");
        }
    }


    /**
     * Test method for tuning depth values of AAA at fixed threshold=3.
     * Evaluates image quality vs render time tradeoff for different depth values.
     */
    @Test
    void tune_ASS_depth_threshold3() {
        Scene scene = createScene();
        int res = 800;
        int th = 3;
        int[] depths = {2, 3, 4, 5};

        for (int d : depths) {
            Camera cam = Camera.getBuilder()
                    .setRayTracer(scene, RayTracerType.SIMPLE)
                    .setLocation(new Point(0, 0, 200))
                    .setDirection(new Point(0, 0, -50), Vector.AXIS_Y)
                    .setVpDistance(200)
                    .setVpSize(250, 250)
                    .setResolution(res, res)
                    .setBlackBoard(new renderer.BlackBoard(1).setIsAntiAliasingEnabled(false))
                    .enableAdaptiveAntiAliasing(d, th)
                    .build()
                    .setMultithreading(true).setThreadsCount(6);

            long t1 = System.currentTimeMillis();
            cam.renderImage().writeToImage("AAA_d" + d + "_th" + th + "_MT6_" + res);
            long t2 = System.currentTimeMillis();
            System.out.println("[ASS] depth=" + d + ", th=" + th + ", MT=6, " + res + "x" + res + " : " + (t2 - t1) + " ms");
        }
    }

    /**
     * Test method for tuning number of threads for AAA depth=4, threshold=3.
     * Determines optimal thread count for best performance.
     */
    @Test
    void tune_threads_ASS_d4_th3() {
        Scene scene = createScene();
        int res = 800;
        int depth = 4, th = 3;
        int[] threadsArr = {1, 2, 4, 6, 8};

        for (int t : threadsArr) {
            Camera cam = Camera.getBuilder()
                    .setRayTracer(scene, RayTracerType.SIMPLE)
                    .setLocation(new Point(0, 0, 200))
                    .setDirection(new Point(0, 0, -50), Vector.AXIS_Y)
                    .setVpDistance(200)
                    .setVpSize(250, 250)
                    .setResolution(res, res)
                    .setBlackBoard(new renderer.BlackBoard(1).setIsAntiAliasingEnabled(false))
                    .enableAdaptiveAntiAliasing(depth, th)
                    .build()
                    .setMultithreading(true).setThreadsCount(t);

            long t1 = System.currentTimeMillis();
            cam.renderImage().writeToImage("AAA_d" + depth + "_th" + th + "_MT" + t + "_" + res);
            long t2 = System.currentTimeMillis();
            System.out.println("[ASS] MT=" + t + ", depth=" + depth + ", th=" + th + ", " + res + "x" + res + " : " + (t2 - t1) + " ms");
        }
    }


    /**
     * Test method for heavy load comparison (1000x1000) between MP1 grid-AA 9x9 and AAA depth=4, threshold=3.
     * Checks if AAA remains faster than MP1 under high-resolution rendering while maintaining image quality.
     */
    @Test
    void heavy_1000_compare_MP1_vs_ASS() {
        Scene scene = createScene();
        int res = 1000;

        // MP1 grid-AA 9x9
        Camera camMP1 = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 200))
                .setDirection(new Point(0, 0, -50), Vector.AXIS_Y)
                .setVpDistance(200)
                .setVpSize(250, 250)
                .setResolution(res, res)
                .setBlackBoard(new renderer.BlackBoard(9).setIsAntiAliasingEnabled(true))
                .build()
                .setMultithreading(true).setThreadsCount(6);
        long b1 = System.currentTimeMillis();
        camMP1.renderImage().writeToImage("HEAVY_BASELINE_MP1_gridAA_9x9_MT6_" + res);
        long b2 = System.currentTimeMillis();
        System.out.println("[MP1 9x9] " + res + "x" + res + " MT=6 : " + (b2 - b1) + " ms");

        // ASS depth=4, th=3
        Camera camASS = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 200))
                .setDirection(new Point(0, 0, -50), Vector.AXIS_Y)
                .setVpDistance(200)
                .setVpSize(250, 250)
                .setResolution(res, res)
                .setBlackBoard(new renderer.BlackBoard(1).setIsAntiAliasingEnabled(false))
                .enableAdaptiveAntiAliasing(4, 3)
                .build()
                .setMultithreading(true).setThreadsCount(6);
        long a1 = System.currentTimeMillis();
        camASS.renderImage().writeToImage("HEAVY_ASS_d4_th3_MT6_" + res);
        long a2 = System.currentTimeMillis();
        System.out.println("[ASS d4 th3] " + res + "x" + res + " MT=6 : " + (a2 - a1) + " ms");
    }



}
