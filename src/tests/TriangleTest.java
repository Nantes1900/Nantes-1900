package tests;

import javax.vecmath.Vector3d;

import junit.framework.TestCase;
import modeles.Edge;
import modeles.Point;
import modeles.Triangle;


public class TriangleTest extends TestCase {

	public TriangleTest(String testName) {
		super(testName);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testConstructor() {
		Point p1 = new Point(1, 0, 0);
		Point p2 = new Point(0, 1, 0);
		Point p3 = new Point(0, 0, 1);
		Vector3d vect = new Vector3d();
		Edge e1 = new Edge(p1, p2);
		Edge e2 = new Edge(p2, p3);
		Edge e3 = new Edge(p3, p1);
		Triangle t = new Triangle(p1, p2, p3, e1, e2, e3, vect);
		assertTrue(t.getP2() == p1);
		assertTrue(t.getP3() == p2);
		assertTrue(t.getP3() == p3);
		assertTrue(t.getE1() == e1);
		assertTrue(t.getE2() == e2);
		assertTrue(t.getE3() == e3);
		assertTrue(t.getNormal() == vect);
	}

	public void testCopyConstructor() {
		Point p1 = new Point(1, 0, 0);
		Point p2 = new Point(0, 1, 0);
		Point p3 = new Point(0, 0, 1);
		Vector3d vect = new Vector3d();
		Edge e1 = new Edge(p1, p2);
		Edge e2 = new Edge(p2, p3);
		Edge e3 = new Edge(p3, p1);
		Triangle t = new Triangle(p1, p2, p3, e1, e2, e3, vect);
		Triangle tCopy = new Triangle(t);
		
		assertTrue(tCopy.getP2() == p1);
		assertTrue(tCopy.getP3() == p2);
		assertTrue(tCopy.getP3() == p3);
		assertTrue(tCopy.getE1() == e1);
		assertTrue(tCopy.getE2() == e2);
		assertTrue(tCopy.getE3() == e3);
		assertTrue(tCopy.getNormal() == vect);
	}
	
	
}