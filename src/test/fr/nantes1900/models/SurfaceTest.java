/**
 *
 */
package test.fr.nantes1900.models;

import fr.nantes1900.models.Polyline;
import fr.nantes1900.models.Surface;
import fr.nantes1900.models.Surface.ImpossibleNeighboursOrderException;
import fr.nantes1900.models.Surface.InvalidSurfaceException;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector3d;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author Daniel Lefevre
 */
public class SurfaceTest extends TestCase {

    /**
     * Test method for
     * {@link fr.nantes1900.models.Mesh#findEdges(java.util.ArrayList, java.util.HashMap, java.util.HashMap, javax.vecmath.Vector3d)}
     * .
     */
    @Test
    public final void testFindEdges() {
        try {
            final Point point1 = new Point(0, 1, 0);
            final Edge edge1 = new Edge(point1, point1);
            final Vector3d vector1 = new Vector3d(0, 1, 0);
            final Surface m1 = new Surface();
            m1.add(new Triangle(point1, point1, point1, edge1, edge1, edge1,
                vector1));

            final Point point2 = new Point(1, 0, 0);
            final Edge edge2 = new Edge(point2, point2);
            final Vector3d vector2 = new Vector3d(1, 0, 0);
            final Surface m2 = new Surface();
            m2.add(new Triangle(point2, point2, point2, edge2, edge2, edge2,
                vector2));

            final Point point3 = new Point(0, -1, 0);
            final Edge edge3 = new Edge(point3, point3);
            final Vector3d vect3 = new Vector3d(0, -1, 0);
            final Surface m3 = new Surface();
            m3.add(new Triangle(point3, point3, point3, edge3, edge3, edge3,
                vect3));

            final Point point4 = new Point(-1, 0, 0);
            final Edge edge4 = new Edge(point4, point4);
            final Vector3d vect4 = new Vector3d(-1, 0, 0);
            final Surface m4 = new Surface();
            m4.add(new Triangle(point4, point4, point4, edge4, edge4, edge4,
                vect4));

            final Point point5 = new Point(0, 0, 0);
            final Edge edge5 = new Edge(point5, point5);
            final Vector3d vect5 = new Vector3d(0, 0, 1);
            final Surface m5 = new Surface();
            m5.add(new Triangle(point5, point5, point5, edge5, edge5, edge5,
                vect5));

            m1.addNeighbour(m2);
            m1.addNeighbour(m4);
            m3.addNeighbour(m2);
            m3.addNeighbour(m4);

            m5.addNeighbour(m1);
            m5.addNeighbour(m2);
            m5.addNeighbour(m3);
            m5.addNeighbour(m4);

            final List<Surface> wallList = new ArrayList<Surface>();

            final Map<Point, Point> pointMap = new HashMap<Point, Point>();

            final Polyline p =
                m5.findEdges(wallList, pointMap, new Vector3d(0, 0, 1));

            Assert.assertTrue(p.edgeSize() == 4);
            Assert.assertTrue(p.pointSize() == 4);
            for (final Point point : p.getPointList()) {
                Assert.assertTrue(point.getZ() == 0);
            }
        } catch (final InvalidSurfaceException e) {
            Assert.fail();
        }
    }

    @Test
    public final void testOrderNeighbours() {
        final Vector3d normal = new Vector3d(0, 0, 1);
        try {
            final Point p1 = new Point(1, 1, 0);
            final Edge e1 = new Edge(p1, p1);
            final Triangle t1 = new Triangle(p1, p1, p1, e1, e1, e1, normal);
            final Point p2 = new Point(2, 2, 0);
            final Edge e2 = new Edge(p1, p1);
            final Triangle t2 = new Triangle(p2, p2, p2, e2, e2, e2, normal);
            final Point p3 = new Point(2, 1, 0);
            final Edge e3 = new Edge(p1, p1);
            final Triangle t3 = new Triangle(p3, p3, p3, e3, e3, e3, normal);
            final Point p4 = new Point(1, 0, 0);
            final Edge e4 = new Edge(p1, p1);
            final Triangle t4 = new Triangle(p4, p4, p4, e4, e4, e4, normal);
            final Point p5 = new Point(0, 0, 0);
            final Edge e5 = new Edge(p1, p1);
            final Triangle t5 = new Triangle(p5, p5, p5, e5, e5, e5, normal);
            final Point p6 = new Point(-1, 1, 0);
            final Edge e6 = new Edge(p1, p1);
            final Triangle t6 = new Triangle(p6, p6, p6, e6, e6, e6, normal);
            final Point p7 = new Point(0, 1, 0);
            final Edge e7 = new Edge(p1, p1);
            final Triangle t7 = new Triangle(p7, p7, p7, e7, e7, e7, normal);

            final Surface s1 = new Surface();
            s1.add(t1);
            final Surface s2 = new Surface();
            s2.add(t2);
            final Surface s3 = new Surface();
            s3.add(t3);
            final Surface s4 = new Surface();
            s4.add(t4);
            final Surface s5 = new Surface();
            s5.add(t5);
            final Surface s6 = new Surface();
            s6.add(t6);
            final Surface s7 = new Surface();
            s7.add(t7);

            s1.addNeighbour(s7);
            s1.addNeighbour(s2);
            s1.addNeighbour(s5);
            s1.addNeighbour(s3);
            s1.addNeighbour(s6);
            s1.addNeighbour(s4);

            s2.addNeighbour(s3);
            s2.addNeighbour(s7);
            s3.addNeighbour(s4);

            // We introduce a mistake : s4 is not for the moment neighbour to
            // s5.

            // Other mistake : s5 is not yet neighbour to s6.

            s6.addNeighbour(s7);

            final List<Surface> wholeList = new ArrayList<Surface>();
            wholeList.add(s1);
            wholeList.add(s2);
            wholeList.add(s3);
            wholeList.add(s4);
            wholeList.add(s5);
            wholeList.add(s6);
            wholeList.add(s7);

            final Surface floors = new Surface();

            s1.orderNeighbours(wholeList, floors);

            Assert.assertTrue(s1.getNeighbours().get(0) == s7);
            Assert.assertTrue(s1.getNeighbours().get(1) == s2);
            Assert.assertTrue(s1.getNeighbours().get(2) == s3);
            Assert.assertTrue(s1.getNeighbours().get(3) == s4);
            Assert.assertTrue(s1.getNeighbours().get(4) == s5);
            Assert.assertTrue(s1.getNeighbours().get(5) == s6);

        } catch (final ImpossibleNeighboursOrderException e) {
            Assert.fail();
        }
    }
}
