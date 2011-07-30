package nantes1900.tests.models;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

import junit.framework.TestCase;
import nantes1900.models.Mesh;
import nantes1900.models.Polyline;
import nantes1900.models.basis.Edge;
import nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import nantes1900.models.basis.Point;
import nantes1900.models.basis.Triangle;
import nantes1900.utils.MatrixMethod;
import nantes1900.utils.MatrixMethod.SingularMatrixException;

import org.junit.Test;

/**
 * A set of tests for the class Polyline.
 * 
 * @author Daniel Lefevre
 * 
 */
public class PolylineTest extends TestCase {

	private Point p1 = new Point(1, 0, -1);
	private Point p2 = new Point(0, 1, 0);
	private Point p3 = new Point(-1, 2, 1);
	private Point p4 = new Point(2, 2, 2);

	private Edge e1 = new Edge(p1, p2);
	private Edge e2 = new Edge(p2, p3);
	private Edge e3 = new Edge(p3, p1);
	private Edge e4 = new Edge(p1, p4);
	private Edge e5 = new Edge(p2, p4);

	private Polyline p = new Polyline();

	/**
	 * Constructor of the PolylineTest object : create a polyline by adding 3
	 * edges.
	 */
	public PolylineTest() {
		p.add(e1);
		p.add(e2);
		p.add(e3);
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#changeBase(double[][])}
	 * .
	 */
	@Test
	public void testChangeBase() {
		Point p1 = new Point(1, 0, -1);
		Point p2 = new Point(0, 1, 0);
		Point p3 = new Point(-1, 2, 1);
		Edge e1 = new Edge(p1, p2);
		Edge e2 = new Edge(p2, p3);
		Edge e3 = new Edge(p3, p1);

		Polyline pol = new Polyline();
		pol.add(e1);
		pol.add(e2);
		pol.add(e3);

		try {
			pol.changeBase(MatrixMethod.createOrthoBase(new Vector3d(1, 0, 0),
					new Vector3d(0, 1, 0), new Vector3d(0, 0, 1)));
			assertTrue(p1.equals(new Point(1, 0, -1)));
			assertTrue(p2.equals(new Point(0, 1, 0)));
			assertTrue(p3.equals(new Point(-1, 2, 1)));
		} catch (SingularMatrixException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#edgeSize()}.
	 */
	@Test
	public void testEdgeSize() {
		assertTrue(p.edgeSize() == 3);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Polyline#getCylinder(nantes1900.models.basis.Edge, double)}
	 * .
	 */
	@Test
	public void testGetCylinder() {

		Point point1 = new Point(-1, -1, 0);
		Point point2 = new Point(1, 1, 0);
		Edge e1 = new Edge(point1, point2);
		Point point3 = new Point(0, 0.7, 0);
		Point point4 = new Point(-0.5, 1, 0);
		Point point5 = new Point(1.6, 1.5, 0);
		Edge e2 = new Edge(point3, point4);
		Edge e3 = new Edge(point4, point5);

		Polyline pol = new Polyline();
		pol.add(e1);
		pol.add(e2);
		pol.add(e3);

		ArrayList<Point> list = pol.getCylinder(e1, 1);

		assertTrue(list.contains(point1));
		assertTrue(list.contains(point2));
		assertTrue(list.contains(point3));
		assertFalse(list.contains(point4));
		assertFalse(list.contains(point5));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Polyline#getNeighbours(nantes1900.models.basis.Point)}
	 * .
	 */
	@Test
	public void testGetNeighboursPoint() {
		assertTrue(p.getNeighbours(p1).contains(e1));
		assertFalse(p.getNeighbours(p1).contains(e2));
		assertTrue(p.getNeighbours(p1).contains(e3));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Polyline#getNumNeighbours(nantes1900.models.basis.Point)}
	 * .
	 */
	@Test
	public void testGetNumNeighbours() {
		assertTrue(p.getNumNeighbours(p1) == 2);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Polyline#isInCylinder2D(nantes1900.models.basis.Point, nantes1900.models.basis.Point, nantes1900.models.basis.Point, double)}
	 * .
	 */
	@Test
	public void testIsInCylinder2D() {
		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(1, 1, 0);
		Point p3 = new Point(2.2, 2, 0);
		Edge e = new Edge(p1, p2);
		double error = 0.3;
		assertTrue(e.isInInfiniteCylinder2D(p3, error));

		p3 = new Point(3, 2, 0);
		assertFalse(e.isInInfiniteCylinder2D(p3, error));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Polyline#isNeighbour(nantes1900.models.Polyline)}
	 * .
	 */
	@Test
	public void testIsNeighbour() {
		Polyline pol = new Polyline();
		pol.add(e1);
		assertTrue(p.isNeighbour(pol));
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#length()}.
	 */
	@Test
	public void testLength() {
		assertTrue(p.length() == 4.0 * Math.pow(3.0, 0.5));
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#lengthAverage()}.
	 */
	@Test
	public void testLengthAverage() {
		double d = Math.pow(4.0 / 3.0, 2) * 3.0;
		assertTrue(p.lengthAverage() == Math.pow(d, 0.5));
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#order()}.
	 */
	@Test
	public void testOrder() {
		Polyline pol = new Polyline();
		pol.add(e1);
		pol.add(e2);
		pol.add(e3);

		pol.order();
		assertTrue(pol.getEdgeList().get(0) == e3);
		assertTrue(pol.getEdgeList().get(1) == e2);
		assertTrue(pol.getEdgeList().get(2) == e1);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Polyline#orientedAs(nantes1900.models.basis.Edge, double)}
	 * .
	 */
	@Test
	public void testOrientedAs() {
		Polyline p = new Polyline();
		p.add(e1);
		p.add(e2);
		p.add(e3);
		p.add(e4);
		p.add(e5);
		Polyline pol = p.orientedAs(new Edge(new Point(0, -1, -2), new Point(1,
				1, 1)), 0.5);

		assertFalse(pol.contains(e1));
		assertFalse(pol.contains(e2));
		assertFalse(pol.contains(e3));
		assertTrue(pol.contains(e4));
		assertFalse(pol.contains(e5));
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#pointSize()}.
	 */
	@Test
	public void testPointSize() {
		assertTrue(p.pointSize() == 3);
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#refresh()}.
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

		assertTrue(pol2.contains(p1));
		assertTrue(pol2.contains(p2));
		assertFalse(pol2.contains(p3));
		assertTrue(pol2.contains(p4));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Polyline#remove(nantes1900.models.basis.Edge)}.
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
		assertTrue(pol2.contains(e2));
		assertTrue(pol2.contains(e3));
		assertTrue(pol2.contains(e4));
		assertTrue(pol2.contains(e5));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Polyline#remove(nantes1900.models.Polyline)}.
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
		assertTrue(pol2.contains(e4));
		assertTrue(pol2.contains(e5));
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#returnCentroidMesh()}.
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

		assertTrue(m.getOne().getP1() == p1 || m.getOne().getP1() == p2
				|| m.getOne().getP1().equals(centroid));
		assertTrue(m.getOne().getP2() == p1 || m.getOne().getP2() == p2
				|| m.getOne().getP2().equals(centroid));
		assertTrue(m.getOne().getP3() == p1 || m.getOne().getP3() == p2
				|| m.getOne().getP3().equals(centroid));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Polyline#returnExistingMesh(nantes1900.models.Mesh)}
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
			Triangle t1 = new Triangle(p1, p2, p3, e1, e2, e3, new Vector3d(0,
					0, 0));

			Edge e4 = new Edge(p1, p4);
			Edge e5 = new Edge(p2, p4);
			Triangle t2 = new Triangle(p1, p2, p4, e1, e4, e5, new Vector3d(0,
					0, 1));

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
			assertTrue(m.contains(t1));
			assertTrue(m.contains(t2));
		} catch (MoreThanTwoTrianglesPerEdgeException e) {
			fail();
		}
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#xAverage()}.
	 */
	@Test
	public void testXAverage() {
		assertTrue(p.xAverage() == 0);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Polyline#xBetween(double, double)}.
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
		assertTrue(p3.contains(e1));
		assertTrue(p3.contains(e2));
		assertTrue(p3.contains(e3));
		assertFalse(p3.contains(e4));
		assertFalse(p3.contains(e5));
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#xLengthAverage()}.
	 */
	@Test
	public void testXLengthAverage() {
		assertTrue(p.xLengthAverage() == 4.0 / 3.0);
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#xMax()}.
	 */
	@Test
	public void testXMax() {
		assertTrue(p.xMax() == 1);
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#xMin()}.
	 */
	@Test
	public void testXMin() {
		assertTrue(p.xMin() == -1);
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#yAverage()}.
	 */
	@Test
	public void testYAverage() {
		assertTrue(p.yAverage() == 1);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Polyline#yBetween(double, double)}.
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
		assertTrue(p3.contains(e1));
		assertFalse(p3.contains(e2));
		assertFalse(p3.contains(e3));
		assertFalse(p3.contains(e4));
		assertFalse(p3.contains(e5));
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#yLengthAverage()}.
	 */
	@Test
	public void testYLengthAverage() {
		assertTrue(p.yLengthAverage() == 4.0 / 3.0);
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#yMax()}.
	 */
	@Test
	public void testYMax() {
		assertTrue(p.yMax() == 2);
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#yMin()}.
	 */
	@Test
	public void testYMin() {
		assertTrue(p.yMin() == 0);
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#zAverage()}.
	 */
	@Test
	public void testZAverage() {
		assertTrue(p.zAverage() == 0);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Polyline#zBetween(double, double)}.
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
		assertTrue(p3.contains(e1));
		assertTrue(p3.contains(e2));
		assertTrue(p3.contains(e3));
		assertFalse(p3.contains(e4));
		assertFalse(p3.contains(e5));
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#zLengthAverage()}.
	 */
	@Test
	public void testZLengthAverage() {
		assertTrue(p.zLengthAverage() == 4.0 / 3.0);
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#zMax()}.
	 */
	@Test
	public void testZMax() {
		assertTrue(p.zMax() == 1);
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#zMaxPoint()}.
	 */
	@Test
	public void testZMaxPoint() {
		assertTrue(p.zMaxPoint() == p3);
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#zMin()}.
	 */
	@Test
	public void testZMin() {
		assertTrue(p.zMin() == -1);
	}

	/**
	 * Test method for {@link nantes1900.models.Polyline#zProjection(double)}.
	 */
	@Test
	public void testZProjection() {
		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(0, 1, 0);

		Edge e = new Edge(p1, p2);

		Polyline p = new Polyline();
		p.add(e);
		p.zProjection(1);

		assertTrue(p1.getZ() == 1);
		assertTrue(p2.getZ() == 1);
	}
}
