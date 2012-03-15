package fr.nantes1900.models.errorcalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;

public class ErrorCalculator {

    private Mesh mesh;
    private Mesh meshDecim;

    private Map<Point, List<Triangle>> mapPLT = new HashMap<>();

    public ErrorCalculator(final Mesh init, final Mesh decim) {
        this.mesh = init;
        this.meshDecim = decim;
    }

    public final Point
            findClosestPoint(final Point p, final List<Point> points) {
        Point s1 = p.closest(points);

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

        return p.closest(list);
    }

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

    public final double ponderation(final Point p) {
        double weight = 0;
        for (Triangle t : this.mapPLT.get(p)) {
            weight += t.computeArea();
        }
        return weight;
    }
}
