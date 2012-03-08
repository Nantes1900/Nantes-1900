package test.fr.nantes1900;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

public class TestContains {

    public static void main(String[] args) {
        {
            GeometryFactory g = new GeometryFactory();

            Coordinate[] coordinates = new Coordinate[]{new Coordinate(0, 0),
                    new Coordinate(0, 1), new Coordinate(0.5, 1),
                    new Coordinate(0.5, 0.5), new Coordinate(1, 0.5),
                    new Coordinate(1, 0), new Coordinate(0, 0)};

            Polygon p = new Polygon(g.createLinearRing(coordinates), null, g);

            CoordinateArraySequence cAS = new CoordinateArraySequence(
                    new Coordinate[]{new Coordinate(0.9, 0.2)});
            Point point = new Point(cAS, g);

            System.out.println(p);
            System.out.println(point);
            System.out.println(p.contains(point));
        }

        // // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Another test
        // // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        {
            GeometryFactory g = new GeometryFactory();

            Coordinate[] coordinates1 = new Coordinate[]{new Coordinate(0, 0),
                    new Coordinate(0, 1), new Coordinate(0.5, 1),
                    new Coordinate(0.5, 0.5), new Coordinate(1, 0.5),
                    new Coordinate(1, 0), new Coordinate(0, 0)};

            Polygon p1 = new Polygon(g.createLinearRing(coordinates1), null, g);

            Coordinate[] coordinates2 = new Coordinate[]{
                    new Coordinate(-1, 0.5), new Coordinate(0.6, 1),
                    new Coordinate(0.6, 0.5), new Coordinate(-1, 0.5)};
            Polygon p2 = new Polygon(g.createLinearRing(coordinates2), null, g);

            System.out.println(p1.contains(p2));
            System.out.println(p1.intersects(p2));
        }
    }
}
