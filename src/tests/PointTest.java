package tests;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import javax.vecmath.Vector3d;

import modeles.Point;

import org.junit.Test;

import utils.MatrixMethod;
import utils.MatrixMethod.SingularMatrixException;

public class PointTest {


	@Test
	public void testDistance() {
		Point p1 = new Point(0, 0, 0);
		double x = 1.2366646772;
		double y = 435.23134144;
		double z = -210.35681944;
		Point p2 = new Point(x, y, z);
		assertTrue(p1.distance(p2) == Math.pow(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2) + Math.pow(p2.getZ() - p1.getZ(), 2), 0.5));
	}

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

	@Test
	public void testChangeBase() {
		Vector3d vect = new Vector3d(0, 0, 1);
		
		boolean exception = true;
		
		try {
			
			double[][] matrix = MatrixMethod.createOrthoBase(vect);

			double x = 1.2366646772;
			double y = 435.23134144;
			double z = -210.35681944;
			Point p = new Point(x, y, z);
			
			p.changeBase(matrix);
			
			Point pChanged = new Point(0, 0, 0);
			double[] coords = {x, y, z};
			pChanged.set(MatrixMethod.changeBase(coords, matrix));
			
			assertTrue(p.equals(pChanged));
			
		} catch (SingularMatrixException e) {
			//TODO : this Exception is not supposed to happen.
			exception = false;
		}
		
		assertTrue(exception);
	}
}
