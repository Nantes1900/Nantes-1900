package test.fr.nantes1900.models;

import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.Polyline;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.utils.MatrixMethod;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;

import java.util.List;

import javax.vecmath.Vector3d;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * A set of tests for the class Polyline.
 * @author Daniel Lefevre
 */
public class PolylineTest extends TestCase {

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
    private Point p4 = new Point(2, 2, 2);

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
    private Edge e4 = new Edge(this.p1, this.p4);
    /**
     * Test attribute.
     */
    private Edge e5 = new Edge(this.p2, this.p4);

    /**
     * Test attribute.
     */
    private Polyline p = new Polyline();

    /**
     * Constructor of the PolylineTest object : create a polyline by adding 3
     * edges.
     */
    public PolylineTest() {
        this.p.add(this.e1);
        this.p.add(this.e2);
        this.p.add(this.e3);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#changeBase(double[][])} .
     */
    @Test
    public final void testChangeBase() {
        final Point point1 = new Point(1, 0, -1);
        final Point point2 = new Point(0, 1, 0);
        final Point point3 = new Point(-1, 2, 1);
        final Edge edge1 = new Edge(point1, point2);
        final Edge edge2 = new Edge(point2, point3);
        final Edge edge3 = new Edge(point3, point1);

        final Polyline pol = new Polyline();
        pol.add(edge1);
        pol.add(edge2);
        pol.add(edge3);

        try {
            pol.changeBase(MatrixMethod.createOrthoBase(new Vector3d(1, 0, 0),
                new Vector3d(0, 1, 0), new Vector3d(0, 0, 1)));
            Assert.assertTrue(point1.equals(new Point(1, 0, -1)));
            Assert.assertTrue(point2.equals(new Point(0, 1, 0)));
            Assert.assertTrue(point3.equals(new Point(-1, 2, 1)));
        } catch (SingularMatrixException e) {
            Assert.fail();
        }
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#edgeSize()}.
     */
    @Test
    public final void testEdgeSize() {
        Assert.assertTrue(this.p.edgeSize() == 3);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#getCylinder(fr.nantes1900.models.basis.Edge, double)}
     * .
     */
    @Test
    public final void testGetCylinder() {

        final Point point1 = new Point(-1, -1, 0);
        final Point point2 = new Point(1, 1, 0);
        final Edge edge1 = new Edge(point1, point2);
        final Point point3 = new Point(0, 0.7, 0);
        final Point point4 = new Point(-0.5, 1, 0);
        final Point point5 = new Point(1.6, 1.5, 0);
        final Edge edge2 = new Edge(point3, point4);
        final Edge edge3 = new Edge(point4, point5);

        final Polyline pol = new Polyline();
        pol.add(edge1);
        pol.add(edge2);
        pol.add(edge3);

        final List<Point> list = pol.getCylinder(edge1, 1);

        Assert.assertTrue(list.contains(point1));
        Assert.assertTrue(list.contains(point2));
        Assert.assertTrue(list.contains(point3));
        Assert.assertFalse(list.contains(point4));
        Assert.assertFalse(list.contains(point5));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#getNeighbours(fr.nantes1900.models.basis.Point)}
     * .
     */
    @Test
    public final void testGetNeighboursPoint() {
        Assert.assertTrue(this.p.getNeighbours(this.p1).contains(this.e1));
        Assert.assertFalse(this.p.getNeighbours(this.p1).contains(this.e2));
        Assert.assertTrue(this.p.getNeighbours(this.p1).contains(this.e3));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#getNumNeighbours(fr.nantes1900.models.basis.Point)}
     * .
     */
    @Test
    public final void testGetNumNeighbours() {
        Assert.assertTrue(this.p.getNumNeighbours(this.p1) == 2);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#isInCylinder2D(fr.nantes1900.models.basis.Point, fr.nantes1900.models.basis.Point, fr.nantes1900.models.basis.Point, double)}
     * .
     */
    @Test
    public final void testIsInCylinder2D() {
        final Point point1 = new Point(0, 0, 0);
        final Point point2 = new Point(1, 1, 0);
        final Point point3 = new Point(2.2, 2, 0);
        final Edge e = new Edge(point1, point2);
        final double error = 0.3;
        Assert.assertTrue(e.isInInfiniteCylinder2D(point3, error));

        final Point point4 = new Point(3, 2, 0);
        Assert.assertFalse(e.isInInfiniteCylinder2D(point4, error));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#isNeighbour(fr.nantes1900.models.Polyline)}
     * .
     */
    @Test
    public final void testIsNeighbour() {
        final Polyline pol = new Polyline();
        pol.add(this.e1);
        Assert.assertTrue(this.p.isNeighbour(pol));
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#length()}.
     */
    @Test
    public final void testLength() {
        Assert.assertTrue(this.p.length() == 4.0 * Math.pow(3.0, 0.5));
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#lengthAverage()}.
     */
    @Test
    public final void testLengthAverage() {
        final double d = 16.0 / 3.0;
        Assert.assertTrue(this.p.lengthAverage() == Math.sqrt(d));
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#order()}.
     */
    @Test
    public final void testOrder() {
        final Polyline pol = new Polyline();
        pol.add(this.e1);
        pol.add(this.e2);
        pol.add(this.e3);

        pol.order();
        Assert.assertTrue(pol.getEdgeList().get(0) == this.e3);
        Assert.assertTrue(pol.getEdgeList().get(1) == this.e2);
        Assert.assertTrue(pol.getEdgeList().get(2) == this.e1);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#orientedAs(fr.nantes1900.models.basis.Edge, double)}
     * .
     */
    @Test
    public void testOrientedAs() {
        final Polyline p = new Polyline();
        p.add(this.e1);
        p.add(e2);
        p.add(e3);
        p.add(e4);
        p.add(e5);
        Polyline pol =
            p.orientedAs(new Edge(new Point(0, -1, -2), new Point(1, 1, 1)),
                0.5);

        Assert.assertFalse(pol.contains(e1));
        assertFalse(pol.contains(e2));
        assertFalse(pol.contains(e3));
        Assert.assertTrue(pol.contains(e4));
        assertFalse(pol.contains(e5));
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#pointSize()}.
     */
    @Test
    public void testPointSize() {
        Assert.assertTrue(p.pointSize() == 3);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#refresh()}.
     */
    @Test
    public void testRefresh() {
        Polyline pol2 = new Polyline();
        pol2.add(e1);
        pol2.add(e2);
        pol2.add(e3);
        pol2.add(e4);
        pol2.add(e5);
        pol2.getEdgeList().remove(e2);
        pol2.getEdgeList().remove(e3);

        pol2.refresh();

        Assert.assertTrue(pol2.contains(p1));
        Assert.assertTrue(pol2.contains(p2));
        assertFalse(pol2.contains(p3));
        Assert.assertTrue(pol2.contains(p4));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#remove(fr.nantes1900.models.basis.Edge)}
     * .
     */
    @Test
    public void testRemoveEdge() {
        Polyline pol2 = new Polyline();
        pol2.add(e1);
        pol2.add(e2);
        pol2.add(e3);
        pol2.add(e4);
        pol2.add(e5);
        pol2.remove(e1);
        assertFalse(pol2.contains(e1));
        Assert.assertTrue(pol2.contains(e2));
        Assert.assertTrue(pol2.contains(e3));
        Assert.assertTrue(pol2.contains(e4));
        Assert.assertTrue(pol2.contains(e5));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#remove(fr.nantes1900.models.Polyline)}
     * .
     */
    @Test
    public void testRemovePolyline() {
        Polyline pol2 = new Polyline();
        pol2.add(e1);
        pol2.add(e2);
        pol2.add(e3);
        pol2.add(e4);
        pol2.add(e5);
        pol2.remove(p);
        assertFalse(pol2.contains(e1));
        assertFalse(pol2.contains(e2));
        assertFalse(pol2.contains(e3));
        Assert.assertTrue(pol2.contains(e4));
        Assert.assertTrue(pol2.contains(e5));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#returnCentroidMesh()}.
     */
    @Test
    public void testReturnCentroidMesh() {

        Point p1 = new Point(1, 0, -1);
        Point p2 = new Point(0, 1, 0);

        Edge e1 = new Edge(p1, p2);

        Polyline p = new Polyline();
        p.add(e1);

        Mesh m = p.returnCentroidMesh();
        Point centroid = new Point(0.5, 0.5, -0.5);

        Assert.assertTrue(m.getOne().getP1() == p1 || m.getOne().getP1() == p2
            || m.getOne().getP1().equals(centroid));
        Assert.assertTrue(m.getOne().getP2() == p1 || m.getOne().getP2() == p2
            || m.getOne().getP2().equals(centroid));
        Assert.assertTrue(m.getOne().getP3() == p1 || m.getOne().getP3() == p2
            || m.getOne().getP3().equals(centroid));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#returnExistingMesh(fr.nantes1900.models.Mesh)}
     * .
     */
    @Test
    public void testReturnExistingMesh() {
        try {
            Point p1 = new Point(1, 0, -1);
            Point p2 = new Point(0, 1, 0);
            Point p3 = new Point(-1, 2, 1);
            Point p4 = new Point(2, 2, 2);

            Edge e1 = new Edge(p1, p2);
            Edge e2 = new Edge(p2, p3);
            Edge e3 = new Edge(p3, p1);
            Triangle t1 =
                new Triangle(p1, p2, p3, e1, e2, e3, new Vector3d(0, 0, 0));

            Edge e4 = new Edge(p1, p4);
            Edge e5 = new Edge(p2, p4);
            Triangle t2 =
                new Triangle(p1, p2, p4, e1, e4, e5, new Vector3d(0, 0, 1));

            Polyline p = new Polyline();
            p.add(e1);
            p.add(e2);
            p.add(e3);
            p.add(e4);
            p.add(e5);

            Mesh belongTo = new Mesh();
            belongTo.add(t1);
            belongTo.add(t2);

            Mesh m = p.returnExistingMesh(belongTo);
            Assert.assertTrue(m.contains(t1));
            Assert.assertTrue(m.contains(t2));
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            Assert.fail();
        }
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#xAverage()}.
     */
    @Test
    public void testXAverage() {
        Assert.assertTrue(p.xAverage() == 0);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#xBetween(double, double)}.
     */
    @Test
    public void testXBetween() {
        Polyline p2 = new Polyline();
        p2.add(e1);
        p2.add(e2);
        p2.add(e3);
        p2.add(e4);
        p2.add(e5);
        Polyline p3 = p2.xBetween(-1.1, 1.1);
        Assert.assertTrue(p3.contains(e1));
        Assert.assertTrue(p3.contains(e2));
        Assert.assertTrue(p3.contains(e3));
        assertFalse(p3.contains(e4));
        assertFalse(p3.contains(e5));
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#xLengthAverage()}.
     */
    @Test
    public void testXLengthAverage() {
        Assert.assertTrue(p.xLengthAverage() == 4.0 / 3.0);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#xMax()}.
     */
    @Test
    public void testXMax() {
        Assert.assertTrue(p.xMax() == 1);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#xMin()}.
     */
    @Test
    public void testXMin() {
        Assert.assertTrue(p.xMin() == -1);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#yAverage()}.
     */
    @Test
    public void testYAverage() {
        Assert.assertTrue(p.yAverage() == 1);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#yBetween(double, double)}.
     */
    @Test
    public void testYBetween() {
        Polyline p2 = new Polyline();
        p2.add(e1);
        p2.add(e2);
        p2.add(e3);
        p2.add(e4);
        p2.add(e5);
        Polyline p3 = p2.yBetween(-1.1, 1.1);
        Assert.assertTrue(p3.contains(e1));
        assertFalse(p3.contains(e2));
        assertFalse(p3.contains(e3));
        assertFalse(p3.contains(e4));
        assertFalse(p3.contains(e5));
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#yLengthAverage()}.
     */
    @Test
    public void testYLengthAverage() {
        Assert.assertTrue(p.yLengthAverage() == 4.0 / 3.0);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#yMax()}.
     */
    @Test
    public void testYMax() {
        Assert.assertTrue(p.yMax() == 2);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#yMin()}.
     */
    @Test
    public void testYMin() {
        Assert.assertTrue(p.yMin() == 0);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#zAverage()}.
     */
    @Test
    public void testZAverage() {
        Assert.assertTrue(p.zAverage() == 0);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.Polyline#zBetween(double, double)}.
     */
    @Test
    public void testZBetween() {
        Polyline p2 = new Polyline();
        p2.add(e1);
        p2.add(e2);
        p2.add(e3);
        p2.add(e4);
        p2.add(e5);
        Polyline p3 = p2.zBetween(-1.1, 1.1);
        Assert.assertTrue(p3.contains(e1));
        Assert.assertTrue(p3.contains(e2));
        Assert.assertTrue(p3.contains(e3));
        assertFalse(p3.contains(e4));
        assertFalse(p3.contains(e5));
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#zLengthAverage()}.
     */
    @Test
    public void testZLengthAverage() {
        Assert.assertTrue(p.zLengthAverage() == 4.0 / 3.0);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#zMax()}.
     */
    @Test
    public void testZMax() {
        Assert.assertTrue(p.zMax() == 1);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#zMaxPoint()}.
     */
    @Test
    public void testZMaxPoint() {
        Assert.assertTrue(p.zMaxPoint() == p3);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#zMin()}.
     */
    @Test
    public void testZMin() {
        Assert.assertTrue(p.zMin() == -1);
    }

    /**
     * Test method for {@link fr.nantes1900.models.Polyline#zProjection(double)}
     * .
     */
    @Test
    public void testZProjection() {
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(0, 1, 0);

        Edge e = new Edge(p1, p2);

        Polyline p = new Polyline();
        p.add(e);
        p.zProjection(1);

        Assert.assertTrue(p1.getZ() == 1);
        Assert.assertTrue(p2.getZ() == 1);
    }
}
