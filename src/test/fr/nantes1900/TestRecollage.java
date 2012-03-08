package test.fr.nantes1900;

import java.util.ArrayList;
import java.util.List;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.recollage.Recollage;

public class TestRecollage {

    public static void main(String[] args) {
        {
            // TODO : put that in TestPoint.class
            Point p = new Point(0, 0, 0);
            Point p1 = new Point(1.45, 1, 0);
            Point p2 = new Point(1.95, -1.5, 0);
            Point p3 = new Point(-0.441, 1.001, 0);
            Point p4 = new Point(2.5, 0, 0);

            List<Point> list = new ArrayList<>();
            list.add(p1);
            list.add(p2);
            list.add(p3);
            list.add(p4);

            System.out.println("First test : \n" + p.getCloser(list));
        }

        // ###############################################
        //
        // Other test
        //
        // ###############################################
        {
            Point p1 = new Point(0, 0, 0);
            Point p2 = new Point(0, 1, 0);
            Point p3 = new Point(0.5, 1, 0);
            Point p4 = new Point(0.5, 0.5, 0);
            Point p5 = new Point(1, 0.5, 0);
            Point p6 = new Point(1, 0, 0);

            Edge e1 = new Edge(p1, p2);
            Edge e2 = new Edge(p2, p3);
            Edge e3 = new Edge(p3, p4);
            Edge e4 = new Edge(p4, p5);
            Edge e5 = new Edge(p5, p6);
            Edge e6 = new Edge(p6, p1);

            List<Edge> edges = new ArrayList<>();
            edges.add(e1);
            edges.add(e2);
            edges.add(e3);
            edges.add(e4);
            edges.add(e5);
            edges.add(e6);

            Point p = new Point(-1, 0.5, 0);

            System.out.println("Second test : \n"
                    + Recollage.getCloserProjectedPointOnEdge(p, edges));
            System.out.println("Should display 0, 0.5, 0");
        }

        // ###############################################
        //
        // Other test
        //
        // ###############################################
        {
            Point p1 = new Point(0, 0, 0);
            Point p2 = new Point(0, 1, 0);
            Point p3 = new Point(0.5, 1, 0);
            Point p4 = new Point(0.5, 0.5, 0);
            Point p5 = new Point(1, 0.5, 0);
            Point p6 = new Point(1, 0, 0);

            Edge e1 = new Edge(p1, p2);
            Edge e2 = new Edge(p2, p3);
            Edge e3 = new Edge(p3, p4);
            Edge e4 = new Edge(p4, p5);
            Edge e5 = new Edge(p5, p6);
            Edge e6 = new Edge(p6, p1);

            List<Edge> edges = new ArrayList<>();
            edges.add(e1);
            edges.add(e2);
            edges.add(e3);
            edges.add(e4);
            edges.add(e5);
            edges.add(e6);

            Polygon polygon = new Polygon(edges);

            // TODO : put that in PolygonTest.class
            System.out.println("Third test : \n" + polygon.getDownEdge());
            System.out
                    .println("Should display : (0, 0, 0), (1, 0, 0) or (1, 0, 0), (0, 0, 0)");
        }
    }
}
