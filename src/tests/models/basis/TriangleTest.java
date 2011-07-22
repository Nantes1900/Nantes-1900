package tests.models.basis;

import static org.junit.Assert.*;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

import models.Mesh;
import models.basis.Edge;
import models.basis.Point;
import models.basis.Triangle;

import org.junit.Test;

/**
 * A set of tests for the class Triangle.
 * 
 * @author Daniel Lefevre
 */
public class TriangleTest {

	private Point p1 = new Point(1, 0, -1);
	private Point p2 = new Point(0, 1, 0);
	private Point p3 = new Point(-1, 2, 1);
	private Vector3d vect = new Vector3d(0, 0, 1);
	private Edge e1 = new Edge(p1, p2);
	private Edge e2 = new Edge(p2, p3);
	private Edge e3 = new Edge(p3, p1);
	Triangle t = new Triangle(p1, p2, p3, e1, e2, e3, vect);

	/**
	 * Test method for {@link models.basis.Triangle#getPoints()}.
	 */
	@Test
	public void testGetPoints() {
		ArrayList<Point> pointList = new ArrayList<Point>(t.getPoints());
		assertTrue(pointList.get(0) == p1);
		assertTrue(pointList.get(1) == p2);
		assertTrue(pointList.get(2) == p3);
	}

	/**
	 * Test method for {@link models.basis.Triangle#contains(models.basis.Point)}.
	 */
	@Test
	public void testContains() {
		assertTrue(t.contains(p1));
		assertTrue(t.contains(p2));
		assertTrue(t.contains(p3));
	}

	/**
	 * Test method for {@link models.basis.Triangle#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		Triangle tBis = new Triangle(p1, p2, p3, e1, e2, e3, vect);
		assertTrue(t.equals(tBis));
		assertTrue(tBis.equals(t));
	}

	/**
	 * Test method for {@link models.basis.Triangle#xAverage()}.
	 */
	@Test
	public void testXAverage() {
		assertTrue(t.xAverage() == 0);
	}

	/**
	 * Test method for {@link models.basis.Triangle#yAverage()}.
	 */
	@Test
	public void testYAverage() {
		assertTrue(t.yAverage() == 1);
	}

	/**
	 * Test method for {@link models.basis.Triangle#zAverage()}.
	 */
	@Test
	public void testZAverage() {
		assertTrue(t.zAverage() == 0);
	}

	/**
	 * Test method for {@link models.basis.Triangle#xMin()}.
	 */
	@Test
	public void testXMin() {
		assertTrue(t.xMin() == -1);
	}

	/**
	 * Test method for {@link models.basis.Triangle#xMax()}.
	 */
	@Test
	public void testXMax() {
		assertTrue(t.xMax() == 1);
	}

	/**
	 * Test method for {@link models.basis.Triangle#yMin()}.
	 */
	@Test
	public void testYMin() {
		assertTrue(t.yMin() == 0);
	}

	/**
	 * Test method for {@link models.basis.Triangle#yMax()}.
	 */
	@Test
	public void testYMax() {
		assertTrue(t.yMax() == 2);
	}

	/**
	 * Test method for {@link models.basis.Triangle#zMin()}.
	 */
	@Test
	public void testZMin() {
		assertTrue(t.zMin() == -1);
	}

	/**
	 * Test method for {@link models.basis.Triangle#zMax()}.
	 */
	@Test
	public void testZMax() {
		assertTrue(t.zMax() == 1);
	}

	/**
	 * Test method for {@link models.basis.Triangle#xMinPoint()}.
	 */
	@Test
	public void testXMinPoint() {
		assertTrue(t.xMinPoint() == p3);
	}

	/**
	 * Test method for {@link models.basis.Triangle#xMaxPoint()}.
	 */
	@Test
	public void testXMaxPoint() {
		assertTrue(t.xMaxPoint() == p1);
	}

	/**
	 * Test method for {@link models.basis.Triangle#yMinPoint()}.
	 */
	@Test
	public void testYMinPoint() {
		assertTrue(t.yMinPoint() == p1);
	}

	/**
	 * Test method for {@link models.basis.Triangle#yMaxPoint()}.
	 */
	@Test
	public void testYMaxPoint() {
		assertTrue(t.yMaxPoint() == p3);
	}

	/**
	 * Test method for {@link models.basis.Triangle#zMinPoint()}.
	 */
	@Test
	public void testZMinPoint() {
		assertTrue(t.zMinPoint() == p1);
	}

	/**
	 * Test method for {@link models.basis.Triangle#zMaxPoint()}.
	 */
	@Test
	public void testZMaxPoint() {
		assertTrue(t.zMaxPoint() == p3);
	}

	/**
	 * Test method for
	 * {@link models.basis.Triangle#angularTolerance(javax.vecmath.Vector3d, double)}
	 * . Test method for
	 * {@link models.basis.Triangle#angularTolerance(models.basis.Triangle, double)}.
	 */
	@Test
	public void testAngularTolerance() {
		Vector3d vector = new Vector3d(0, 1, 0);
		Triangle tBis = new Triangle(p1, p2, p3, e1, e2, e3, vector);

		assertFalse(t.angularTolerance(vector, 60));
		assertFalse(t.angularTolerance(tBis, 60));

		assertTrue(t.angularTolerance(vector, 100));
		assertTrue(t.angularTolerance(tBis, 100));
	}

	/**
	 * Test method for
	 * {@link models.basis.Triangle#isNormalTo(javax.vecmath.Vector3d, double)}.
	 */
	@Test
	public void testIsNormalTo() {
		Vector3d vector = new Vector3d(0, 1, 0);
		Vector3d vector2 = new Vector3d(0, 0, 1);

		assertTrue(t.isNormalTo(vector, 1));
		assertFalse(t.isNormalTo(vector2, 0.2));
	}

	/**
	 * Test method for
	 * {@link models.basis.Triangle#returnNeighbours(models.Mesh, models.Mesh)}.
	 */
	@Test
	public void testReturnNeighbours() {
		Point p4 = new Point(3, 4, 5);
		Point p5 = new Point(-3, -4, -5);
		Point p6 = new Point(-3.5, -1.2, 5.9);
		Edge e4 = new Edge(p1, p4);
		Edge e5 = new Edge(p2, p4);
		Edge e6 = new Edge(p1, p5);
		Edge e7 = new Edge(p2, p5);
		Edge e8 = new Edge(p1, p6);
		Edge e9 = new Edge(p2, p6);
		Edge e10 = new Edge(p5, p6);
		Triangle t2 = new Triangle(p1, p2, p4, e1, e4, e5, vect);
		Triangle t3 = new Triangle(p1, p3, p5, e3, e6, e7, vect);
		Triangle t4 = new Triangle(p2, p3, p6, e2, e8, e9, vect);
		Triangle t5 = new Triangle(p1, p5, p6, e6, e8, e10, vect);

		Mesh contain = new Mesh();
		contain.add(t);
		contain.add(t2);
		contain.add(t3);
		contain.add(t4);
		contain.add(t5);

		// TODO : ajouter un autre triangle qui n'est pas un voisin et tester le
		// résultat.

		Mesh ret = new Mesh();
		t.returnNeighbours(ret, contain);

		assertTrue(ret.contains(t));
		assertTrue(ret.contains(t2));
		assertTrue(ret.contains(t3));
		assertTrue(ret.contains(t4));
		assertTrue(ret.contains(t5));
	}

	/**
	 * Test method for {@link models.basis.Triangle#getNeighbours()}.
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
		Triangle t2 = new Triangle(p1, p2, p4, e1, e4, e5, vect);
		Triangle t3 = new Triangle(p1, p3, p5, e3, e6, e7, vect);
		Triangle t4 = new Triangle(p2, p3, p6, e2, e8, e9, vect);

		Mesh contain = new Mesh();
		contain.add(t);
		contain.add(t2);
		contain.add(t3);
		contain.add(t4);

		ArrayList<Triangle> l = t.getNeighbours();

		assertFalse(l.contains(t));
		assertTrue(l.contains(t2));
		assertTrue(l.contains(t3));
		assertTrue(l.contains(t4));
	}

	/**
	 * Test method for {@link models.basis.Triangle#isNeighboor(models.basis.Triangle)}.
	 */
	public void testIsNeighboor() {

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

		assertFalse(t.isNeighboor(t));
		assertTrue(t.isNeighboor(t2));
		assertTrue(t.isNeighboor(t3));
		assertTrue(t.isNeighboor(t4));
	}

	/**
	 * Test method for {@link models.basis.Triangle#getNumNeighbours()}.
	 */
	@Test
	public void testGetNumNeighbours() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link models.basis.Triangle#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link models.basis.Triangle#Triangle(models.basis.Point, models.basis.Point, models.basis.Point, models.basis.Edge, models.basis.Edge, models.basis.Edge, javax.vecmath.Vector3d)}
	 * .
	 */
	@Test
	public void testTriangle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link models.basis.Triangle#getP1()}.
	 */
	@Test
	public void testGetP1() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link models.basis.Triangle#getP2()}.
	 */
	@Test
	public void testGetP2() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link models.basis.Triangle#getP3()}.
	 */
	@Test
	public void testGetP3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link models.basis.Triangle#getNormal()}.
	 */
	@Test
	public void testGetNormal() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link models.basis.Triangle#getE1()}.
	 */
	@Test
	public void testGetE1() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link models.basis.Triangle#getE2()}.
	 */
	@Test
	public void testGetE2() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link models.basis.Triangle#getE3()}.
	 */
	@Test
	public void testGetE3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link models.basis.Triangle#getEdges()}.
	 */
	@Test
	public void testGetEdges() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link models.basis.Triangle#toString()}.
	 */
	@Test
	public void testToString() {
		fail("Not yet implemented"); // TODO
	}
}