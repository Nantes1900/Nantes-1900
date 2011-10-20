package test.fr.nantes1900.models;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.middle.TriangleMesh;
import fr.nantes1900.models.middle.Polygone;
import fr.nantes1900.utils.MatrixMethod;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;

import java.util.List;

import javax.vecmath.Vector3d;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * A set of tests for the class Polyline.
 * 
 * @author Daniel Lefevre
 */
public class PolylineTest extends TestCase {

    /**
     * Test attribute.
     */
    private final Point p1 = new Point(1, 0, -1);
    /**
     * Test attribute.
     */
    private final Point p2 = new Point(0, 1, 0);
    /**
     * Test attribute.
     */
    private final Point p3 = new Point(-1, 2, 1);
    /**
     * Test attribute.
     */
    private final Point p4 = new Point(2, 2, 2);

    /**
     * Test attribute.
     */
    private final Edge e1 = new Edge(this.p1, this.p2);
    /**
     * Test attribute.
     */
    private final Edge e2 = new Edge(this.p2, this.p3);
    /**
     * Test attribute.
     */
    private final Edge e3 = new Edge(this.p3, this.p1);
    /**
     * Test attribute.
     */
    private final Edge e4 = new Edge(this.p1, this.p4);
    /**
     * Test attribute.
     */
    private final Edge e5 = new Edge(this.p2, this.p4);

    /**
     * Test attribute.
     */
    private final Polygone p = new Polygone();

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
     * {@link fr.nantes1900.models.middle.Polygone#changeBase(double[][])} .
     */
    @Test
    public final static void testChangeBase() {
	final Point point1 = new Point(1, 0, -1);
	final Point point2 = new Point(0, 1, 0);
	final Point point3 = new Point(-1, 2, 1);
	final Edge edge1 = new Edge(point1, point2);
	final Edge edge2 = new Edge(point2, point3);
	final Edge edge3 = new Edge(point3, point1);

	final Polygone pol = new Polygone();
	pol.add(edge1);
	pol.add(edge2);
	pol.add(edge3);

	try {
	    pol.changeBase(MatrixMethod.createOrthoBase(new Vector3d(1, 0, 0),
		    new Vector3d(0, 1, 0), new Vector3d(0, 0, 1)));
	    Assert.assertTrue(point1.equals(new Point(1, 0, -1)));
	    Assert.assertTrue(point2.equals(new Point(0, 1, 0)));
	    Assert.assertTrue(point3.equals(new Point(-1, 2, 1)));
	} catch (final SingularMatrixException e) {
	    Assert.fail();
	}
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#edgeSize()}.
     */
    @Test
    public final void testEdgeSize() {
	Assert.assertTrue(this.p.edgeSize() == 3);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#getCylinder(fr.nantes1900.models.basis.Edge, double)}
     * .
     */
    @Test
    public final static void testGetCylinder() {

	final Point point1 = new Point(-1, -1, 0);
	final Point point2 = new Point(1, 1, 0);
	final Edge edge1 = new Edge(point1, point2);
	final Point point3 = new Point(0, 0.7, 0);
	final Point point4 = new Point(-0.5, 1, 0);
	final Point point5 = new Point(1.6, 1.5, 0);
	final Edge edge2 = new Edge(point3, point4);
	final Edge edge3 = new Edge(point4, point5);

	final Polygone pol = new Polygone();
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
     * {@link fr.nantes1900.models.middle.Polygone#getNeighbours(fr.nantes1900.models.basis.Point)}
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
     * {@link fr.nantes1900.models.middle.Polygone#getNumNeighbours(fr.nantes1900.models.basis.Point)}
     * .
     */
    @Test
    public final void testGetNumNeighbours() {
	Assert.assertTrue(this.p.getNumNeighbours(this.p1) == 2);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#isInCylinder2D(fr.nantes1900.models.basis.Point, fr.nantes1900.models.basis.Point, fr.nantes1900.models.basis.Point, double)}
     * .
     */
    @Test
    public final static void testIsInCylinder2D() {
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
     * {@link fr.nantes1900.models.middle.Polygone#isNeighbour(fr.nantes1900.models.middle.Polygone)}
     * .
     */
    @Test
    public final void testIsNeighbour() {
	final Polygone pol = new Polygone();
	pol.add(this.e1);
	Assert.assertTrue(this.p.isNeighbour(pol));
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#length()}.
     */
    @Test
    public final void testLength() {
	Assert.assertTrue(this.p.length() == 4.0 * Math.pow(3.0, 0.5));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#lengthAverage()} .
     */
    @Test
    public final void testLengthAverage() {
	final double d = 16.0 / 3.0;
	Assert.assertTrue(this.p.lengthAverage() == Math.sqrt(d));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#orientedAs(fr.nantes1900.models.basis.Edge, double)}
     * .
     */
    @Test
    public final void testOrientedAs() {
	final Polygone polygone = new Polygone();
	polygone.add(this.e1);
	polygone.add(this.e2);
	polygone.add(this.e3);
	polygone.add(this.e4);
	polygone.add(this.e5);
	final Polygone pol = polygone.orientedAs(new Edge(new Point(0, -1, -2),
		new Point(1, 1, 1)), 0.5);

	Assert.assertFalse(pol.contains(this.e1));
	Assert.assertFalse(pol.contains(this.e2));
	Assert.assertFalse(pol.contains(this.e3));
	Assert.assertTrue(pol.contains(this.e4));
	Assert.assertFalse(pol.contains(this.e5));
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#pointSize()}.
     */
    @Test
    public final void testPointSize() {
	Assert.assertTrue(this.p.pointSize() == 3);
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#refresh()}.
     */
    @Test
    public final void testRefresh() {
	final Polygone pol2 = new Polygone();
	pol2.add(this.e1);
	pol2.add(this.e2);
	pol2.add(this.e3);
	pol2.add(this.e4);
	pol2.add(this.e5);
	pol2.getEdgeList().remove(this.e2);
	pol2.getEdgeList().remove(this.e3);

	pol2.refresh();

	Assert.assertTrue(pol2.contains(this.p1));
	Assert.assertTrue(pol2.contains(this.p2));
	Assert.assertFalse(pol2.contains(this.p3));
	Assert.assertTrue(pol2.contains(this.p4));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#remove(fr.nantes1900.models.basis.Edge)}
     * .
     */
    @Test
    public final void testRemoveEdge() {
	final Polygone pol2 = new Polygone();
	pol2.add(this.e1);
	pol2.add(this.e2);
	pol2.add(this.e3);
	pol2.add(this.e4);
	pol2.add(this.e5);
	pol2.remove(this.e1);
	Assert.assertFalse(pol2.contains(this.e1));
	Assert.assertTrue(pol2.contains(this.e2));
	Assert.assertTrue(pol2.contains(this.e3));
	Assert.assertTrue(pol2.contains(this.e4));
	Assert.assertTrue(pol2.contains(this.e5));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#remove(fr.nantes1900.models.middle.Polygone)}
     * .
     */
    @Test
    public final void testRemovePolyline() {
	final Polygone pol2 = new Polygone();
	pol2.add(this.e1);
	pol2.add(this.e2);
	pol2.add(this.e3);
	pol2.add(this.e4);
	pol2.add(this.e5);
	pol2.remove(this.p);
	Assert.assertFalse(pol2.contains(this.e1));
	Assert.assertFalse(pol2.contains(this.e2));
	Assert.assertFalse(pol2.contains(this.e3));
	Assert.assertTrue(pol2.contains(this.e4));
	Assert.assertTrue(pol2.contains(this.e5));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#returnCentroidMesh()} .
     */
    @Test
    public final static void testReturnCentroidMesh() {

	final Point point1 = new Point(1, 0, -1);
	final Point point2 = new Point(0, 1, 0);

	final Edge edge1 = new Edge(point1, point2);

	final Polygone polygone = new Polygone();
	polygone.add(edge1);

	TriangleMesh m;
	m = polygone.returnCentroidMesh();
	final Point centroid = new Point(0.5, 0.5, -0.5);

	Assert.assertTrue(m.getOne().getP1() == point1
		|| m.getOne().getP1() == point2
		|| m.getOne().getP1().equals(centroid));
	Assert.assertTrue(m.getOne().getP2() == point1
		|| m.getOne().getP2() == point2
		|| m.getOne().getP2().equals(centroid));
	Assert.assertTrue(m.getOne().getP3() == point1
		|| m.getOne().getP3() == point2
		|| m.getOne().getP3().equals(centroid));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#returnExistingMesh(fr.nantes1900.models.middle.TriangleMesh)}
     * .
     */
    @Test
    public final static void testReturnExistingMesh() {
	final Point point1 = new Point(1, 0, -1);
	final Point point2 = new Point(0, 1, 0);
	final Point point3 = new Point(-1, 2, 1);
	final Point point4 = new Point(2, 2, 2);

	final Edge edge1 = new Edge(point1, point2);
	final Edge edge2 = new Edge(point2, point3);
	final Edge edge3 = new Edge(point3, point1);
	final Triangle t1 = new Triangle(point1, point2, point3, edge1, edge2,
		edge3, new Vector3d(0, 0, 0));

	final Edge edge4 = new Edge(point1, point4);
	final Edge edge5 = new Edge(point2, point4);
	final Triangle t2 = new Triangle(point1, point2, point4, edge1, edge4,
		edge5, new Vector3d(0, 0, 1));

	final Polygone polygone = new Polygone();
	polygone.add(edge1);
	polygone.add(edge2);
	polygone.add(edge3);
	polygone.add(edge4);
	polygone.add(edge5);

	final TriangleMesh belongTo = new TriangleMesh();
	belongTo.add(t1);
	belongTo.add(t2);

	final TriangleMesh m = polygone.returnExistingMesh(belongTo);
	Assert.assertTrue(m.contains(t1));
	Assert.assertTrue(m.contains(t2));
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#xAverage()}.
     */
    @Test
    public final void testXAverage() {
	Assert.assertTrue(this.p.xAverage() == 0);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#xBetween(double, double)} .
     */
    @Test
    public final void testXBetween() {
	final Polygone point2 = new Polygone();
	point2.add(this.e1);
	point2.add(this.e2);
	point2.add(this.e3);
	point2.add(this.e4);
	point2.add(this.e5);
	final Polygone point3 = point2.xBetween(-1.1, 1.1);
	Assert.assertTrue(point3.contains(this.e1));
	Assert.assertTrue(point3.contains(this.e2));
	Assert.assertTrue(point3.contains(this.e3));
	Assert.assertFalse(point3.contains(this.e4));
	Assert.assertFalse(point3.contains(this.e5));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#xLengthAverage()} .
     */
    @Test
    public final void testXLengthAverage() {
	Assert.assertTrue(this.p.xLengthAverage() == 4.0 / 3.0);
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#xMax()}.
     */
    @Test
    public final void testXMax() {
	Assert.assertTrue(this.p.xMax() == 1);
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#xMin()}.
     */
    @Test
    public final void testXMin() {
	Assert.assertTrue(this.p.xMin() == -1);
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#yAverage()}.
     */
    @Test
    public final void testYAverage() {
	Assert.assertTrue(this.p.yAverage() == 1);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#yBetween(double, double)} .
     */
    @Test
    public final void testYBetween() {
	final Polygone point2 = new Polygone();
	point2.add(this.e1);
	point2.add(this.e2);
	point2.add(this.e3);
	point2.add(this.e4);
	point2.add(this.e5);
	final Polygone point3 = point2.yBetween(-1.1, 1.1);
	Assert.assertTrue(point3.contains(this.e1));
	Assert.assertFalse(point3.contains(this.e2));
	Assert.assertFalse(point3.contains(this.e3));
	Assert.assertFalse(point3.contains(this.e4));
	Assert.assertFalse(point3.contains(this.e5));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#yLengthAverage()} .
     */
    @Test
    public final void testYLengthAverage() {
	Assert.assertTrue(this.p.yLengthAverage() == 4.0 / 3.0);
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#yMax()}.
     */
    @Test
    public final void testYMax() {
	Assert.assertTrue(this.p.yMax() == 2);
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#yMin()}.
     */
    @Test
    public final void testYMin() {
	Assert.assertTrue(this.p.yMin() == 0);
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#zAverage()}.
     */
    @Test
    public final void testZAverage() {
	Assert.assertTrue(this.p.zAverage() == 0);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#zBetween(double, double)} .
     */
    @Test
    public final void testZBetween() {
	final Polygone polyline2 = new Polygone();
	polyline2.add(this.e1);
	polyline2.add(this.e2);
	polyline2.add(this.e3);
	polyline2.add(this.e4);
	polyline2.add(this.e5);
	final Polygone polyline3 = polyline2.zBetween(-1.1, 1.1);
	Assert.assertTrue(polyline3.contains(this.e1));
	Assert.assertTrue(polyline3.contains(this.e2));
	Assert.assertTrue(polyline3.contains(this.e3));
	Assert.assertFalse(polyline3.contains(this.e4));
	Assert.assertFalse(polyline3.contains(this.e5));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#zLengthAverage()} .
     */
    @Test
    public final void testZLengthAverage() {
	Assert.assertTrue(this.p.zLengthAverage() == 4.0 / 3.0);
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#zMax()}.
     */
    @Test
    public final void testZMax() {
	Assert.assertTrue(this.p.zMax() == 1);
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#zMaxPoint()}.
     */
    @Test
    public final void testZMaxPoint() {
	Assert.assertTrue(this.p.zMaxPoint() == this.p3);
    }

    /**
     * Test method for {@link fr.nantes1900.models.middle.Polygone#zMin()}.
     */
    @Test
    public final void testZMin() {
	Assert.assertTrue(this.p.zMin() == -1);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.middle.Polygone#zProjection(double)} .
     */
    @Test
    public final static void testZProjection() {
	final Point point1 = new Point(0, 0, 0);
	final Point point2 = new Point(0, 1, 0);

	final Edge e = new Edge(point1, point2);

	final Polygone polygone = new Polygone();
	polygone.add(e);
	polygone.zProjection(1);

	Assert.assertTrue(point1.getZ() == 1);
	Assert.assertTrue(point2.getZ() == 1);
    }
}
