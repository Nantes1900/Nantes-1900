package nantes1900.tests.models.basis;

import java.security.InvalidParameterException;
import java.util.List;

import javax.vecmath.Vector3d;

import junit.framework.TestCase;
import nantes1900.models.Polyline;
import nantes1900.models.basis.Edge;
import nantes1900.models.basis.Point;
import nantes1900.models.basis.Triangle;
import nantes1900.models.basis.Edge.BadFormedPolylineException;
import nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * A set of tests for the class Edge.
 * 
 * @author Daniel Lefevre
 * 
 */
public class EdgeTest extends TestCase {

	private final Point point1 = new Point(1, 0, -1);
	private final Point point2 = new Point(0, 1, 0);
	private final Point point3 = new Point(-1, 2, 1);
	private final Vector3d vect = new Vector3d(0, 0, 1);
	private final Edge edge1 = new Edge(point1, point2);
	private final Edge edge2 = new Edge(point2, point3);
	private final Edge edge3 = new Edge(point3, point1);

	private final Triangle triangle1;

	private final Point point4 = new Point(2, 2, 2);
	private final Edge edge4 = new Edge(point1, point4);
	private final Edge edge5 = new Edge(point2, point4);

	private final Triangle triangle2;

	private final Edge edge6 = new Edge(point3, point4);
	private final Polyline polyline = new Polyline();

	/**
	 * Constructor of the test class EdgeTest : creates a polyline for the
	 * following tests.
	 * 
	 * @throws MoreThanTwoTrianglesPerEdgeException
	 *             exception if one edge has more than two triangles
	 */
	public EdgeTest() throws MoreThanTwoTrianglesPerEdgeException {
		triangle1 = new Triangle(point1, point2, point3, edge1, edge2, edge3,
				vect);
		triangle2 = new Triangle(point1, point2, point4, edge1, edge4, edge5,
				vect);
		polyline.add(edge1);
		polyline.add(edge2);
		polyline.add(edge4);
		polyline.add(edge6);
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
			edge1.addTriangle(t3);
			fail();
		} catch (MoreThanTwoTrianglesPerEdgeException e) {
			assertFalse(edge1.getTriangles().contains(t3));
		}
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#compose(nantes1900.models.basis.Edge)}
	 * .
	 */
	@Test
	public void testCompose() {
		final Edge e = edge1.compose(edge2);
		assertSame(e.getP1(), point1);
		assertSame(e.getP2(), point3);
	}

	/**
	 * Test method for {@link nantes1900.models.basis.Edge#computeMiddle()}.
	 */
	@Test
	public void testComputeMiddle() {
		assertTrue(edge5.computeMiddle().equals(new Point(1, 1.5, 1)));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#contains(nantes1900.models.basis.Point)}
	 * .
	 */
	@Test
	public void testContains() {
		assertTrue(edge5.contains(point2));
		assertFalse(edge5.contains(point1));
	}

	/**
	 * Test method for {@link nantes1900.models.basis.Edge#convertToVector3d()}.
	 */
	@Test
	public void testConvertToVector3d() {
		assertTrue(edge5.convertToVector3d().equals(new Vector3d(2, 1, 2)));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		final Edge eTest = new Edge(point1, point2);
		assertFalse(edge1.equals(edge2));
		assertEquals(edge1, edge1);
		assertEquals(edge1, eTest);
	}

	/**
	 * Test method for {@link nantes1900.models.basis.Edge#getNumberTriangles()}
	 * .
	 */
	@Test
	public void testGetNumberTriangles() {
		assertTrue(edge1.getNumberTriangles() == 2);
		assertTrue(edge5.getNumberTriangles() == 1);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#getNumNeighbours(nantes1900.models.Polyline)}
	 * .
	 */
	@Test
	public void testGetNumNeighbours() {
		Polyline p = new Polyline();
		p.add(edge1);
		p.add(edge2);
		p.add(edge3);
		p.add(edge4);
		assertTrue(edge1.getNumNeighbours(p) == 3);
		assertTrue(edge2.getNumNeighbours(p) == 2);
		try {
			assertTrue(edge5.getNumNeighbours(p) == 0);
			fail();
		} catch (InvalidParameterException e) {
		}
	}

	/**
	 * Test method for {@link nantes1900.models.basis.Edge#getTriangles()}
	 * and
	 * {@link nantes1900.models.basis.Edge#addTriangle(nantes1900.models.basis.Triangle)}
	 * . This test is also the test of addTriangle.
	 */
	@Test
	public void testGetTriangleList() {
		List<Triangle> list1 = edge1.getTriangles();
		List<Triangle> list2 = edge2.getTriangles();
		List<Triangle> list4 = edge4.getTriangles();

		assertTrue(list1.contains(triangle1));
		assertTrue(list1.contains(triangle2));

		assertTrue(list2.contains(triangle1));
		assertFalse(list2.contains(triangle2));

		assertTrue(list4.contains(triangle2));
		assertFalse(list4.contains(triangle1));
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
		assertTrue(edge1.isNeighboor(edge2));
		assertFalse(edge3.isNeighboor(edge5));
	}

	/**
	 * Test method for {@link nantes1900.models.basis.Edge#length()}.
	 */
	@Test
	public void testLength() {
		assertTrue(edge5.length() == 3);
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
		assertTrue(edge1.returnOther(point1) == point2);
		assertTrue(edge3.returnOther(point3) == point1);
		assertTrue(edge5.returnOther(point4) == point2);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Edge#returnOther(nantes1900.models.basis.Triangle)}
	 * .
	 */
	@Test
	public void testReturnOtherTriangle() {
		try {
			assertTrue(edge1.returnOther(triangle1) == triangle2);
			assertTrue(edge1.returnOther(triangle2) == triangle1);
			assertTrue(edge2.returnOther(triangle1) == null);
		} catch (MoreThanTwoTrianglesPerEdgeException e) {
			fail();
		}
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
