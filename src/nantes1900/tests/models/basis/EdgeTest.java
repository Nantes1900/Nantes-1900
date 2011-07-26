package nantes1900.tests.models.basis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import nantes1900.models.Polyline;
import nantes1900.models.basis.Edge;
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
	private Triangle t1 = new Triangle(p1, p2, p3, e1, e2, e3, vect);

	private Point p4 = new Point(2, 2, 2);
	private Edge e4 = new Edge(p1, p4);
	private Edge e5 = new Edge(p2, p4);
	private Triangle t2 = new Triangle(p1, p2, p4, e1, e4, e5, vect);

	private Edge e6 = new Edge(p3, p4);
	private Polyline line = new Polyline();

	/**
	 * Constructor of the test class EdgeTest : creates a polyline for the
	 * following tests.
	 */
	public EdgeTest() {
		line.add(e1);
		line.add(e2);
		line.add(e4);
		line.add(e6);
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
}
