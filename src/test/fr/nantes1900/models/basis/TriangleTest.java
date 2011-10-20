package test.fr.nantes1900.models.basis;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.middle.Mesh;

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
public class TriangleTest extends TestCase
{

    /**
     * Test attribute.
     */
    private final Point    p1   = new Point(1, 0, -1);
    /**
     * Test attribute.
     */
    private final Point    p2   = new Point(0, 1, 0);
    /**
     * Test attribute.
     */
    private final Point    p3   = new Point(-1, 2, 1);
    /**
     * Test attribute.
     */
    private final Vector3d vect = new Vector3d(0, 0, 1);
    /**
     * Test attribute.
     */
    private final Edge     e1   = new Edge(this.p1, this.p2);
    /**
     * Test attribute.
     */
    private final Edge     e2   = new Edge(this.p2, this.p3);
    /**
     * Test attribute.
     */
    private final Edge     e3   = new Edge(this.p3, this.p1);
    /**
     * Test attribute.
     */
    private final Triangle t;

    /**
     * Constructor.
     */
    public TriangleTest()
    {
        this.t = new Triangle(this.p1, this.p2, this.p3, this.e1, this.e2,
                this.e3, this.vect);
    }

    /**
     * Test method for
     * {@link nantes1900.models.basis.Triangle#angularTolerance(javax.vecmath.Vector3d, double)}
     * . Test method for
     * {@link nantes1900.models.basis.Triangle#angularTolerance(nantes1900.models.basis.Triangle, double)}
     * .
     * @throws MoreThanTwoTrianglesPerEdgeException
     */
    @Test
    public final void testAngularTolerance()
    {
        final Vector3d vector = new Vector3d(0, 1, 0);
        final Triangle tBis = new Triangle(this.p1, this.p2, this.p3, this.e1,
                this.e2, this.e3, vector);

        Assert.assertFalse(this.t.angularTolerance(vector, 60));
        Assert.assertFalse(this.t.angularTolerance(tBis, 60));

        Assert.assertTrue(this.t.angularTolerance(vector, 100));
        Assert.assertTrue(this.t.angularTolerance(tBis, 100));
    }

    /**
     * Test method for
     * {@link nantes1900.models.basis.Triangle#contains(nantes1900.models.basis.Edge)}
     * .
     */
    @Test
    public final void testContainsEdge()
    {
        Assert.assertTrue(this.t.contains(this.e1));
        Assert.assertTrue(this.t.contains(this.e2));
        Assert.assertTrue(this.t.contains(this.e3));
    }

    /**
     * Test method for
     * {@link nantes1900.models.basis.Triangle#contains(nantes1900.models.basis.Point)}
     * .
     */
    @Test
    public final void testContainsPoint()
    {
        Assert.assertTrue(this.t.contains(this.p1));
        Assert.assertTrue(this.t.contains(this.p2));
        Assert.assertTrue(this.t.contains(this.p3));
    }

    /**
     * Test method for
     * {@link nantes1900.models.basis.Triangle#equals(java.lang.Object)}.
     */
    @Test
    public final void testEqualsObject()
    {
        final Triangle tBis = new Triangle(this.p1, this.p2, this.p3, this.e1,
                this.e2, this.e3, this.vect);
        Assert.assertTrue(this.t.equals(tBis));
        Assert.assertTrue(tBis.equals(this.t));
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#getNeighbours()}.
     */
    @Test
    public final void testGetNeighbours()
    {
        final Point p4 = new Point(3, 4, 5);
        final Point p5 = new Point(-3, -4, -5);
        final Point p6 = new Point(-3.5, -1.2, 5.9);
        final Edge e4 = new Edge(this.p1, p4);
        final Edge e5 = new Edge(this.p2, p4);
        final Edge e6 = new Edge(this.p1, p5);
        final Edge e7 = new Edge(this.p2, p5);
        final Edge e8 = new Edge(this.p1, p6);
        final Edge e9 = new Edge(this.p2, p6);

        final Triangle t2 = new Triangle(this.p1, this.p2, p4, this.e1, e4, e5,
                this.vect);
        final Triangle t3 = new Triangle(this.p1, this.p3, p5, this.e3, e6, e7,
                this.vect);
        final Triangle t4 = new Triangle(this.p2, this.p3, p6, this.e2, e8, e9,
                this.vect);

        final List<Triangle> l = this.t.getNeighbours();

        Assert.assertFalse(l.contains(this.t));
        Assert.assertTrue(l.contains(t2));
        Assert.assertTrue(l.contains(t3));
        Assert.assertTrue(l.contains(t4));
    }

    /**
     * Test method for
     * {@link nantes1900.models.basis.Triangle#getNumNeighbours()}.
     */
    @SuppressWarnings("unused")
    @Test
    public final void testGetNumNeighbours()
    {
        final Point p4 = new Point(3, 4, 5);
        final Point p5 = new Point(-3, -4, -5);
        final Point p6 = new Point(-3.5, -1.2, 5.9);
        final Edge e4 = new Edge(this.p1, p4);
        final Edge e5 = new Edge(this.p2, p4);
        final Edge e6 = new Edge(this.p1, p5);
        final Edge e7 = new Edge(this.p2, p5);
        final Edge e8 = new Edge(this.p1, p6);
        final Edge e9 = new Edge(this.p2, p6);

        new Triangle(this.p1, this.p2, p4, this.e1, e4, e5, this.vect);
        new Triangle(this.p1, this.p3, p5, this.e3, e6, e7, this.vect);
        new Triangle(this.p2, this.p3, p6, this.e2, e8, e9, this.vect);

        Assert.assertTrue(this.t.getNumNeighbours() == 3);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#getPoints()}.
     */
    @Test
    public final void testGetPoints()
    {
        final List<Point> pointList = new ArrayList<Point>(this.t.getPoints());
        Assert.assertTrue(pointList.get(0) == this.p1);
        Assert.assertTrue(pointList.get(1) == this.p2);
        Assert.assertTrue(pointList.get(2) == this.p3);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#getPoints()}.
     */
    public final static void testIsInPlanes()
    {
        final Point p11 = new Point(0, 0, 0);
        final Point p21 = new Point(0, 1, 0);
        final Point p31 = new Point(1, 0, 0);
        final Edge e11 = new Edge(p11, p21);
        final Edge e21 = new Edge(p21, p31);
        final Edge e31 = new Edge(p11, p31);
        final Vector3d normal1 = new Vector3d(0, 0, 1);

        final Point p4 = new Point(0, 0, 1);
        final Point p5 = new Point(0, 1, 1);
        final Point p6 = new Point(1, 0, 1);
        final Edge e4 = new Edge(p11, p21);
        final Edge e5 = new Edge(p21, p31);
        final Edge e6 = new Edge(p11, p31);
        final Vector3d normal2 = new Vector3d(0, 0, 1);

        final Point p7 = new Point(0, 0, 2);
        final Point p8 = new Point(0, 1, 2);
        final Point p9 = new Point(1, 0, 2);
        final Edge e7 = new Edge(p11, p21);
        final Edge e8 = new Edge(p21, p31);
        final Edge e9 = new Edge(p11, p31);
        final Vector3d normal3 = new Vector3d(0, 0, 1);

        final Triangle t1 = new Triangle(p11, p21, p31, e11, e21, e31, normal1);
        final Triangle t2 = new Triangle(p4, p5, p6, e4, e5, e6, normal2);
        final Triangle t3 = new Triangle(p7, p8, p9, e7, e8, e9, normal3);

        Assert.assertTrue(t1.isInPlanes(t2.getNormal(), t2.getP1(), 1.1));
        Assert.assertFalse(t1.isInPlanes(t3.getNormal(), t3.getP1(), 1.1));
        Assert.assertTrue(t2.isInPlanes(t3.getNormal(), t3.getP1(), 1.1));
    }

    /**
     * Test method for
     * {@link nantes1900.models.basis.Triangle#isNeighboor(nantes1900.models.basis.Triangle)}
     * .
     */
    public final void testIsNeighboor()
    {

        final Point p4 = new Point(3, 4, 5);
        final Point p5 = new Point(-3, -4, -5);
        final Point p6 = new Point(-3.5, -1.2, 5.9);
        final Edge e4 = new Edge(this.p1, p4);
        final Edge e5 = new Edge(this.p2, p4);
        final Edge e6 = new Edge(this.p1, p5);
        final Edge e7 = new Edge(this.p2, p5);
        final Edge e8 = new Edge(this.p1, p6);
        final Edge e9 = new Edge(this.p2, p6);
        final Triangle t2 = new Triangle(this.p1, this.p2, p4, this.e1, e4, e5,
                this.vect);
        final Triangle t3 = new Triangle(this.p1, this.p3, p5, this.e3, e6, e7,
                this.vect);
        final Triangle t4 = new Triangle(this.p2, this.p3, p6, this.e2, e8, e9,
                this.vect);

        Assert.assertFalse(this.t.isNeighboor(this.t));
        Assert.assertTrue(this.t.isNeighboor(t2));
        Assert.assertTrue(this.t.isNeighboor(t3));
        Assert.assertTrue(this.t.isNeighboor(t4));
    }

    /**
     * Test method for
     * {@link nantes1900.models.basis.Triangle#isNormalTo(javax.vecmath.Vector3d, double)}
     * .
     */
    @Test
    public final void testIsNormalTo()
    {
        final Vector3d vector = new Vector3d(0, 1, 0);
        final Vector3d vector2 = new Vector3d(0, 0, 1);

        Assert.assertTrue(this.t.isNormalTo(vector, 1));
        Assert.assertFalse(this.t.isNormalTo(vector2, 0.2));
    }

    /**
     * Test method for
     * {@link nantes1900.models.basis.Triangle#returnNeighbours(nantes1900.models.Mesh, nantes1900.models.Mesh)}
     * .
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             if an edge is bad formed
     */
    @Test
    public final void testReturnNeighbours()
    {
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
        final Triangle t2 = new Triangle(this.p1, this.p2, p4, this.e1, e4, e5,
                this.vect);
        final Triangle t3 = new Triangle(this.p1, this.p3, p5, this.e3, e6, e7,
                this.vect);
        final Triangle t4 = new Triangle(this.p2, this.p3, p6, this.e2, e8, e9,
                this.vect);
        final Triangle t5 = new Triangle(this.p1, p5, p6, e6, e8, e10,
                this.vect);

        final Mesh contain = new Mesh();
        contain.add(this.t);
        contain.add(t2);
        contain.add(t3);
        contain.add(t4);
        contain.add(t5);

        final Triangle t6 = new Triangle(new Point(0.05, 0.05, 0.05),
                new Point(0.05, 0.05, 0.05), new Point(0.05, 0.05, 0.05),
                new Edge(new Point(0.05, 0.05, 0.05), new Point(0.05, 0.05,
                        0.05)), new Edge(new Point(0.05, 0.05, 0.05),
                        new Point(0.05, 0.05, 0.05)), new Edge(new Point(0.05,
                        0.05, 0.05), new Point(0.05, 0.05, 0.05)), this.vect);

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
     * Test method for {@link nantes1900.models.basis.Triangle#xAverage()}.
     */
    @Test
    public final void testXAverage()
    {
        Assert.assertTrue(this.t.xAverage() == 0);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#xMax()}.
     */
    @Test
    public final void testXMax()
    {
        Assert.assertTrue(this.t.xMax() == 1);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#xMaxPoint()}.
     */
    @Test
    public final void testXMaxPoint()
    {
        Assert.assertTrue(this.t.xMaxPoint() == this.p1);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#xMin()}.
     */
    @Test
    public final void testXMin()
    {
        Assert.assertTrue(this.t.xMin() == -1);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#xMinPoint()}.
     */
    @Test
    public final void testXMinPoint()
    {
        Assert.assertTrue(this.t.xMinPoint() == this.p3);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#yAverage()}.
     */
    @Test
    public final void testYAverage()
    {
        Assert.assertTrue(this.t.yAverage() == 1);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#yMax()}.
     */
    @Test
    public final void testYMax()
    {
        Assert.assertTrue(this.t.yMax() == 2);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#yMaxPoint()}.
     */
    @Test
    public final void testYMaxPoint()
    {
        Assert.assertTrue(this.t.yMaxPoint() == this.p3);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#yMin()}.
     */
    @Test
    public final void testYMin()
    {
        Assert.assertTrue(this.t.yMin() == 0);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#yMinPoint()}.
     */
    @Test
    public final void testYMinPoint()
    {
        Assert.assertTrue(this.t.yMinPoint() == this.p1);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#zAverage()}.
     */
    @Test
    public final void testZAverage()
    {
        Assert.assertTrue(this.t.zAverage() == 0);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#zMax()}.
     */
    @Test
    public final void testZMax()
    {
        Assert.assertTrue(this.t.zMax() == 1);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#zMaxPoint()}.
     */
    @Test
    public final void testZMaxPoint()
    {
        Assert.assertTrue(this.t.zMaxPoint() == this.p3);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#zMin()}.
     */
    @Test
    public final void testZMin()
    {
        Assert.assertTrue(this.t.zMin() == -1);
    }

    /**
     * Test method for {@link nantes1900.models.basis.Triangle#zMinPoint()}.
     */
    @Test
    public final void testZMinPoint()
    {
        Assert.assertTrue(this.t.zMinPoint() == this.p1);
    }
}
