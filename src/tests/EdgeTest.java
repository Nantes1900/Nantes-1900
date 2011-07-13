package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.InvalidParameterException;

import javax.vecmath.Vector3d;

import modeles.Edge;
import modeles.Point;
import modeles.Polyline;
import modeles.Triangle;

import org.junit.Test;

/**
 * @author CFV
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
	 * Constructor of the EdgeTest class : create a polyline for the following tests.
	 */
	public EdgeTest() {
		line.add(e1);
		line.add(e2);
		line.add(e4);
		line.add(e6);
	}

	/**
	 * Test of the method returnOtherTriangle.
	 */
	@Test
	public void testReturnOtherTriangle() {
		assertTrue(e1.returnOther(t1) == t2);
		assertTrue(e1.returnOther(t2) == t1);
		assertTrue(e2.returnOther(t1) == null);
		try {
			e2.returnOther(t2);
			fail("Should return an exception !");
		} catch(InvalidParameterException e) {
			//The method is supposed to return the Exception.
		}
	}

	/**
	 * Test of the method length.
	 */
	@Test
	public void testLength() {
		assertTrue(e5.length() == 3);
	}

	/**
	 * Test of the method contains.
	 */
	@Test
	public void testContains() {
		assertTrue(e5.contains(p2));
		assertFalse(e5.contains(p1));
	}

	/**
	 * Test of the method addTriangle.
	 */
	//	@Test
	//	TODO
	//	public final void testAddTriangle() {
	//		
	//	}

	/**
	 * Test of the method isNeighboor.
	 */
	@Test
	public void testIsNeighboor() {
		assertTrue(e1.isNeighboor(e2));
		assertFalse(e3.isNeighboor(e5));
	}

	/**
	 * Test of the method equals.
	 */
	@Test
	public void testEquals() {
		Edge eTest = new Edge(p1, p2);
		assertFalse(e1.equals(e2));
		assertTrue(e1.equals(e1));
		assertTrue(e1.equals(eTest));
	}

	/**
	 * Test of the method returnNeighbours.
	 */
	//	@Test 
	//	TODO
	//	public final void testReturnNeighbours() {
	//		fail("Not yet implemented");
	//	}

	/**
	 * Test of the method getNumNeighbours.
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
		} catch(InvalidParameterException e) {}
	}

	/**
	 * Test of the method returnOtherPoint.
	 */
	@Test
	public void testReturnOtherPoint() {
		assertTrue(e1.returnOther(p1) == p2);
		assertTrue(e3.returnOther(p3) == p1);
		assertTrue(e5.returnOther(p4) == p2);
	}

	/**
	 * Test of the method returnNeighbour.
	 */
	@Test
	public void testReturnNeighbour() {
		assertTrue(e1.returnNeighbour(p1, line) == e4);
		assertTrue(e2.returnNeighbour(p2, line) == e1);
		assertTrue(e6.returnNeighbour(p3, line) == e2);
	}
}
