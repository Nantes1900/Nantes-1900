package test.nantes1900.models;

import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.Mesh.InvalidSurfaceException;
import fr.nantes1900.models.Polyline;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.utils.MatrixMethod;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector3d;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * A set of tests for the class Mesh.
 * @author Daniel Lefevre
 */
public class MeshTest extends TestCase {

    /**
     * Test attribute.
     */
    private Point p1 = new Point(1, 0, -1);
    /**
     * Test attribute.
     */
    private Point p2 = new Point(0, 1, 0);
    /**
     * Test attribute.
     */
    private Point p3 = new Point(-1, 2, 1);
    /**
     * Test attribute.
     */
    private Vector3d vect1 = new Vector3d(0, 0, 1);
    /**
     * Test attribute.
     */
    private Edge e1 = new Edge(this.p1, this.p2);
    /**
     * Test attribute.
     */
    private Edge e2 = new Edge(this.p2, this.p3);
    /**
     * Test attribute.
     */
    private Edge e3 = new Edge(this.p3, this.p1);
    /**
     * Test attribute.
     */
    private Triangle t1;

    /**
     * Test attribute.
     */
    private Point p4 = new Point(4, 5, 4);
    /**
     * Test attribute.
     */
    private Point p5 = new Point(2, -3, -3);
    /**
     * Test attribute.
     */
    private Point p6 = new Point(-2, 4, -5);
    /**
     * Test attribute.
     */
    private Vector3d vect2 = new Vector3d(1, 0, 0);
    /**
     * Test attribute.
     */
    private Edge e4 = new Edge(this.p4, this.p5);
    /**
     * Test attribute.
     */
    private Edge e5 = new Edge(this.p5, this.p6);
    /**
     * Test attribute.
     */
    private Edge e6 = new Edge(this.p6, this.p4);
    /**
     * Test attribute.
     */
    private Triangle t2;

    /**
     * Test attribute.
     */
    private Mesh m = new Mesh();

    /**
     * Constructor of the MeshTest class : creating the mesh which will be an
     * example.
     */
    public MeshTest() {
        try {
            this.t1 =
                new Triangle(this.p1, this.p2, this.p3, this.e1, this.e2,
                    this.e3, this.vect1);
            this.t2 =
                new Triangle(this.p4, this.p5, this.p6, this.e4, this.e5,
                    this.e6, this.vect2);
            this.m.add(this.t1);
            this.m.add(this.t2);
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            Assert.fail("More than two triangles per edge exception !");
        }
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#averageNormal()}.
     */
    @Test
    public final void testAverageNormal() {
        Assert.assertTrue(this.m.averageNormal().equals(
            new Vector3d(0.5, 0, 0.5)));
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#changeBase(double[][])}.
     */
    @Test
    public final void testChangeBase() {
        try {
            final Point point1 = new Point(1, 0, -1);
            final Point point2 = new Point(0, 1, 0);
            final Point point3 = new Point(-1, 2, 1);
            final Vector3d vector1 = new Vector3d(0, 0, 1);
            final Edge edge1 = new Edge(point1, point2);
            final Edge edge2 = new Edge(point2, point3);
            final Edge edge3 = new Edge(point3, point1);
            final Triangle triangle1 =
                new Triangle(point1, point2, point3, edge1, edge2, edge3,
                    vector1);

            final Point point4 = new Point(4, 5, 4);
            final Point point5 = new Point(2, -3, -3);
            final Point point6 = new Point(-2, 4, -5);
            final Vector3d vector2 = new Vector3d(1, 0, 0);
            final Edge edge4 = new Edge(point4, point5);
            final Edge edge5 = new Edge(point5, point6);
            final Edge edge6 = new Edge(point6, point4);
            final Triangle triangle2 =
                new Triangle(point4, point5, point6, edge4, edge5, edge6,
                    vector2);

            final Mesh mesh = new Mesh();
            mesh.add(triangle1);
            mesh.add(triangle2);

            mesh.changeBase(MatrixMethod.createOrthoBase(new Vector3d(1, 0, 0),
                new Vector3d(0, 1, 0), new Vector3d(0, 0, 1)));
            Assert.assertTrue(point1.equals(new Point(1, 0, -1)));
            Assert.assertTrue(point2.equals(new Point(0, 1, 0)));
            Assert.assertTrue(point3.equals(new Point(-1, 2, 1)));
        } catch (SingularMatrixException e) {
            Assert.fail();
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            Assert.fail();
        }
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Mesh#contains(fr.nantes1900.models.basis.Edge)}
     * .
     */
    @Test
    public final void testContainsEdge() {
        Assert.assertTrue(this.m.contains(this.e1));
        Assert.assertTrue(this.m.contains(this.e2));
        Assert.assertTrue(this.m.contains(this.e3));
        Assert.assertTrue(this.m.contains(this.e4));
        Assert.assertTrue(this.m.contains(this.e5));
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#faceUnderZ(double)}.
     */
    @Test
    public final void testFaceUnderZ() {
        Assert.assertTrue(this.m.faceUnderZ(2) == this.t1);
    }

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
            final Mesh m1 = new Mesh();
            m1.add(new Triangle(point1, point1, point1, edge1, edge1, edge1,
                vector1));

            final Point point2 = new Point(1, 0, 0);
            final Edge edge2 = new Edge(point2, point2);
            final Vector3d vector2 = new Vector3d(1, 0, 0);
            final Mesh m2 = new Mesh();
            m2.add(new Triangle(point2, point2, point2, edge2, edge2, edge2,
                vector2));

            final Point point3 = new Point(0, -1, 0);
            final Edge edge3 = new Edge(point3, point3);
            final Vector3d vect3 = new Vector3d(0, -1, 0);
            final Mesh m3 = new Mesh();
            m3.add(new Triangle(point3, point3, point3, edge3, edge3, edge3,
                vect3));

            final Point point4 = new Point(-1, 0, 0);
            final Edge edge4 = new Edge(point4, point4);
            final Vector3d vect4 = new Vector3d(-1, 0, 0);
            final Mesh m4 = new Mesh();
            m4.add(new Triangle(point4, point4, point4, edge4, edge4, edge4,
                vect4));

            final Point point5 = new Point(0, 0, 0);
            final Edge edge5 = new Edge(point5, point5);
            final Vector3d vect5 = new Vector3d(0, 0, 1);
            final Mesh m5 = new Mesh();
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

            final List<Mesh> wallList = new ArrayList<Mesh>();

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

    /**
     * Test method for
     * {@link fr.nantes1900.models.Mesh#intersection(fr.nantes1900.models.Mesh, fr.nantes1900.models.Mesh)}
     * .
     */
    @Test
    public final void testIntersection() {
        try {
            final Point point1 = new Point(0, 0, 0);
            final Vector3d vector1 = new Vector3d(0, 0, 1);
            final Edge edge1 = new Edge(point1, point1);
            final Triangle triangle1 =
                new Triangle(point1, point1, point1, edge1, edge1, edge1,
                    vector1);

            final Point point2 = new Point(0, 0, 0);
            final Vector3d vector2 = new Vector3d(0, 1, 0);
            final Edge edge2 = new Edge(point1, point1);
            final Triangle triangle2 =
                new Triangle(point2, point2, point2, edge2, edge2, edge2,
                    vector2);

            final Point point3 = new Point(0, 0, 0);
            final Vector3d vector3 = new Vector3d(1, 0, 0);
            final Edge edge3 = new Edge(point1, point1);
            final Triangle triangle3 =
                new Triangle(point3, point3, point3, edge3, edge3, edge3,
                    vector3);

            final Mesh m1 = new Mesh();
            m1.add(triangle1);
            final Mesh m2 = new Mesh();
            m2.add(triangle2);
            final Mesh m3 = new Mesh();
            m3.add(triangle3);

            Assert.assertTrue(m1.intersection(m2, m3)
                .equals(new Point(0, 0, 0)));

        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            Assert.fail();
        } catch (SingularMatrixException e) {
            Assert.fail();
        }
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Mesh#isNeighbour(fr.nantes1900.models.Mesh)}.
     */
    @Test
    public final void testIsNeighbour() {
        final Mesh m1 = new Mesh();
        m1.add(this.t1);
        Assert.assertTrue(m1.isNeighbour(this.m));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Mesh#isOrientedAs(fr.nantes1900.models.Mesh, double)}
     * .
     */
    @Test
    public final void testIsOrientedAs() {
        try {
            final Point point1 = new Point(0, 0, 0);
            final Vector3d vector1 = new Vector3d(0, 0, 1);
            final Edge edge1 = new Edge(point1, point1);
            final Triangle triangle1 =
                new Triangle(point1, point1, point1, edge1, edge1, edge1,
                    vector1);

            final Point point2 = new Point(0, 0, 0);
            final Vector3d vector2 = new Vector3d(0, 0.1, 1.1);
            final Edge edge2 = new Edge(point1, point1);
            final Triangle triangle2 =
                new Triangle(point2, point2, point2, edge2, edge2, edge2,
                    vector2);

            final Mesh mesh1 = new Mesh();
            mesh1.add(triangle1);
            final Mesh mesh2 = new Mesh();
            mesh2.add(triangle2);

            Assert.assertTrue(mesh1.isOrientedAs(mesh2, 15));
            Assert.assertFalse(mesh1.isOrientedAs(mesh2, 2));
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            Assert.fail();
        }
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Mesh#orientedAs(javax.vecmath.Vector3d, double)}
     * .
     */
    @Test
    public final void testOrientedAs() {
        final Mesh oriented =
            this.m.orientedAs(new Vector3d(0.1, 0.1, 0.9), 15);
        Assert.assertTrue(oriented.contains(this.t1));
        Assert.assertFalse(oriented.contains(this.t2));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Mesh#orientedNormalTo(javax.vecmath.Vector3d, double)}
     * .
     */
    @Test
    public final void testOrientedNormalTo() {
        final Mesh oriented =
            this.m.orientedNormalTo(new Vector3d(0.1, 0.1, 0.9), 0.2);
        Assert.assertTrue(oriented.contains(this.t2));
        Assert.assertFalse(oriented.contains(this.t1));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Mesh#remove(fr.nantes1900.models.Mesh)}.
     */
    @Test
    public final void testRemoveMesh() {
        final Mesh m1 = new Mesh(this.m);
        final Mesh m2 = new Mesh();
        m2.add(this.t1);

        m1.remove(m2);

        Assert.assertTrue(m1.contains(this.t2));
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#returnUnsortedBounds()}.
     */
    @Test
    public final void testReturnUnsortedBounds() {

        try {
            // We create points, but no matter their coordinates, if they are
            // not
            // equals.
            final Point point1 = new Point(1, 0, -1);
            final Point point2 = new Point(0, 1, 0);
            final Point point3 = new Point(-1, 2, 1);
            final Point point4 = new Point(1, 1, 1);
            final Point point5 = new Point(2, 2, 2);
            final Point point6 = new Point(-1, 3, 1);
            final Point point7 = new Point(-2, 1, 0);
            final Point point8 = new Point(3, 4, 5);
            final Point point9 = new Point(3, -2, -2);

            final Vector3d vector = new Vector3d(0, 0, 1);

            final Edge edge1 = new Edge(point1, point2);
            final Edge edge2 = new Edge(point2, point3);
            final Edge edge3 = new Edge(point3, point1);
            final Edge edge4 = new Edge(point1, point4);
            final Edge edge5 = new Edge(point3, point4);
            final Edge edge6 = new Edge(point4, point5);
            final Edge edge7 = new Edge(point1, point5);

            final Edge edge8 = new Edge(point1, point6);
            final Edge edge9 = new Edge(point5, point6);
            final Edge edge10 = new Edge(point6, point7);
            final Edge edge11 = new Edge(point6, point9);
            final Edge edge12 = new Edge(point7, point9);
            final Edge edge13 = new Edge(point8, point9);
            final Edge edge14 = new Edge(point7, point8);
            final Edge edge15 = new Edge(point2, point7);
            final Edge edge16 = new Edge(point1, point7);
            final Edge edge17 = new Edge(point2, point8);

            final Triangle triangle1 =
                new Triangle(point1, point2, point3, edge1, edge2, edge3,
                    vector);
            final Triangle triangle2 =
                new Triangle(point1, point3, point4, edge3, edge4, edge5,
                    vector);
            final Triangle triangle3 =
                new Triangle(point1, point4, point5, edge4, edge6, edge7,
                    vector);
            final Triangle triangle4 =
                new Triangle(point1, point5, point6, edge7, edge8, edge9,
                    vector);
            final Triangle triangle5 =
                new Triangle(point1, point6, point7, edge8, edge10, edge16,
                    vector);
            final Triangle triangle6 =
                new Triangle(point6, point7, point9, edge10, edge11, edge12,
                    vector);
            final Triangle triangle7 =
                new Triangle(point7, point8, point9, edge12, edge13, edge14,
                    vector);
            final Triangle triangle8 =
                new Triangle(point2, point7, point8, edge14, edge15, edge17,
                    vector);

            final Mesh mesh = new Mesh();
            mesh.add(triangle1);
            mesh.add(triangle2);
            mesh.add(triangle3);
            mesh.add(triangle4);
            mesh.add(triangle5);
            mesh.add(triangle6);
            mesh.add(triangle7);
            mesh.add(triangle8);

            final Polyline bounds = mesh.returnUnsortedBounds();
            Assert.assertTrue(bounds.contains(point1));
            Assert.assertTrue(bounds.contains(point2));
            Assert.assertTrue(bounds.contains(point3));
            Assert.assertTrue(bounds.contains(point4));
            Assert.assertTrue(bounds.contains(point5));
            Assert.assertTrue(bounds.contains(point6));
            Assert.assertTrue(bounds.contains(point7));
            Assert.assertTrue(bounds.contains(point8));
            Assert.assertTrue(bounds.contains(point9));

            Assert.assertTrue(bounds.contains(edge1));
            Assert.assertTrue(bounds.contains(edge2));
            Assert.assertFalse(bounds.contains(edge3));
            Assert.assertFalse(bounds.contains(edge4));
            Assert.assertTrue(bounds.contains(edge5));
            Assert.assertTrue(bounds.contains(edge6));
            Assert.assertFalse(bounds.contains(edge7));
            Assert.assertFalse(bounds.contains(edge8));
            Assert.assertTrue(bounds.contains(edge9));
            Assert.assertFalse(bounds.contains(edge10));
            Assert.assertTrue(bounds.contains(edge11));
            Assert.assertFalse(bounds.contains(edge12));
            Assert.assertTrue(bounds.contains(edge13));
            Assert.assertFalse(bounds.contains(edge14));
            Assert.assertTrue(bounds.contains(edge15));
            Assert.assertTrue(bounds.contains(edge16));
            Assert.assertTrue(bounds.contains(edge17));
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            Assert.fail();
        }
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#xAverage()}.
     */
    @Test
    public final void testXAverage() {
        Assert.assertTrue(this.m.xAverage() == 2.0 / 3.0);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Mesh#xBetween(double, double)}.
     */
    @Test
    public final void testXBetween() {
        final Mesh xBet = this.m.xBetween(10, -10);
        Assert.assertTrue(xBet.contains(this.t1));
        Assert.assertTrue(xBet.contains(this.t2));
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#xLengthAverage()}.
     */
    @Test
    public final void testXLengthAverage() {
        Assert.assertTrue(this.m.xLengthAverage() == 4);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#xMax()}.
     */
    @Test
    public final void testXMax() {
        Assert.assertTrue(this.m.xMax() == 4);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#xMin()}.
     */
    @Test
    public final void testXMin() {
        Assert.assertTrue(this.m.xMin() == -2);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#yAverage()}.
     */
    @Test
    public final void testYAverage() {
        Assert.assertTrue(this.m.yAverage() == 3.0 / 2.0);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Mesh#yBetween(double, double)}.
     */
    @Test
    public final void testYBetween() {
        final Mesh yBet = this.m.yBetween(10, -10);
        Assert.assertTrue(yBet.contains(this.t1));
        Assert.assertTrue(yBet.contains(this.t2));
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#yLengthAverage()}.
     */
    @Test
    public final void testYLengthAverage() {
        Assert.assertTrue(this.m.yLengthAverage() == 5);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#yMax()}.
     */
    @Test
    public final void testYMax() {
        Assert.assertTrue(this.m.yMax() == 5);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#yMin()}.
     */
    @Test
    public final void testYMin() {
        Assert.assertTrue(this.m.yMin() == -3);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#zAverage()}.
     */
    @Test
    public final void testZAverage() {
        Assert.assertTrue(this.m.zAverage() == -2.0 / 3.0);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Mesh#zBetween(double, double)}.
     */
    @Test
    public final void testZBetween() {
        final Mesh zBet = this.m.zBetween(10, -10);
        Assert.assertTrue(zBet.contains(this.t1));
        Assert.assertTrue(zBet.contains(this.t2));
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#zLengthAverage()}.
     */
    @Test
    public final void testZLengthAverage() {
        Assert.assertTrue(this.m.zLengthAverage() == 5.5);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#zMax()}.
     */
    @Test
    public final void testZMax() {
        Assert.assertTrue(this.m.zMax() == 4);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#zMin()}.
     */
    @Test
    public final void testZMin() {
        Assert.assertTrue(this.m.zMin() == -5);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Mesh#zMinFace()}.
     */
    @Test
    public final void testZMinFace() {
        Assert.assertTrue(m.zMinFace() == t2);
    }
}
