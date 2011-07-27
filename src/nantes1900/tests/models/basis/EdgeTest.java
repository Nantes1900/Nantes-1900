package nantes1900.tests.models.basis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import nantes1900.models.Polyline;
import nantes1900.models.basis.Edge;
import nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import nantes1900.models.basis.Point;
import nantes1900.models.basis.Triangle;
import nantes1900.models.basis.Edge.BadFormedPolylineException;

import org.junit.Test;

/**
 * A set of tests for the class Edge.
 * 
 * @author Daniel Lefevre
 * 
 */
public class EdgeTest {

	private Point p1 = new Point(1, 0, -1);
	private Point p2 = new Point(0, 1, 0);
	private Point p3 = new Point(-1, 2, 1);
	private Vector3d vect = new Vector3d(0, 0, 1);
	private Edge e1 = new Edge(p1, p2);
	private Edge e2 = new Edge(p2, p3);
	private Edge e3 = new Edge(p3, p1);

	private Triangle t1;

	private Point p4 = new Point(2, 2, 2);
	private Edge e4 = new Edge(p1, p4);
	private Edge e5 = new Edge(p2, p4);

	private Triangle t2;

	private Edge e6 = new Edge(p3, p4);
	private Polyline line = new Polyline();

	/**
	 * Constructor of the test class EdgeTest : creates a polyline for the
	 * following tests.
	 * 
	 * @throws MoreThanTwoTrianglesPerEdgeException
	 *             exception if one edge has more than two triangles
	 */
	public EdgeTest() throws MoreThanTwoTrianglesPerEdgeException {
		t1 = new Triangle(p1, p2, p3, e1, e2, e3, vect);
		t2 = new Triangle(p1, p2, p4, e1, e4, e5, vect);
		line.add(e1);
		line.add(e2);
		line.add(e4);
		line.add(e6);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#addTriangle(nantes1900.models.basis.Triangle)}
	 * .
	 */
	@Test
	public void testAddTriangle() {
		Triangle t3 = null;
		try {
			e1.addTriangle(t3);
			fail();
		} catch (MoreThanTwoTrianglesPerEdgeException e) {
			assertFalse(e1.getTriangleList().contains(t3));
		}
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#compose(nantes1900.models.basis.Edge)}
	 * .
	 */
	@Test
	public void testCompose() {
		Edge e = e1.compose(e2);
		assertTrue(e.getP1() == p1);
		assertTrue(e.getP2() == p3);
	}

	/**
	 * Test method for {@link nantes1900.models.basis.Edge#computeMiddle()}.
	 */
	@Test
	public void testComputeMiddle() {
		assertTrue(e5.computeMiddle().equals(new Point(1, 1.5, 1)));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#contains(nantes1900.models.basis.Point)}
	 * .
	 */
	@Test
	public void testContains() {
		assertTrue(e5.contains(p2));
		assertFalse(e5.contains(p1));
	}

	/**
	 * Test method for {@link nantes1900.models.basis.Edge#convertToVector3d()}.
	 */
	@Test
	public void testConvertToVector3d() {
		assertTrue(e5.convertToVector3d().equals(new Vector3d(2, 1, 2)));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		Edge eTest = new Edge(p1, p2);
		assertFalse(e1.equals(e2));
		assertTrue(e1.equals(e1));
		assertTrue(e1.equals(eTest));
	}

	/**
	 * Test method for {@link nantes1900.models.basis.Edge#getNumberTriangles()}
	 * .
	 */
	@Test
	public void testGetNumberTriangles() {
		assertTrue(e1.getNumberTriangles() == 2);
		assertTrue(e5.getNumberTriangles() == 1);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#getNumNeighbours(nantes1900.models.Polyline)}
	 * .
	 */
	@Test
	public void testGetNumNeighbours() {
		Polyline p = new Polyline();
		p.add(e1);
		p.add(e2);
		p.add(e3);
		p.add(e4);
		assertTrue(e1.getNumNeighbours(p) == 3);
		assertTrue(e2.getNumNeighbours(p) == 2);
		try {
			assertTrue(e5.getNumNeighbours(p) == 0);
			fail();
		} catch (InvalidParameterException e) {
		}
	}

	/**
	 * Test method for {@link nantes1900.models.basis.Edge#getTriangleList()}
	 * and
	 * {@link nantes1900.models.basis.Edge#addTriangle(nantes1900.models.basis.Triangle)}
	 * . This test is also the test of addTriangle.
	 */
	@Test
	public void testGetTriangleList() {
		ArrayList<Triangle> list1 = e1.getTriangleList();
		ArrayList<Triangle> list2 = e2.getTriangleList();
		ArrayList<Triangle> list4 = e4.getTriangleList();

		assertTrue(list1.contains(t1));
		assertTrue(list1.contains(t2));

		assertTrue(list2.contains(t1));
		assertFalse(list2.contains(t2));

		assertTrue(list4.contains(t2));
		assertFalse(list4.contains(t1));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#isInInfiniteCylinder2D(nantes1900.models.basis.Point, double)}
	 * .
	 */
	@Test
	public void testIsInInfiniteCylinder2D() {
		Point point1 = new Point(-1, -1, 0);
		Point point2 = new Point(1, 1, 0);
		Edge e = new Edge(point1, point2);

		Point point3 = new Point(0, 0.7, 0);
		assertTrue(e.isInInfiniteCylinder2D(point3, 1));

		Point point4 = new Point(-0.5, 1, 0);
		Point point5 = new Point(1.6, 1.5, 0);
		assertFalse(e.isInInfiniteCylinder2D(point4, 1));
		assertTrue(e.isInInfiniteCylinder2D(point5, 1));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#isInCylinder3D(nantes1900.models.basis.Point, double)}
	 * .
	 */
	@Test
	public void testIsInCylinder3D() {
		Point point1 = new Point(0, 0, 0);
		Point point2 = new Point(1, 1, 1);
		Edge e = new Edge(point1, point2);

		Point point3 = new Point(0, 0.5, 0.5);
		assertTrue(e.isInCylinder3D(point3, 1));

		Point point4 = new Point(1.5, 1.5, 1.5);
		Point point5 = new Point(1, 3, 1);
		assertFalse(e.isInCylinder3D(point4, 1));
		assertFalse(e.isInCylinder3D(point5, 1));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#isInInfiniteCylinder3D(nantes1900.models.basis.Point, double)}
	 * .
	 */
	@Test
	public void testIsInInfiniteCylinder3D() {
		Point point1 = new Point(0, 0, 0);
		Point point2 = new Point(1, 1, 1);
		Edge e = new Edge(point1, point2);

		Point point3 = new Point(0, 0.5, 0.5);
		assertTrue(e.isInInfiniteCylinder3D(point3, 1));

		Point point4 = new Point(1.5, 1.5, 1.5);
		Point point5 = new Point(1, 3, 1);
		assertTrue(e.isInInfiniteCylinder3D(point4, 1));
		assertFalse(e.isInInfiniteCylinder3D(point5, 1));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#isNeighboor(nantes1900.models.basis.Edge)}
	 * .
	 */
	@Test
	public void testIsNeighboor() {
		assertTrue(e1.isNeighboor(e2));
		assertFalse(e3.isNeighboor(e5));
	}

	/**
	 * Test method for {@link nantes1900.models.basis.Edge#length()}.
	 */
	@Test
	public void testLength() {
		assertTrue(e5.length() == 3);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#orientedAs(nantes1900.models.basis.Edge, double)}
	 * .
	 */
	@Test
	public void testOrientedAs() {
		Point point0 = new Point(0, 0, 0);
		Point point1 = new Point(1, 0, 0);
		Point point2 = new Point(1, 0.1, 0);

		Edge e1 = new Edge(point0, point1);
		Edge e2 = new Edge(point2, point0);

		assertTrue(e1.orientedAs(e2, 20));
		assertFalse(e1.orientedAs(e2, 2));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#project(nantes1900.models.basis.Point)}
	 * .
	 */
	@Test
	public void testProject() {
		Point point0 = new Point(0, 0, 0);
		Point point1 = new Point(1, 0, 0);
		Point point2 = new Point(0.9, 0.1, 0);

		Edge e1 = new Edge(point0, point1);

		assertTrue(e1.project(point2).equals(new Point(0.9, 0, 0)));
	}

	/**
	 * Test method for
	 * {@link modeles.Edge#returnNeighbour(modeles.Point, modeles.Polyline)}.
	 */
	@Test
	public void testReturnLeftNeighbour() {

		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(0, 1, 0);
		Point p3 = new Point(-1, 2, 0);
		Point p4 = new Point(2, 2, 0);
		Point p5 = new Point(4, 2, 0);

		Edge edge1 = new Edge(p1, p2);
		Edge edge2 = new Edge(p1, p3);
		Edge edge3 = new Edge(p1, p4);
		Edge edge4 = new Edge(p1, p5);

		Vector3d normalFloor = new Vector3d(0, 0, 1);

		Polyline b = new Polyline();
		b.add(edge1);
		b.add(edge2);
		b.add(edge3);
		b.add(edge4);

		try {
			assertTrue(edge1.returnLeftNeighbour(b, p1, normalFloor) == edge3);
		} catch (BadFormedPolylineException e) {
			fail();
		}
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#returnNeighbour(nantes1900.models.Polyline, nantes1900.models.basis.Point)}
	 * .
	 */
	@Test
	public void testReturnNeighbour() {
		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(0, 1, 0);
		Point p3 = new Point(-1, 2, 0);

		Edge edge1 = new Edge(p1, p2);
		Edge edge2 = new Edge(p1, p3);

		Polyline b = new Polyline();
		b.add(edge1);
		b.add(edge2);

		try {
			assertTrue(edge1.returnNeighbour(b, p1) == edge2);
		} catch (BadFormedPolylineException e) {
			fail();
		}
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#returnOther(nantes1900.models.basis.Point)}
	 * .
	 */
	@Test
	public void testReturnOtherPoint() {
		assertTrue(e1.returnOther(p1) == p2);
		assertTrue(e3.returnOther(p3) == p1);
		assertTrue(e5.returnOther(p4) == p2);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#returnOther(nantes1900.models.basis.Triangle)}
	 * .
	 */
	@Test
	public void testReturnOtherTriangle() {
		assertTrue(e1.returnOther(t1) == t2);
		assertTrue(e1.returnOther(t2) == t1);
		assertTrue(e2.returnOther(t1) == null);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#sharedPoint(nantes1900.models.basis.Edge)}
	 * .
	 */
	@Test
	public void testSharedPoint() {
		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(0, 1, 0);
		Point p3 = new Point(-1, 2, 0);

		Edge edge1 = new Edge(p1, p2);
		Edge edge2 = new Edge(p1, p3);

		assertTrue(edge1.sharedPoint(edge2) == p1);
	}

}
