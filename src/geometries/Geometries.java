package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.addAll;

/**
 * class for collection of shapes
 * @author Sagiv Maoz and Yair Elhasid
 */
public class Geometries extends Intersectable{
    private final LinkedList<Intersectable> intersectables = new LinkedList<>();

    /**
     * empty constructor
     */
    public Geometries(){}
    /**
     * constructor that get unknown amount of geometries
     */
    public Geometries(Intersectable... geometries){
        add(geometries);
    }

    /**
     * add new geometries to the list
     * @param geometries - collection of geometries
     */
    public void add(Intersectable... geometries){
        addAll(intersectables, geometries);
    }
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> intersections = new LinkedList<>();
        for (Intersectable geometry : intersectables) {
            List<Intersection> geometryIntersections = geometry.calculateIntersections(ray);
            if (geometryIntersections != null) {
                intersections.addAll(geometryIntersections);
            }
        }

        return intersections.isEmpty() ? null : intersections;
    }
}