package geometries;

import java.util.LinkedList;
import java.util.List;

import primitives.Ray;
import primitives.Point;

/**
 * The Geometries class represents a collection of intersectable geometries.
 * It extends the Intersectable class and stores a list of intersectable
 * objects. This class allows for adding intersectable objects to the list and
 * finding the intersections of a ray with the geometries in the list.
 *
 * @author Odeya and Atara
 */
public class Geometries implements Intersectable {

    /**
     * A list of intersectable objects (geometric bodies).
     */
    private final List<Intersectable> geometricBodies = new LinkedList<>();

    /**
     * Default constructor that initializes an empty list of bodies.
     */
    public Geometries() {
    }

    /**
     * Constructor that initializes the list of bodies with the given geometries.
     *
     * @param geometries the geometries to be added to the list
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds a variable number of intersectable objects to the list.
     *
     * @param geometries A variable number of intersectable objects to add to the
     *                   list.
     */
    public void add(Intersectable... geometries) {
        geometricBodies.addAll(List.of(geometries));
    }

    /**
     * Finds the intersections of a ray with all the geometries in the list.
     *
     * @param ray The ray to check for intersections.
     * @return A list of intersection points, or null if there are no
     * intersections.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = new LinkedList<>();
        for (Intersectable geometry : geometricBodies) {
            List<Point> currentIntersections = geometry.findIntersections(ray);
            if (currentIntersections != null) {
                intersections.addAll(currentIntersections);
            }
        }
        return intersections.isEmpty() ? null : intersections;
    }


}