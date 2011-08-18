/**
 *
 */
package test.fr.nantes1900.models;

import fr.nantes1900.models.Polyline;
import fr.nantes1900.models.Surface;
import fr.nantes1900.models.Surface.InvalidSurfaceException;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
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
    // TODO : check this method...
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
            final Map<Edge, Edge> edgeMap = new HashMap<Edge, Edge>();

            final Polyline p =
                m5.findEdges(wallList, pointMap, edgeMap, new Vector3d(0, 0, 1));

            Assert.assertTrue(p.edgeSize() == 4);
            Assert.assertTrue(p.pointSize() == 4);
            for (Point point : p.getPointList()) {
                Assert.assertTrue(point.getZ() == 0);
            }
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            Assert.fail();
        } catch (InvalidSurfaceException e) {
            Assert.fail();
        }
    }
}
