package tests.models.basis;

import static org.junit.Assert.*;

import java.security.InvalidParameterException;

import javax.vecmath.Vector3d;

import modeles.basis.Edge;
import modeles.basis.Point;
import modeles.basis.Polyline;
import modeles.basis.Triangle;

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
	 * Test method for {@link modeles.basis.Edge#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.basis.Edge#Edge(modeles.basis.Point, modeles.basis.Point)}.
	 */
	@Test
	public void testEdge() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.basis.Edge#getTriangleList()}.
	 */
	@Test
	public void testGetTriangleList() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.basis.Edge#returnOther(modeles.basis.Triangle)}.
	 */
	@Test
	public void testReturnOtherTriangle() {
		assertTrue(e1.returnOther(t1) == t2);
		assertTrue(e1.returnOther(t2) == t1);
		assertTrue(e2.returnOther(t1) == null);
		try {
			e2.returnOther(t2);
			fail("Should return an exception !");
		} catch (InvalidParameterException e) {
			// The method is supposed to return the Exception.
		}
	}

	/**
	 * Test method for {@link modeles.basis.Edge#getP1()}.
	 */
	@Test
	public void testGetP1() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.basis.Edge#getP2()}.
	 */
	@Test
	public void testGetP2() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.basis.Edge#getPoints()}.
	 */
	@Test
	public void testGetPoints() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.basis.Edge#length()}.
	 */
	@Test
	public void testLength() {
		assertTrue(e5.length() == 3);
	}

	/**
	 * Test method for {@link modeles.basis.Edge#contains(modeles.basis.Point)}.
	 */
	@Test
	public void testContains() {
		assertTrue(e5.contains(p2));
		assertFalse(e5.contains(p1));
	}

	/**
	 * Test method for {@link modeles.basis.Edge#addTriangle(modeles.basis.Triangle)}.
	 */
	@Test
	public void testAddTriangle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.basis.Edge#getNumberTriangles()}.
	 */
	@Test
	public void testGetNumberTriangles() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.basis.Edge#isNeighboor(modeles.basis.Edge)}.
	 */
	@Test
	public void testIsNeighboor() {
		assertTrue(e1.isNeighboor(e2));
		assertFalse(e3.isNeighboor(e5));
	}

	/**
	 * Test method for {@link modeles.basis.Edge#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		Edge eTest = new Edge(p1, p2);
		assertFalse(e1.equals(e2));
		assertTrue(e1.equals(e1));
		assertTrue(e1.equals(eTest));
	}

	/**
	 * Test method for
	 * {@link modeles.basis.Edge#returnNeighbours(modeles.basis.Polyline, modeles.basis.Polyline)}
	 * .
	 */
	@Test
	public void testReturnNeighbours() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.basis.Edge#getNumNeighbours(modeles.basis.Polyline)}.
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
	 * Test method for {@link modeles.basis.Edge#returnOther(modeles.basis.Point)}.
	 */
	@Test
	public void testReturnOtherPoint() {
		assertTrue(e1.returnOther(p1) == p2);
		assertTrue(e3.returnOther(p3) == p1);
		assertTrue(e5.returnOther(p4) == p2);
	}

//	/**
//	 * Test method for
//	 * {@link modeles.Edge#returnNeighbour(modeles.Point, modeles.Polyline)}.
//	 */
//	@Test
//	public void testReturnNeighbour() {
//		assertTrue(e1.returnNeighbour(p1, line) == e4);
//		assertTrue(e2.returnNeighbour(p2, line) == e1);
//		assertTrue(e6.returnNeighbour(p3, line) == e2);
//	}

	/**
	 * Test method for {@link modeles.basis.Edge#compose(modeles.basis.Edge, modeles.basis.Point)}
	 * .
	 */
	@Test
	public void testCompose() {
		fail("Not yet implemented"); // TODO
	}

}
