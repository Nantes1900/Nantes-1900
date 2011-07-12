package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import modeles.Edge;
import modeles.Point;
import modeles.Polyline;

import org.junit.Test;

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
	
	public PolylineTest() {
		p.add(e1);
		p.add(e2);
		p.add(e3);
	}

	@Test
	public void testEdgeSize() {
		assertTrue(p.edgeSize() == 3);
	}

	@Test
	public void testPointSize() {
		assertTrue(p.pointSize() == 3);
	}

	@Test
	public void testXAverage() {
		assertTrue(p.xAverage() == 0);
	}

	@Test
	public void testYAverage() {
		assertTrue(p.yAverage() == 1);
	}

	@Test
	public void testZAverage() {
		assertTrue(p.zAverage() == 0);
	}

	@Test
	public void testXMax() {
		assertTrue(p.xMax() == 1);
	}

	@Test
	public void testXMin() {
		assertTrue(p.xMin() == -1);
	}

	@Test
	public void testYMax() {
		assertTrue(p.yMax() == 2);
	}

	@Test
	public void testYMin() {
		assertTrue(p.yMin() == 0);
	}

	@Test
	public void testZMax() {
		assertTrue(p.zMax() == 1);
	}

	@Test
	public void testZMin() {
		assertTrue(p.zMin() == -1);
	}

	@Test
	public void testXLengthAverage() {
		assertTrue(p.xLengthAverage() == 4.0/3.0);
	}

	@Test
	public void testYLengthAverage() {
		assertTrue(p.yLengthAverage() == 4.0/3.0);
	}

	@Test
	public void testZLengthAverage() {
		assertTrue(p.zLengthAverage() == 4.0/3.0);
	}

	@Test
	public void testLengthAverage() {
		double d = Math.pow(4.0/3.0, 2) * 3.0;
		assertTrue(p.lengthAverage() == Math.pow(d, 0.5));
	}

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

	@Test
	public void testZMaxPoint() {
		assertTrue(p.zMaxPoint() == p3);
	}

	@Test
	public void testDistance() {
		assertTrue(p.distance() == 4.0 * Math.pow(3.0, 0.5));
	}
	
	@Test
	public void testRemove() {
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
		
		assertTrue(pol2.contains(p1));
		assertTrue(pol2.contains(p2));
		//FIXME : look in Polyline
		assertFalse(pol2.contains(p3));
		assertTrue(pol2.contains(p4));
	}
	
	@Test
	public void testAreWeInTheTwoLinesOrNot() {
		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(1, 1, 0);
		Point p3 = new Point(2.2, 2, 0);
		double error = 0.3;
		assertTrue(p.areWeInTheTwoLinesOrNot(p1, p2, p3, error));
		
		p3 = new Point(3, 2, 0);
		assertFalse(p.areWeInTheTwoLinesOrNot(p1, p2, p3, error));
	}
	
	@Test
	public void testFollowTheFramedLine() {
		Point p0 = new Point(-1, -1, 0);
		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(1, 1, 0);
		Point p3 = new Point(2.2, 2, 0);
		Point p4 = new Point(3, 3, 0);
		Point p5 = new Point(4.1, 4, 0);
		Point p6 = new Point(10, 4, 0);
		
		Edge e0 = new Edge(p0, p1);
		Edge e1 = new Edge(p1, p2);
		Edge e2 = new Edge(p2, p3);
		Edge e3 = new Edge(p3, p4);
		Edge e4 = new Edge(p4, p5);
		Edge e5 = new Edge(p5, p6);
		
		Polyline line = new Polyline();
		
		line.add(e0);
		line.add(e1);
		line.add(e2);
		line.add(e3);
		line.add(e4);
		line.add(e5);
		
		assertTrue(line.followTheFramedLine(e1, p1, 0.4, e0) == e5);
	}

//	@Test
//	TODO
//	public void testReturnMesh() {
//	
//	}
//
//	@Test
//	TODO
//	public void testOrderBorder() {
//	}
}
