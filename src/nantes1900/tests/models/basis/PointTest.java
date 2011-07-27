package nantes1900.tests.models.basis;

import static org.junit.Assert.*;

import javax.vecmath.Vector3d;

import nantes1900.models.basis.Point;
import nantes1900.utils.MatrixMethod;
import nantes1900.utils.MatrixMethod.SingularMatrixException;

import org.junit.Test;

/**
 * A set of tests for the class Point.
 * 
 * @author Daniel Lefevre
 * 
 */
public class PointTest {
	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Point#changeBase(double[][])}.
	 */
	@Test
	public void testChangeBase() {
		Vector3d vect = new Vector3d(0, 0, 1);

		try {

			double[][] matrix = MatrixMethod.createOrthoBase(vect);

			double x = 1.2366646772;
			double y = 435.23134144;
			double z = -210.35681944;
			Point p = new Point(x, y, z);

			p.changeBase(matrix);

			Point pChanged = new Point(0, 0, 0);
			double[] coords = { x, y, z };
			pChanged.set(MatrixMethod.changeBase(coords, matrix));

			assertTrue(p.equals(pChanged));

		} catch (SingularMatrixException e) {
			fail();
		}
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Point#distance(nantes1900.models.basis.Point)}
	 * .
	 */
	@Test
	public void testDistance() {
		Point p1 = new Point(0, 0, 0);
		double x = 1.2366646772;
		double y = 435.23134144;
		double z = -210.35681944;
		Point p2 = new Point(x, y, z);
		assertTrue(p1.distance(p2) == Math.pow(
				Math.pow(p2.getX() - p1.getX(), 2)
						+ Math.pow(p2.getY() - p1.getY(), 2)
						+ Math.pow(p2.getZ() - p1.getZ(), 2), 0.5));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Point#equals(java.lang.Object)}.
	 */
	@Test
	public void testEquals() {
		Point p1 = new Point(0, 0, 0);
		Point p2 = p1;
		assertTrue(p2.equals(p1));

		p2 = new Point(p1);
		assertTrue(p2.equals(p1));

		p2.setX(1.0);
		assertFalse(p2.equals(p1));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Point#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(0, 0, 0);

		assertTrue(p1.equals(p2));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Point#getPointAsCoordinates()}.
	 */
	@Test
	public void testGetPointAsCoordinates() {
		Point p1 = new Point(0.5, 0.5, 0.5);
		double[] coords = p1.getPointAsCoordinates();
		assertTrue(coords[0] == 0.5 && coords[1] == 0.5 && coords[2] == 0.5);
	}

	/**
	 * Test method for {@link nantes1900.models.basis.Point#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		Point p1 = new Point(0.0242515242412, 0, 0);
		Point p2 = new Point(0.0242515244450, 0, 0);
		assertTrue(p1.hashCode() == p2.hashCode());
	}

	/**
	 * Test method for {@link nantes1900.models.basis.Point#set(double[])}.
	 */
	@Test
	public void testSetDoubleArray() {
		double[] a = { 0.1, 0.2, 0.4 };
		Point p1 = new Point(0, 0, 0);
		p1.set(a);

		assertTrue(p1.getX() == 0.1);
		assertTrue(p1.getY() == 0.2);
		assertTrue(p1.getZ() == 0.4);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.basis.Point#set(double, double, double)}.
	 */
	@Test
	public void testSetDoubleDoubleDouble() {
		double a = 0.1, b = 0.2, c = 0.4;
		Point p1 = new Point(0, 0, 0);
		p1.set(a, b, c);

		assertTrue(p1.getX() == 0.1);
		assertTrue(p1.getY() == 0.2);
		assertTrue(p1.getZ() == 0.4);
	}
}
