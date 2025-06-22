package renderer;

import geometries.Sphere;
import geometries.Triangle;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;
import renderer.BlackBoard;

import java.util.LinkedList;
import java.util.List;

public class MP1 {

    private final Scene scene = new Scene("Anti-Aliasing Test Scene");

    @Test
    public void AntiAliasingTestScene() {

        // Add geometries
        scene.geometries.add(
                new Sphere(new Point(0, 0, -1000), 500)
                        .setEmission(new Color(30, 30, 30))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30)),

                new Sphere(new Point(50, 50, -300), 30)
                        .setEmission(new Color(255, 0, 0))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(30)),

                new Sphere(new Point(-50, -50, -300), 30)
                        .setEmission(new Color(0, 255, 0))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(30)),

                new Sphere(new Point(50, -50, -300), 30)
                        .setEmission(new Color(0, 0, 255))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(30)),

                new Sphere(new Point(-50, 50, -300), 30)
                        .setEmission(new Color(255, 255, 0))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(30)),

                new Triangle(new Point(-100, 100, -400), new Point(-50, 150, -400), new Point(0, 100, -400))
                        .setEmission(new Color(128, 128, 128))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(30)),

                new Triangle(new Point(100, -100, -400), new Point(50, -150, -400), new Point(0, -100, -400))
                        .setEmission(new Color(64, 64, 64))
                        .setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(30)),
// משולש תכלת – עליון
                new Triangle(
                        new Point(-20, -15, -400),
                        new Point(20, -15, -400),
                        new Point(20, 15, -400))
                        .setEmission(new Color(173, 216, 230)) // תכלת
                        .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(30)),

// משולש סגול – תחתון, טיפה מאחור
                new Triangle(
                        new Point(-20, -15, -401),
                        new Point(20, 15, -401),
                        new Point(-20, 15, -401))
                        .setEmission(new Color(128, 0, 128)) // סגול
                        .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(30)),





                new Triangle(new Point(-100, 0, -500), new Point(100, 0, -500), new Point(0, 100, -500))
                .setEmission(new Color(255, 255, 255))
                .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60))
        );

        // Add lights
        List<LightSource> lights = new LinkedList<>();
        lights.add(new DirectionalLight(new Color(255, 255, 255), new Vector(0, -1, -1)));
        lights.add(new PointLight(new Color(255, 200, 150), new Point(-100, 100, 100))
                .setKl(0.001).setKq(0.0001));
        scene.setLights(lights);
        scene.setAmbientLight(new AmbientLight(new Color(3, 3, 3)));
        scene.setBackground(new Color(0, 0, 30));

        // ---------- Render WITHOUT Anti-Aliasing ----------
        Camera.Builder cameraBuilder = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 1000))
                .setDirection(new Vector(0, 0, -1))
                .setVpDistance(1000)
                .setVpSize(200, 200)
                .setResolution(500, 500)
                .setBlackBoard(new BlackBoard(9).setIsAntiAliasingEnabled(false)); // 9x9 = 81


        Camera camera = cameraBuilder.build();

// מדידת זמן
        long start = System.currentTimeMillis();
        camera.renderImage();
        long end = System.currentTimeMillis();

        System.out.println("Rendering time WITHOUT Anti-Aliasing: " + (end - start) + " ms");

// כתיבת תמונה
        camera.writeToImage("AntiAliasing_OFF");

        // ---------- Render WITH Anti-Aliasing ----------
        // ---------- Render WITH Anti-Aliasing ----------
        Camera.Builder cameraBuilder2 = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setLocation(new Point(0, 0, 1000))
                .setDirection(new Vector(0, 0, -1))
                .setVpDistance(1000)
                .setVpSize(200, 200)
                .setResolution(500, 500)
                .setBlackBoard(new BlackBoard(9).setIsAntiAliasingEnabled(true));// 9x9 = 81


        Camera camera2 = cameraBuilder2.build();  //

        long start2 = System.currentTimeMillis();
        camera2.renderImage();  // ✅ משתמשים ב־camera2
        long end2 = System.currentTimeMillis();

        System.out.println("Rendering time WITH Anti-Aliasing: " + (end2 - start2) + " ms");

        camera2.writeToImage("AntiAliasing_ON");  // ✅ camera2, לא camera
    }
}