package test.nantes1900.models.basis;

import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * A set of tests for the class Triangle.
 * @author Daniel Lefevre
 */
public class TriangleTest extends TestCase {

    private Point p1 = new Point(1, 0, -1);
    private Point p2 = new Point(0, 1, 0);
    private Point p3 = new Point(-1, 2, 1);
    private Vector3d vect = new Vector3d(0, 0, 1);
    private Edge e1 = new Edge(p1, p2);
    private Edge e2 = new Edge(p2, p3);
    private Edge e3 = new Edge(p3, p1);
    Triangle t;

    public TriangleTest()
        throws MoreThanTwoTrianglesPerEdgeException {
        t = new Triangle(p1, p2, p3, e1, e2, e3, vect);
    }

    /**
     * Test method for
     * {@link nantes1900pjct.models.basis.Triangle#angularTolerance(javax.vecmath.Vector3d, double)}
     * . Test method for
     * {@link nantes1900pjct.models.basis.Triangle#angularTolerance(nantes1900pjct.models.basis.Triangle, double)}
     * .
     * @throws MoreThanTwoTrianglesPerEdgeException
     */
    @Test
    public void testAngularTolerance()
        throws MoreThanTwoTrianglesPerEdgeException {
        Vector3d vector = new Vector3d(0, 1, 0);
        Triangle tBis = new Triangle(p1, p2, p3, e1, e2, e3, vector);

        Assert.assertFalse(t.angularTolerance(vector, 60));
        Assert.assertFalse(t.angularTolerance(tBis, 60));

        Assert.assertTrue(t.angularTolerance(vector, 100));
        Assert.assertTrue(t.angularTolerance(tBis, 100));
    }

    /**
     * Test method for
     * {@link nantes1900pjct.models.basis.Triangle#contains(nantes1900pjct.models.basis.Edge)}
     * .
     */
    @Test
    public void testContainsEdge() {
        Assert.assertTrue(t.contains(e1));
        Assert.assertTrue(t.contains(e2));
        Assert.assertTrue(t.contains(e3));
    }

    /**
     * Test method for
     * {@link nantes1900pjct.models.basis.Triangle#contains(nantes1900pjct.models.basis.Point)}
     * .
     */
    @Test
    public void testContainsPoint() {
        Assert.assertTrue(t.contains(p1));
        Assert.assertTrue(t.contains(p2));
        Assert.assertTrue(t.contains(p3));
    }

    /**
     * Test method for
     * {@link nantes1900pjct.models.basis.Triangle#equals(java.lang.Object)}.
     * @throws MoreThanTwoTrianglesPerEdgeException
     */
    @Test
    public void testEqualsObject() throws MoreThanTwoTrianglesPerEdgeException {
        Triangle tBis = new Triangle(p1, p2, p3, e1, e2, e3, vect);
        Assert.assertTrue(t.equals(tBis));
        Assert.assertTrue(tBis.equals(t));
    }

    /**
     * Test method for
     * {@link nantes1900pjct.models.basis.Triangle#getNeighbours()}.
     */
    @Test
    public void testGetNeighbours() {
        Point p4 = new Point(3, 4, 5);
        Point p5 = new Point(-3, -4, -5);
        Point p6 = new Point(-3.5, -1.2, 5.9);
        Edge e4 = new Edge(p1, p4);
        Edge e5 = new Edge(p2, p4);
        Edge e6 = new Edge(p1, p5);
        Edge e7 = new Edge(p2, p5);
        Edge e8 = new Edge(p1, p6);
        Edge e9 = new Edge(p2, p6);
        try {

            Triangle t2 = new Triangle(p1, p2, p4, e1, e4, e5, vect);
            Triangle t3 = new Triangle(p1, p3, p5, e3, e6, e7, vect);
            Triangle t4 = new Triangle(p2, p3, p6, e2, e8, e9, vect);

            List<Triangle> l = t.getNeighbours();

            Assert.assertFalse(l.contains(t));
            Assert.assertTrue(l.contains(t2));
            Assert.assertTrue(l.contains(t3));
            Assert.assertTrue(l.contains(t4));
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            fail();
        }
    }

    /**
     * Test method for
     * {@link nantes1900pjct.models.basis.Triangle#getNumNeighbours()}.
     */
    @Test
    public void testGetNumNeighbours() {
        Point p4 = new Point(3, 4, 5);
        Point p5 = new Point(-3, -4, -5);
        Point p6 = new Point(-3.5, -1.2, 5.9);
        Edge e4 = new Edge(p1, p4);
        Edge e5 = new Edge(p2, p4);
        Edge e6 = new Edge(p1, p5);
        Edge e7 = new Edge(p2, p5);
        Edge e8 = new Edge(p1, p6);
        Edge e9 = new Edge(p2, p6);

        try {
            new Triangle(p1, p2, p4, e1, e4, e5, vect);
            new Triangle(p1, p3, p5, e3, e6, e7, vect);
            new Triangle(p2, p3, p6, e2, e8, e9, vect);

            Assert.assertTrue(t.getNumNeighbours() == 3);
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            fail();
        }
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#getPoints()}.
     */
    @Test
    public void testGetPoints() {
        final List<Point> pointList = new ArrayList<Point>(t.getPoints());
        Assert.assertTrue(pointList.get(0) == p1);
        Assert.assertTrue(pointList.get(1) == p2);
        Assert.assertTrue(pointList.get(2) == p3);
    }

    /**
     * Test method for
     * {@link nantes1900pjct.models.basis.Triangle#isNeighboor(nantes1900pjct.models.basis.Triangle)}
     * .
     * @throws MoreThanTwoTrianglesPerEdgeException
     */
    public void testIsNeighboor() throws MoreThanTwoTrianglesPerEdgeException {

        Point p4 = new Point(3, 4, 5);
        Point p5 = new Point(-3, -4, -5);
        Point p6 = new Point(-3.5, -1.2, 5.9);
        Edge e4 = new Edge(p1, p4);
        Edge e5 = new Edge(p2, p4);
        Edge e6 = new Edge(p1, p5);
        Edge e7 = new Edge(p2, p5);
        Edge e8 = new Edge(p1, p6);
        Edge e9 = new Edge(p2, p6);
        Triangle t2 = new Triangle(p1, p2, p4, e1, e4, e5, vect);
        Triangle t3 = new Triangle(p1, p3, p5, e3, e6, e7, vect);
        Triangle t4 = new Triangle(p2, p3, p6, e2, e8, e9, vect);

        Assert.assertFalse(t.isNeighboor(t));
        Assert.assertTrue(t.isNeighboor(t2));
        Assert.assertTrue(t.isNeighboor(t3));
        Assert.assertTrue(t.isNeighboor(t4));
    }

    /**
     * Test method for
     * {@link nantes1900pjct.models.basis.Triangle#isNormalTo(javax.vecmath.Vector3d, double)}
     * .
     */
    @Test
    public final void testIsNormalTo() {
        final Vector3d vector = new Vector3d(0, 1, 0);
        final Vector3d vector2 = new Vector3d(0, 0, 1);

        Assert.assertTrue(this.t.isNormalTo(vector, 1));
        Assert.assertFalse(this.t.isNormalTo(vector2, 0.2));
    }

    /**
     * Test method for
     * {@link nantes1900pjct.models.basis.Triangle#returnNeighbours(nantes1900pjct.models.Mesh, nantes1900pjct.models.Mesh)}
     * .
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             if an edge is bad formed
     */
    @Test
    public final void testReturnNeighbours()
        throws MoreThanTwoTrianglesPerEdgeException {
        final Point p4 = new Point(3, 4, 5);
        final Point p5 = new Point(-3, -4, -5);
        final Point p6 = new Point(-3.5, -1.2, 5.9);
        final Edge e4 = new Edge(this.p1, p4);
        final Edge e5 = new Edge(this.p2, p4);
        final Edge e6 = new Edge(this.p1, p5);
        final Edge e7 = new Edge(this.p2, p5);
        final Edge e8 = new Edge(this.p1, p6);
        final Edge e9 = new Edge(this.p2, p6);
        final Edge e10 = new Edge(p5, p6);
        final Triangle t2 =
            new Triangle(this.p1, this.p2, p4, this.e1, e4, e5, this.vect);
        final Triangle t3 =
            new Triangle(this.p1, this.p3, p5, this.e3, e6, e7, this.vect);
        final Triangle t4 =
            new Triangle(this.p2, this.p3, p6, this.e2, e8, e9, this.vect);
        final Triangle t5 =
            new Triangle(this.p1, p5, p6, e6, e8, e10, this.vect);

        final Mesh contain = new Mesh();
        contain.add(this.t);
        contain.add(t2);
        contain.add(t3);
        contain.add(t4);
        contain.add(t5);

        final Triangle t6 =
            new Triangle(new Point(0.05, 0.05, 0.05), new Point(0.05,
                0.05, 0.05), new Point(0.05, 0.05, 0.05), new Edge(new Point(
                0.05, 0.05, 0.05), new Point(0.05, 0.05, 0.05)), new Edge(
                new Point(0.05, 0.05, 0.05), new Point(0.05, 0.05, 0.05)),
                new Edge(new Point(0.05, 0.05, 0.05), new Point(0.05, 0.05,
                    0.05)), this.vect);

        final Mesh ret = new Mesh();
        this.t.returnNeighbours(ret, contain);

        Assert.assertTrue(ret.contains(this.t));
        Assert.assertTrue(ret.contains(t2));
        Assert.assertTrue(ret.contains(t3));
        Assert.assertTrue(ret.contains(t4));
        Assert.assertTrue(ret.contains(t5));
        Assert.assertFalse(ret.contains(t6));
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#xAverage()}.
     */
    @Test
    public final void testXAverage() {
        Assert.assertTrue(this.t.xAverage() == 0);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#xMax()}.
     */
    @Test
    public final void testXMax() {
        Assert.assertTrue(this.t.xMax() == 1);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#xMaxPoint()}.
     */
    @Test
    public final void testXMaxPoint() {
        Assert.assertTrue(this.t.xMaxPoint() == this.p1);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#xMin()}.
     */
    @Test
    public final void testXMin() {
        Assert.assertTrue(this.t.xMin() == -1);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#xMinPoint()}.
     */
    @Test
    public final void testXMinPoint() {
        Assert.assertTrue(this.t.xMinPoint() == this.p3);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#yAverage()}.
     */
    @Test
    public final void testYAverage() {
        Assert.assertTrue(this.t.yAverage() == 1);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#yMax()}.
     */
    @Test
    public final void testYMax() {
        Assert.assertTrue(this.t.yMax() == 2);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#yMaxPoint()}.
     */
    @Test
    public final void testYMaxPoint() {
        Assert.assertTrue(this.t.yMaxPoint() == this.p3);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#yMin()}.
     */
    @Test
    public final void testYMin() {
        Assert.assertTrue(this.t.yMin() == 0);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#yMinPoint()}.
     */
    @Test
    public final void testYMinPoint() {
        Assert.assertTrue(this.t.yMinPoint() == this.p1);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#zAverage()}.
     */
    @Test
    public final void testZAverage() {
        Assert.assertTrue(this.t.zAverage() == 0);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#zMax()}.
     */
    @Test
    public final void testZMax() {
        Assert.assertTrue(this.t.zMax() == 1);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#zMaxPoint()}.
     */
    @Test
    public final void testZMaxPoint() {
        Assert.assertTrue(this.t.zMaxPoint() == this.p3);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#zMin()}.
     */
    @Test
    public final void testZMin() {
        Assert.assertTrue(this.t.zMin() == -1);
    }

    /**
     * Test method for {@link nantes1900pjct.models.basis.Triangle#zMinPoint()}.
     */
    @Test
    public final void testZMinPoint() {
        Assert.assertTrue(this.t.zMinPoint() == this.p1);
    }
}
