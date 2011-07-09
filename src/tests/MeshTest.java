package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.vecmath.Vector3d;

import modeles.Edge;
import modeles.Mesh;
import modeles.Point;
import modeles.Triangle;

import org.junit.Test;

public class MeshTest {

	//LOOK : doc !
	private Point p1 = new Point(1, 0, -1);
	private Point p2 = new Point(0, 1, 0);
	private Point p3 = new Point(-1, 2, 1);
	private Vector3d vect1 = new Vector3d(0, 0, 1);
	private Edge e1 = new Edge(p1, p2);
	private Edge e2 = new Edge(p2, p3);
	private Edge e3 = new Edge(p3, p1);
	private Triangle t1 = new Triangle(p1, p2, p3, e1, e2, e3, vect1);

	private Point p4 = new Point(4, 5, 4);
	private Point p5 = new Point(2, -3, -3);
	private Point p6 = new Point(-2, 4, -5);
	private Vector3d vect2 = new Vector3d(1, 0, 0);
	private Edge e4 = new Edge(p4, p5);
	private Edge e5 = new Edge(p5, p6);
	private Edge e6 = new Edge(p6, p4);
	private Triangle t2 = new Triangle(p4, p5, p6, e4, e5, e6, vect2);

	private Mesh m = new Mesh();

	public MeshTest() {
		this.m.add(this.t1);
		this.m.add(this.t2);
	}

	@Test
	public void testAverageNormal() {
		assertTrue(this.m.averageNormal().equals(new Vector3d(0.5, 0, 0.5)));
	}

	@Test
	public void testxAverage() {
		assertTrue(this.m.xAverage() == 2.0/3.0);
	}

	@Test
	public void testyAverage() {
		assertTrue(this.m.yAverage() == 3.0/2.0);
	}

	@Test
	public void testzAverage() {
		assertTrue(this.m.zAverage() == -2.0/3.0);
	}

	@Test
	public void testxMax() {
		assertTrue(this.m.xMax() == 4);
	}

	@Test
	public void testxMin() {
		assertTrue(this.m.xMin() == -2);
	}

	@Test
	public void testyMax() {
		assertTrue(this.m.yMax() == 5);
	}

	@Test
	public void testyMin() {
		assertTrue(this.m.yMin() == -3);
	}

	@Test
	public void testzMax() {
		assertTrue(this.m.zMax() == 4);
	}

	@Test
	public void testzMin() {
		assertTrue(this.m.zMin() == -5);
	}

	@Test
	public void testxLengthAverage() {
		assertTrue(this.m.xLengthAverage() == 4);
	}

	@Test
	public void testyLengthAverage() {
		assertTrue(this.m.yLengthAverage() == 5);
	}

	@Test
	public void testzLengthAverage() {
		assertTrue(this.m.zLengthAverage() == 5.5);
	}

	@Test
	//LOOK : complete this test with other Triangles
	public void testXBetween() {
		Mesh xBet = m.xBetween(10, -10);
		assertTrue(xBet.contains(t1));
		assertTrue(xBet.contains(t2));
	}

	@Test
	//LOOK : complete this test with other Triangles
	public void testYBetween() {
		Mesh yBet = m.yBetween(10, -10);
		assertTrue(yBet.contains(t1));
		assertTrue(yBet.contains(t2));
	}

	@Test
	//LOOK : complete this test with other Triangles
	public void testZBetween() {
		Mesh zBet = m.zBetween(10, -10);
		assertTrue(zBet.contains(t1));
		assertTrue(zBet.contains(t2));
	}

	//LOOK : don't use xAverage() ?
	@Test
	public void testXProjection() {
		Mesh mProj = m.xProjection(1.2);
		assertTrue(mProj.xAverage() == 1.2);
	}

	@Test
	public void testYProjection() {
		Mesh mProj = m.yProjection(1.2);
		assertTrue(mProj.yAverage() == 1.2);
	}

	@Test
	public void testZProjection() {
		Mesh mProj = m.zProjection(1.2);
		assertTrue(mProj.zAverage() == 1.2);
	}

	@Test
	public void testZMinFace() {
		assertTrue(m.zMinFace() == t2);
	}

	@Test
	public void testFaceUnderZ() {
		assertTrue(m.faceUnderZ(2) == t1);
	}

	@Test
	public void testOrientedAs() {
		Mesh oriented = m.orientedAs(new Vector3d(0.1, 0.1, 0.9), 15);
		assertTrue(oriented.contains(t1));
		assertFalse(oriented.contains(t2));
	}

	@Test
	public void testOrientedNormalTo() {
		Mesh oriented = m.orientedNormalTo(new Vector3d(0.1, 0.1, 0.9), 0.2);
		assertTrue(oriented.contains(t2));
		assertFalse(oriented.contains(t1));
	}
}