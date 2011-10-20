package test.fr.nantes1900.models.basis;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.middle.Polyline;
import fr.nantes1900.models.middle.Polyline.BadFormedPolylineException;

import java.security.InvalidParameterException;
import java.util.List;

import javax.vecmath.Vector3d;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * A set of tests for the class Edge.
 * @author Daniel Lefevre
 */
public class EdgeTest extends TestCase
{

    /** Test attribute. */
    private final Point    point1   = new Point(1, 0, -1);
    /** Test attribute. */
    private final Point    point2   = new Point(0, 1, 0);
    /** Test attribute. */
    private final Point    point3   = new Point(-1, 2, 1);
    /** Test attribute. */
    private final Vector3d vect     = new Vector3d(0, 0, 1);
    /** Test attribute. */
    private final Edge     edge1    = new Edge(this.point1, this.point2);
    /** Test attribute. */
    private final Edge     edge2    = new Edge(this.point2, this.point3);
    /** Test attribute. */
    private final Edge     edge3    = new Edge(this.point3, this.point1);

    /** Test attribute. */
    private final Triangle triangle1;

    /** Test attribute. */
    private final Point    point4   = new Point(2, 2, 2);
    /** Test attribute. */
    private final Edge     edge4    = new Edge(this.point1, this.point4);
    /** Test attribute. */
    private final Edge     edge5    = new Edge(this.point2, this.point4);

    /** Test attribute. */
    private final Triangle triangle2;

    /** Test attribute. */
    private final Edge     edge6    = new Edge(this.point3, this.point4);
    /** Test attribute. */
    private final Polyline polyline = new Polyline();

    /**
     * Constructor of the test class EdgeTest : creates a polyline for the
     * following tests.
     */
    public EdgeTest()
    {

        this.triangle1 = new Triangle(this.point1, this.point2, this.point3,
                this.edge1, this.edge2, this.edge3, this.vect);
        this.triangle2 = new Triangle(this.point1, this.point2, this.point4,
                this.edge1, this.edge4, this.edge5, this.vect);
        this.polyline.add(this.edge1);
        this.polyline.add(this.edge2);
        this.polyline.add(this.edge4);
        this.polyline.add(this.edge6);
    }

    /**
     * Test method for {@link fr.nantes1900.models.basis.Edge#computeMiddle()} .
     */
    @Test
    public final void testComputeMiddle()
    {
        Assert.assertTrue(this.edge5.computeMiddle().equals(
                new Point(1, 1.5, 1)));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#contains(fr.nantes1900.models.basis.Point)}
     * .
     */
    @Test
    public final void testContains()
    {
        Assert.assertTrue(this.edge5.contains(this.point2));
        Assert.assertFalse(this.edge5.contains(this.point1));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#convertToVector3d()} .
     */
    @Test
    public final void testConvertToVector3d()
    {
        Assert.assertTrue(this.edge5.convertToVector3d().equals(
                new Vector3d(2, 1, 2)));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#equals(java.lang.Object)} .
     */
    @Test
    public final void testEqualsObject()
    {
        final Edge eTest = new Edge(this.point1, this.point2);
        Assert.assertFalse(this.edge1.equals(this.edge2));
        Assert.assertEquals(this.edge1, this.edge1);
        Assert.assertEquals(this.edge1, eTest);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#getNumberTriangles()} .
     */
    @Test
    public final void testGetNumberTriangles()
    {
        Assert.assertTrue(this.edge1.getNumberTriangles() == 2);
        Assert.assertTrue(this.edge5.getNumberTriangles() == 1);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#getNumNeighbours(fr.nantes1900.models.middle.Polyline)}
     * .
     */
    @Test
    public final void testGetNumNeighbours()
    {
        final Polyline p = new Polyline();
        p.add(this.edge1);
        p.add(this.edge2);
        p.add(this.edge3);
        p.add(this.edge4);
        Assert.assertTrue(this.edge1.getNumNeighbours(p) == 3);
        Assert.assertTrue(this.edge2.getNumNeighbours(p) == 2);
        try
        {
            Assert.assertTrue(this.edge5.getNumNeighbours(p) == 0);
            Assert.fail();
        } catch (final InvalidParameterException e)
        {
            Assert.fail();
        }
    }

    /**
     * Test method for {@link fr.nantes1900.models.basis.Edge#getTriangles()}
     * and
     * {@link fr.nantes1900.models.basis.Edge#addTriangle(fr.nantes1900.models.basis.Triangle)}
     * . This test is also the test of addTriangle.
     */
    @Test
    public final void testGetTriangleList()
    {
        final List<Triangle> list1 = this.edge1.getTriangles();
        final List<Triangle> list2 = this.edge2.getTriangles();
        final List<Triangle> list4 = this.edge4.getTriangles();

        Assert.assertTrue(list1.contains(this.triangle1));
        Assert.assertTrue(list1.contains(this.triangle2));

        Assert.assertTrue(list2.contains(this.triangle1));
        Assert.assertFalse(list2.contains(this.triangle2));

        Assert.assertTrue(list4.contains(this.triangle2));
        Assert.assertFalse(list4.contains(this.triangle1));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#isInCylinder3D(fr.nantes1900.models.basis.Point, double)}
     * .
     */
    @Test
    public final static void testIsInCylinder3D()
    {
        final Point pointTest1 = new Point(0, 0, 0);
        final Point pointTest2 = new Point(1, 1, 1);
        final Edge e = new Edge(pointTest1, pointTest2);

        final Point pointTest3 = new Point(0, 0.5, 0.5);
        Assert.assertTrue(e.isInCylinder3D(pointTest3, 1));

        final Point pointTest4 = new Point(1.5, 1.5, 1.5);
        final Point pointTest5 = new Point(1, 3, 1);
        Assert.assertFalse(e.isInCylinder3D(pointTest4, 1));
        Assert.assertFalse(e.isInCylinder3D(pointTest5, 1));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#isInInfiniteCylinder2D(fr.nantes1900.models.basis.Point, double)}
     * .
     */
    @Test
    public final static void testIsInInfiniteCylinder2D()
    {
        final Point pointTest1 = new Point(-1, -1, 0);
        final Point pointTest2 = new Point(1, 1, 0);
        final Edge e = new Edge(pointTest1, pointTest2);

        final Point pointTest3 = new Point(0, 0.7, 0);
        Assert.assertTrue(e.isInInfiniteCylinder2D(pointTest3, 1));

        final Point pointTest4 = new Point(-0.5, 1, 0);
        final Point pointTest5 = new Point(1.6, 1.5, 0);
        Assert.assertFalse(e.isInInfiniteCylinder2D(pointTest4, 1));
        Assert.assertTrue(e.isInInfiniteCylinder2D(pointTest5, 1));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#isInInfiniteCylinder3D(fr.nantes1900.models.basis.Point, double)}
     * .
     */
    @Test
    public final static void testIsInInfiniteCylinder3D()
    {
        final Point pointTest1 = new Point(0, 0, 0);
        final Point pointTest2 = new Point(1, 1, 1);
        final Edge e = new Edge(pointTest1, pointTest2);

        final Point pointTest3 = new Point(0, 0.5, 0.5);
        Assert.assertTrue(e.isInInfiniteCylinder3D(pointTest3, 1));

        final Point pointTest4 = new Point(1.5, 1.5, 1.5);
        final Point pointTest5 = new Point(1, 3, 1);
        Assert.assertTrue(e.isInInfiniteCylinder3D(pointTest4, 1));
        Assert.assertFalse(e.isInInfiniteCylinder3D(pointTest5, 1));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#isNeighboor(fr.nantes1900.models.basis.Edge)}
     * .
     */
    @Test
    public final void testIsNeighboor()
    {
        Assert.assertTrue(this.edge1.isNeighboor(this.edge2));
        Assert.assertFalse(this.edge3.isNeighboor(this.edge5));
    }

    /** Test method for {@link fr.nantes1900.models.basis.Edge#length()} . */
    @Test
    public final void testLength()
    {
        Assert.assertTrue(this.edge5.length() == 3);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#orientedAs(fr.nantes1900.models.basis.Edge, double)}
     * .
     */
    @Test
    public final static void testOrientedAs()
    {
        final Point pointTest0 = new Point(0, 0, 0);
        final Point pointTest1 = new Point(1, 0, 0);
        final Point pointTest2 = new Point(1, 0.1, 0);

        final Edge e1 = new Edge(pointTest0, pointTest1);
        final Edge e2 = new Edge(pointTest2, pointTest0);

        Assert.assertTrue(e1.orientedAs(e2, 20));
        Assert.assertFalse(e1.orientedAs(e2, 2));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#project(fr.nantes1900.models.basis.Point)}
     * .
     */
    @Test
    public final static void testProject()
    {
        final Point pointTest0 = new Point(0, 0, 0);
        final Point pointTest1 = new Point(1, 0, 0);
        final Point pointTest2 = new Point(0.9, 0.1, 0);

        final Edge e1 = new Edge(pointTest0, pointTest1);

        Assert.assertTrue(e1.project(pointTest2).equals(new Point(0.9, 0, 0)));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#returnNeighbour(fr.nantes1900.models.middle.Polyline, fr.nantes1900.models.basis.Point)}
     * .
     */
    @Test
    public final static void testReturnNeighbour()
    {
        final Point p1 = new Point(0, 0, 0);
        final Point p2 = new Point(0, 1, 0);
        final Point p3 = new Point(-1, 2, 0);

        final Edge e1 = new Edge(p1, p2);
        final Edge e2 = new Edge(p1, p3);

        final Polyline b = new Polyline();
        b.add(e1);
        b.add(e2);

        try
        {
            Assert.assertTrue(e1.returnNeighbour(b, p1) == e2);
        } catch (final BadFormedPolylineException e)
        {
            Assert.fail();
        }
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#returnOther(fr.nantes1900.models.basis.Point)}
     * .
     */
    @Test
    public final void testReturnOtherPoint()
    {
        Assert.assertTrue(this.edge1.returnOther(this.point1) == this.point2);
        Assert.assertTrue(this.edge3.returnOther(this.point3) == this.point1);
        Assert.assertTrue(this.edge5.returnOther(this.point4) == this.point2);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#returnOther(fr.nantes1900.models.basis.Triangle)}
     * .
     */
    @Test
    public final void testReturnOtherTriangle()
    {
        Assert.assertTrue(this.edge1.returnOther(this.triangle1) == this.triangle2);
        Assert.assertTrue(this.edge1.returnOther(this.triangle2) == this.triangle1);
        Assert.assertTrue(this.edge2.returnOther(this.triangle1) == null);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Edge#sharedPoint(fr.nantes1900.models.basis.Edge)}
     * .
     */
    @Test
    public final static void testSharedPoint()
    {
        final Point p1 = new Point(0, 0, 0);
        final Point p2 = new Point(0, 1, 0);
        final Point p3 = new Point(-1, 2, 0);

        final Edge e1 = new Edge(p1, p2);
        final Edge e2 = new Edge(p1, p3);

        Assert.assertTrue(e1.sharedPoint(e2) == p1);
    }

}
