package fr.nantes1900.models.errorcalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;

/**
 * Implements a calculator of the error between two meshes.
 * @author Daniel Lefevre
 */
public class ErrorCalculator {

    /**
     * The mesh before simplification.
     */
    private Mesh mesh;
    /**
     * The mesh after simplification.
     */
    private Mesh meshDecim;

    /**
     * The map associating each point with the triangles it belongs to.
     */
    private Map<Point, List<Triangle>> mapPLT = new HashMap<>();

    /**
     * Constructor.
     * @param init
     *            the mesh before simplification
     * @param decim
     *            the mesh after
     */
    public ErrorCalculator(final Mesh init, final Mesh decim) {
        this.mesh = init;
        this.meshDecim = decim;
    }

    /**
     * Searches for the point in points which is the closest from p.
     * @param p
     *            the point
     * @param points
     *            the list of points
     * @return the point from points which is the closest from p
     */
    public final Point
            findClosestPoint(final Point p, final List<Point> points) {
        Point s1 = p.getCloser(points);

        List<Point> list = new ArrayList<>();

        for (Triangle t : this.mapPLT.get(p)) {
            Point proj = t.project(s1);
            if (t.isInside(proj)) {
                list.add(s1);
            }
        }

        if (list.isEmpty()) {
            return s1;
        }

        return p.getCloser(list);
    }

    /**
     * Computes the error of all triangles, following the algorithm.
     * @return the error
     */
    public final double computeError() {
        for (Triangle t : this.meshDecim) {
            for (Point p : t.getPoints()) {
                List<Triangle> l = this.mapPLT.get(p);
                if (l == null) {
                    l = new ArrayList<>(2);
                    this.mapPLT.put(p, l);
                }
                if (!l.contains(t)) {
                    l.add(t);
                }
            }
        }

        List<Point> points = this.mesh.getPoints();
        List<Point> pointsDecim = this.meshDecim.getPoints();

        double error = 0;

        // Algorithm.
        for (Point p : pointsDecim) {
            error += p.distance(findClosestPoint(p, points)) / ponderation(p);
        }

        return error;
    }

    /**
     * Adds all the areas of the triangles which contains p.
     * @param p
     *            the point
     * @return the sum
     */
    public final double ponderation(final Point p) {
        double weight = 0;
        for (Triangle t : this.mapPLT.get(p)) {
            weight += t.computeArea();
        }
        return weight;
    }
}
