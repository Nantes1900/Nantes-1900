package tests;

import javax.vecmath.Vector3d;

import junit.framework.TestCase;
import modeles.Point;
import utils.MatrixMethod;
import utils.MatrixMethod.SingularMatrixException;

public class PointTest extends TestCase {

	public PointTest(String testName) {
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
		double x = 1.2366646772;
		double y = 435.23134144;
		double z = -210.35681944;
		Point p = new Point(x, y, z);
		assertTrue(p.getX() == x);
		assertTrue(p.getY() == y);
		assertTrue(p.getZ() == z);
	}

	public void testCopyConstructor() {
		double x = 1.2366646772;
		double y = 435.23134144;
		double z = -210.35681944;
		Point p = new Point(x, y, z);
		Point pCopy = new Point(p);
		
		assertTrue(pCopy.getX() == x);
		assertTrue(pCopy.getY() == y);
		assertTrue(pCopy.getZ() == z);
	}

	public void testGetterX() {
		double x = 1.2366646772;
		double y = 435.23134144;
		double z = -210.35681944;
		Point p = new Point(x, y, z);
		assertTrue(p.getX() == x);
	}

	public void testGetterY() {
		double x = 1.2366646772;
		double y = 435.23134144;
		double z = -210.35681944;
		Point p = new Point(x, y, z);
		assertTrue(p.getY() == y);
	}

	public void testGetterZ() {
		double x = 1.2366646772;
		double y = 435.23134144;
		double z = -210.35681944;
		Point p = new Point(x, y, z);
		assertTrue(p.getZ() == z);
	}

	public void testSetterX() {
		double x = 1.2366646772;
		Point p = new Point(0, 0, 0);
		p.setX(x);
		assertTrue(p.getX() == x);
	}

	public void testSetterY() {
		double y = 435.23134144;
		Point p = new Point(0, 0, 0);
		p.setY(y);
		assertTrue(p.getY() == y);
	}

	public void testSetterZ() {
		double z = -210.35681944;
		Point p = new Point(0, 0, 0);
		p.setZ(z);
		assertTrue(p.getZ() == z);
	}

	public void testSetter() {
		double x = 1.2366646772;
		double y = 435.23134144;
		double z = -210.35681944;
		Point p = new Point(0, 0, 0);
		double[] coords = {x, y, z};
		p.set(coords);
		assertTrue(p.getX() == x);
		assertTrue(p.getY() == y);
		assertTrue(p.getZ() == z);
	}

	public void testToString() {
		double x = 1.2366646772;
		double y = 435.23134144;
		double z = -210.35681944;
		Point p2 = new Point(x, y, z);
		assertTrue(p2.toString().equals(new String("(" + x + ", " + y + ", " + z + ")")));
	}

	public void testDistance() {
		Point p1 = new Point(0, 0, 0);
		double x = 1.2366646772;
		double y = 435.23134144;
		double z = -210.35681944;
		Point p2 = new Point(x, y, z);
		assertTrue(p1.distance(p2) == Math.pow(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2) + Math.pow(p2.getZ() - p1.getZ(), 2), 0.5));
	}

	public void testHashCode() {
		double x = 1.2366646772;
		double y = 435.23134144;
		double z = -210.35681944;
		Point p = new Point(x, y, z);

		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));

		assertTrue(p.hashCode() == result);
	}

	public void testEquals() {
		Point p1 = new Point(0, 0, 0);
		Point p2 = p1;
		assertTrue(p2.equals(p1));

		p2 = new Point(p1);
		assertTrue(p2.equals(p1));

		p2.setX(1.0);
		assertFalse(p2.equals(p1));
	}

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
