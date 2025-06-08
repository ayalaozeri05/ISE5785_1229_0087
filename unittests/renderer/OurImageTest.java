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

        // רקע ואור סביבתי
        scene.setBackground(new Color(0, 0, 0));
        scene.setAmbientLight(new AmbientLight(new Color(0, 0, 0)));

        // הגדרת הכדור האדום שבתוך הפרמידה
        Sphere sphere = new Sphere(new Point(0, 0, 5), 6);
        sphere.setEmission(new Color(255, 0, 0)); // אדום
        sphere.setMaterial(new Material()
                .setKD(0.7)
                .setKS(0.2)
                .setShininess(50));

        // נקודות בסיס הפרמידה
        Point p1 = new Point(-20, -20, 0);
        Point p2 = new Point(20, -20, 0);
        Point p3 = new Point(20, 20, 0);
        Point p4 = new Point(-20, 20, 0);
        Point top = new Point(0, 0, 40); // קודקוד למעלה

        // משולשים לפרמידה
        Triangle t1 = new Triangle(p1, p2, top);
        Triangle t2 = new Triangle(p2, p3, top);
        Triangle t3 = new Triangle(p3, p4, top);
        Triangle t4 = new Triangle(p4, p1, top);

        Color lightBlue = new Color(150, 200, 255); // תכלת שקוף
        Material pyramidMaterial = new Material()
                .setKD(0.4)
                .setKS(0.3)
                .setShininess(60)
                .setKT(0.3); // שקיפות מסוימת

        t1.setEmission(lightBlue).setMaterial(pyramidMaterial);
        t2.setEmission(lightBlue).setMaterial(pyramidMaterial);
        t3.setEmission(lightBlue).setMaterial(pyramidMaterial);
        t4.setEmission(lightBlue).setMaterial(pyramidMaterial);

        // הוספת הגיאומטריות לסצנה
        scene.geometries.add(sphere, t1, t2, t3, t4);

        scene.lights.add(new SpotLight(
                new Color(150, 100, 100),         // עוצמה מופחתת
                new Point(50, 80, 50),            // מיקום
                new Vector(-1, -1, -1))           // כיוון
                .setKl(0.01).setKq(0.001));       // דעיכה מוגברת

        Camera.Builder cameraBuilder = Camera.getBuilder()
                .setRayTracer(scene, RayTracerType.SIMPLE);

        cameraBuilder
                .setLocation(new Point(0, -100, 60))    // מצלמה מקדימה
                .setDirection(new Vector(0, 1, 0))      // מבט קדימה
                .setVpDistance(100)
                .setVpSize(100, 100)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("pyramidWithRedSphere_front");

    }
}