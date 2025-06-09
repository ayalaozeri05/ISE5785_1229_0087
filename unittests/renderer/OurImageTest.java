package renderer;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

public class OurImageTest {

    @Test
    public void pyramidWithSphereTest() {
        Scene scene = new Scene("PyramidWithSphere");

        // רקע ואור סביבתי חלש
        scene.setBackground(new Color(0, 0, 0));
        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        // הכדור האדום
        Sphere redSphere = new Sphere(new Point(0, 0, 4), 6);
        redSphere.setEmission(new Color(255, 0, 0));
        redSphere.setMaterial(new Material()
                .setKD(0.7)
                .setKS(0.2)
                .setShininess(50));

        // נקודות בסיס הפרמידה
        Point p1 = new Point(-20, -20, 0);
        Point p2 = new Point(20, -20, 0);
        Point p3 = new Point(20, 20, 0);
        Point p4 = new Point(-20, 20, 0);
        Point top = new Point(0, 0, 40);

        // משולשי צד של הפרמידה
        Triangle t2 = new Triangle(p2, p3, top); // צד ימין
        Triangle t3 = new Triangle(p3, p4, top); // הצד האחורי - מראה
        Triangle t4 = new Triangle(p4, p1, top); // צד שמאל

        Color lightBlue = new Color(150, 200, 255);

        Material pyramidMaterial = new Material()
                .setKD(0.4)
                .setKS(0.3)
                .setShininess(60)
                .setKT(0.3); // שקיפות

        Material mirrorMaterial = new Material()
                .setKD(0.2)
                .setKS(0.8)
                .setShininess(300)
                .setKR(0.7); // מראה חלקית

        t2.setEmission(lightBlue).setMaterial(pyramidMaterial);
        t3.setEmission(new Color(80, 80, 100)).setMaterial(mirrorMaterial); // צד אחורי - מראה
        t4.setEmission(lightBlue).setMaterial(pyramidMaterial);

        // בסיס הפרמידה - ירוק כהה
        Triangle base1 = new Triangle(p1, p2, p3);
        Triangle base2 = new Triangle(p3, p4, p1);
        Color darkGreen = new Color(20, 80, 20);
        Material baseMaterial = new Material()
                .setKD(0.8)
                .setKS(0.2)
                .setShininess(20);

        base1.setEmission(darkGreen).setMaterial(baseMaterial);
        base2.setEmission(darkGreen).setMaterial(baseMaterial);

        // הוספת הגיאומטריות לסצנה
        scene.geometries.add(redSphere, t2, t3, t4, base1, base2);

        // אור ממוקד חזק
        scene.lights.add(new SpotLight(
                new Color(700, 400, 400),
                new Point(50, -50, 60),
                new Vector(-1, 1, -1))
                .setKl(0.005).setKq(0.0001));

        // מצלמה
        Camera.Builder cameraBuilder = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE);

        cameraBuilder
                .setLocation(new Point(0, -100, 60))
                .setDirection(new Vector(0, 1, 0))
                .setVpDistance(100)
                .setVpSize(100, 100)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("final_image");
    }
}
