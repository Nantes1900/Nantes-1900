package tests;

import static org.junit.Assert.*;

import javax.vecmath.Vector3d;

import org.junit.Test;

import utils.MatrixMethod;
import utils.MatrixMethod.SingularMatrixException;

/**
 * A set of tests for the class MatrixMethod.
 * 
 * @author Daniel Lefevre
 * 
 */
public class MatrixMethodTest {

	/**
	 * Test method for
	 * {@link utils.MatrixMethod#createOrthoBase(javax.vecmath.Vector3d)}.
	 */
	@Test
	public void testCreateOrthoBaseVector3d() {
		Vector3d vect = new Vector3d(0, 0, 1);
		try {
			double[][] matrix = MatrixMethod.createOrthoBase(vect);
			assertTrue(matrix[0][0] == -1);
			assertTrue(matrix[0][1] == 0);
			assertTrue(matrix[0][2] == 0);
			assertTrue(matrix[1][0] == 0);
			assertTrue(matrix[1][1] == -1);
			assertTrue(matrix[1][2] == 0);
			assertTrue(matrix[2][0] == 0);
			assertTrue(matrix[2][1] == 0);
			assertTrue(matrix[2][2] == 1);
		} catch (SingularMatrixException e) {
			fail("Singular matrix !");
		}

		vect = new Vector3d(0, 0, -1);
		try{
			MatrixMethod.createOrthoBase(vect);
			fail();
		} catch(SingularMatrixException e) {
			
		}
	}

	/**
	 * Test method for {@link utils.MatrixMethod#determinant(double[][])}.
	 */
	@Test
	public void testDeterminant() {
		Vector3d vect = new Vector3d(0, 0, 1);
		try {
			double[][] matrix = MatrixMethod.createOrthoBase(vect);
			assertTrue(MatrixMethod.determinant(matrix) == 1);
		} catch (SingularMatrixException e) {
			fail("Singular matrix !");
		}
	}

	/**
	 * Test method for {@link utils.MatrixMethod#getInversMatrix(double[][])}.
	 */
	@Test
	public void testGetInversMatrix() {
		Vector3d vect = new Vector3d(0, 0, 1);
		try {
			double[][] matrix = MatrixMethod.createOrthoBase(vect);
			double[][] matrixInv = MatrixMethod.getInversMatrix(matrix);
			assertTrue(matrixInv[0][0] == -1);
			assertTrue(matrixInv[0][1] == 0);
			assertTrue(matrixInv[0][2] == 0);
			assertTrue(matrixInv[1][0] == 0);
			assertTrue(matrixInv[1][1] == -1);
			assertTrue(matrixInv[1][2] == 0);
			assertTrue(matrixInv[2][0] == 0);
			assertTrue(matrixInv[2][1] == 0);
			assertTrue(matrixInv[2][2] == 1);
		} catch (SingularMatrixException e) {
			fail("Singular matrix !");
		}
	}

	/**
	 * Test method for
	 * {@link utils.MatrixMethod#createOrthoBase(javax.vecmath.Vector3d, javax.vecmath.Vector3d, javax.vecmath.Vector3d)}
	 * .
	 */
	@Test
	public void testCreateOrthoBaseVector3dVector3dVector3d() {
		Vector3d vect1 = new Vector3d(0, 0, 1);
		Vector3d vect2 = new Vector3d(1, 0, 0);
		Vector3d vect3 = new Vector3d(0, 1, 0);
		try {
			double[][] matrix = MatrixMethod.createOrthoBase(vect1, vect2, vect3);
			assertTrue(matrix[0][0] == 0);
			assertTrue(matrix[0][1] == 0);
			assertTrue(matrix[0][2] == 1);
			assertTrue(matrix[1][0] == 1);
			assertTrue(matrix[1][1] == 0);
			assertTrue(matrix[1][2] == 0);
			assertTrue(matrix[2][0] == 0);
			assertTrue(matrix[2][1] == 1);
			assertTrue(matrix[2][2] == 0);
		} catch (SingularMatrixException e) {
			fail("Singular matrix !");
		}
	}

	/**
	 * Test method for
	 * {@link utils.MatrixMethod#changeBase(double[], double[][])}.
	 */
	@Test
	public void testChangeBaseDoubleArrayDoubleArrayArray() {
		double[] coord = {0, 1, 0};
		Vector3d vect = new Vector3d(0, 0, 1);
		try {
			double[][] matrix = MatrixMethod.createOrthoBase(vect);
			double[] newCoords = MatrixMethod.changeBase(coord, matrix);
			assertTrue(newCoords[0] == 0);
			assertTrue(newCoords[1] == -1);
			assertTrue(newCoords[2] == 0);
		} catch (SingularMatrixException e) {
			fail("Singular matrix !");
		}
	}

	/**
	 * Test method for
	 * {@link utils.MatrixMethod#changeBase(javax.vecmath.Vector3d, double[][])}
	 * .
	 */
	@Test
	public void testChangeBaseVector3dDoubleArrayArray() {
		Vector3d vect = new Vector3d(0, 1, 0);
		Vector3d norm = new Vector3d(0, 0, 1);
		try {
			double[][] matrix = MatrixMethod.createOrthoBase(norm);
			MatrixMethod.changeBase(vect, matrix);
			assertTrue(vect.x == 0);
			assertTrue(vect.y == -1);
			assertTrue(vect.z == 0);
		} catch (SingularMatrixException e) {
			fail("Singular matrix !");
		}
	}

}
