package tests;

import static org.junit.Assert.*;
import modeles.Edge;
import modeles.Point;
import modeles.Polyline;

import org.junit.Test;

/**
 * A set of tests for the class Polyline.
 * 
 * @author Daniel Lefevre
 * 
 */
public class PolylineTest {

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
	 * Test method for {@link modeles.Polyline#edgeSize()}.
	 */
	@Test
	public void testEdgeSize() {
		assertTrue(p.edgeSize() == 3);
	}

	/**
	 * Test method for {@link modeles.Polyline#pointSize()}.
	 */
	@Test
	public void testPointSize() {
		assertTrue(p.pointSize() == 3);
	}

	/**
	 * Test method for {@link modeles.Polyline#xAverage()}.
	 */
	@Test
	public void testXAverage() {
		assertTrue(p.xAverage() == 0);
	}

	/**
	 * Test method for {@link modeles.Polyline#yAverage()}.
	 */
	@Test
	public void testYAverage() {
		assertTrue(p.yAverage() == 1);
	}

	/**
	 * Test method for {@link modeles.Polyline#zAverage()}.
	 */
	@Test
	public void testZAverage() {
		assertTrue(p.zAverage() == 0);
	}

	/**
	 * Test method for {@link modeles.Polyline#xMax()}.
	 */
	@Test
	public void testXMax() {
		assertTrue(p.xMax() == 1);
	}

	/**
	 * Test method for {@link modeles.Polyline#xMin()}.
	 */
	@Test
	public void testXMin() {
		assertTrue(p.xMin() == -1);
	}

	/**
	 * Test method for {@link modeles.Polyline#yMax()}.
	 */
	@Test
	public void testYMax() {
		assertTrue(p.yMax() == 2);
	}

	/**
	 * Test method for {@link modeles.Polyline#yMin()}.
	 */
	@Test
	public void testYMin() {
		assertTrue(p.yMin() == 0);
	}

	/**
	 * Test method for {@link modeles.Polyline#zMax()}.
	 */
	@Test
	public void testZMax() {
		assertTrue(p.zMax() == 1);
	}

	/**
	 * Test method for {@link modeles.Polyline#zMin()}.
	 */
	@Test
	public void testZMin() {
		assertTrue(p.zMin() == -1);
	}

	/**
	 * Test method for {@link modeles.Polyline#xLengthAverage()}.
	 */
	@Test
	public void testXLengthAverage() {
		assertTrue(p.xLengthAverage() == 4.0 / 3.0);
	}

	/**
	 * Test method for {@link modeles.Polyline#yLengthAverage()}.
	 */
	@Test
	public void testYLengthAverage() {
		assertTrue(p.yLengthAverage() == 4.0 / 3.0);
	}

	/**
	 * Test method for {@link modeles.Polyline#zLengthAverage()}.
	 */
	@Test
	public void testZLengthAverage() {
		assertTrue(p.zLengthAverage() == 4.0 / 3.0);
	}

	/**
	 * Test method for {@link modeles.Polyline#lengthAverage()}.
	 */
	@Test
	public void testLengthAverage() {
		double d = Math.pow(4.0 / 3.0, 2) * 3.0;
		assertTrue(p.lengthAverage() == Math.pow(d, 0.5));
	}

	/**
	 * Test method for {@link modeles.Polyline#xBetween(double, double)}.
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
	 * Test method for {@link modeles.Polyline#yBetween(double, double)}.
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
	 * Test method for {@link modeles.Polyline#zBetween(double, double)}.
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
	 * Test method for {@link modeles.Polyline#zMaxPoint()}.
	 */
	@Test
	public void testZMaxPoint() {
		assertTrue(p.zMaxPoint() == p3);
	}

	/**
	 * Test method for {@link modeles.Polyline#length()}.
	 */
	@Test
	public void testLength() {
		assertTrue(p.length() == 4.0 * Math.pow(3.0, 0.5));
	}

	/**
	 * Test method for {@link modeles.Polyline#remove(modeles.Polyline)}.
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
	 * Test method for
	 * {@link modeles.Polyline#isInCylinder2D(modeles.Point, modeles.Point, modeles.Point, double)}
	 * .
	 */
	@Test
	public void testAreWeInTheTwoLinesOrNot() {
		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(1, 1, 0);
		Point p3 = new Point(2.2, 2, 0);
		double error = 0.3;
		assertTrue(p.isInCylinder2D(p1, p2, p3, error));

		p3 = new Point(3, 2, 0);
		assertFalse(p.isInCylinder2D(p1, p2, p3, error));
	}

	// /**
	// * Test method for {@link
	// modeles.Polyline#followTheFramedLine(modeles.Edge, modeles.Point, double,
	// modeles.Edge)}.
	// */
	// @Test
	// public void testFollowTheFramedLine() {
	// Point p0 = new Point(-1, -1, 0);
	// Point p1 = new Point(0, 0, 0);
	// Point p2 = new Point(1, 1, 0);
	// Point p3 = new Point(2.2, 2, 0);
	// Point p4 = new Point(3, 3, 0);
	// Point p5 = new Point(4.1, 4, 0);
	// Point p6 = new Point(10, 4, 0);
	//
	// Edge e0 = new Edge(p0, p1);
	// Edge e1 = new Edge(p1, p2);
	// Edge e2 = new Edge(p2, p3);
	// Edge e3 = new Edge(p3, p4);
	// Edge e4 = new Edge(p4, p5);
	// Edge e5 = new Edge(p5, p6);
	//
	// Polyline line = new Polyline();
	//
	// line.add(e0);
	// line.add(e1);
	// line.add(e2);
	// line.add(e3);
	// line.add(e4);
	// line.add(e5);
	//
	// assertTrue(line.followTheFramedLine(e1, p1, 0.4, e0) == e5);
	// }

	/**
	 * Test method for {@link modeles.Polyline#returnMesh()}.
	 */
	@Test
	public void testReturnMesh() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#order()}.
	 */
	@Test
	public void testOrder() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#Polyline(java.util.List)}.
	 */
	@Test
	public void testPolylineListOfEdge() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#Polyline()}.
	 */
	@Test
	public void testPolyline() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#getEdgeList()}.
	 */
	@Test
	public void testGetEdgeList() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#getPointList()}.
	 */
	@Test
	public void testGetPointList() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#getID()}.
	 */
	@Test
	public void testGetID() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#add(modeles.Edge)}.
	 */
	@Test
	public void testAddEdge() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#add(modeles.Point)}.
	 */
	@Test
	public void testAddPoint() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#addAll(java.util.List)}.
	 */
	@Test
	public void testAddAll() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#getOne()}.
	 */
	@Test
	public void testGetOne() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#remove(modeles.Edge)}.
	 */
	@Test
	public void testRemoveEdge() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#contains(modeles.Point)}.
	 */
	@Test
	public void testContainsPoint() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#contains(modeles.Edge)}.
	 */
	@Test
	public void testContainsEdge() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#isEmpty()}.
	 */
	@Test
	public void testIsEmpty() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#returnCentroidMesh()}.
	 */
	@Test
	public void testReturnCentroidMesh() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#changeBase(double[][])}.
	 */
	@Test
	public void testChangeBase() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#getNumNeighbours(modeles.Point)}.
	 */
	@Test
	public void testGetNumNeighbours() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#getNeighbours(modeles.Point)}.
	 */
	@Test
	public void testGetNeighbours() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link modeles.Polyline#zProjection(double)}.
	 */
	@Test
	public void testZProjection() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link modeles.Polyline#determinateSingularPoints(double)}.
	 */
	@Test
	public void testDeterminateSingularPoints() {
		fail("Not yet implemented"); // TODO
	}

}
