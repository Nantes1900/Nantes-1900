package test.fr.nantes1900.utils;

import fr.nantes1900.utils.MatrixMethod;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;

import javax.vecmath.Vector3d;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * A set of tests for the class MatrixMethod.
 * @author Daniel Lefevre
 */
public final class MatrixMethodTest extends TestCase
{

    /**
     * Constructor.
     */
    public MatrixMethodTest()
    {
    }

    /**
     * Test method for
     * {@link fr.nantes1900.utils.MatrixMethod#changeBase(double[], double[][])}
     * .
     */
    @Test
    public static void testChangeBaseDoubleArrayDoubleArrayArray()
    {
        final double[] coord = {0, 1, 0};
        final Vector3d vect = new Vector3d(0, 0, 1);
        try
        {
            final double[][] matrix = MatrixMethod.createOrthoBase(vect);
            final double[] newCoords = MatrixMethod.changeBase(coord, matrix);
            Assert.assertTrue(newCoords[0] == 0);
            Assert.assertTrue(newCoords[1] == -1);
            Assert.assertTrue(newCoords[2] == 0);
        } catch (final SingularMatrixException e)
        {
            Assert.fail("Singular matrix !");
        }
    }

    /**
     * Test method for
     * {@link fr.nantes1900.utils.MatrixMethod#changeBase(javax.vecmath.Vector3d, double[][])}
     * .
     */
    @Test
    public static void testChangeBaseVector3dDoubleArrayArray()
    {
        final Vector3d vect = new Vector3d(0, 1, 0);
        final Vector3d norm = new Vector3d(0, 0, 1);
        try
        {
            final double[][] matrix = MatrixMethod.createOrthoBase(norm);
            MatrixMethod.changeBase(vect, matrix);
            Assert.assertTrue(vect.x == 0);
            Assert.assertTrue(vect.y == -1);
            Assert.assertTrue(vect.z == 0);
        } catch (final SingularMatrixException e)
        {
            Assert.fail("Singular matrix !");
        }
    }

    /**
     * Test method for
     * {@link fr.nantes1900.utils.MatrixMethod#createOrthoBase(javax.vecmath.Vector3d)}
     * .
     */
    @Test
    public static void testCreateOrthoBaseVector3d()
    {
        Vector3d vect = new Vector3d(0, 0, 1);
        try
        {
            final double[][] matrix = MatrixMethod.createOrthoBase(vect);
            Assert.assertTrue(matrix[0][0] == -1);
            Assert.assertTrue(matrix[0][1] == 0);
            Assert.assertTrue(matrix[0][2] == 0);
            Assert.assertTrue(matrix[1][0] == 0);
            Assert.assertTrue(matrix[1][1] == -1);
            Assert.assertTrue(matrix[1][2] == 0);
            Assert.assertTrue(matrix[2][0] == 0);
            Assert.assertTrue(matrix[2][1] == 0);
            Assert.assertTrue(matrix[2][2] == 1);
        } catch (final SingularMatrixException e)
        {
            Assert.fail("Singular matrix !");
        }

        vect = new Vector3d(0, 0, -1);
        try
        {
            final double[][] matrix = MatrixMethod.createOrthoBase(vect);
            Assert.assertTrue(matrix[0][0] == 1);
            Assert.assertTrue(matrix[0][1] == 0);
            Assert.assertTrue(matrix[0][2] == 0);
            Assert.assertTrue(matrix[1][0] == 0);
            Assert.assertTrue(matrix[1][1] == 1);
            Assert.assertTrue(matrix[1][2] == 0);
            Assert.assertTrue(matrix[2][0] == 0);
            Assert.assertTrue(matrix[2][1] == 0);
            Assert.assertTrue(matrix[2][2] == -1);
        } catch (final SingularMatrixException e)
        {
            // If the exception is reached, it's good.
        }
    }

    /**
     * Test method for
     * {@link fr.nantes1900.utils.MatrixMethod#createOrthoBase(javax.vecmath.Vector3d, javax.vecmath.Vector3d, javax.vecmath.Vector3d)}
     * .
     */
    @Test
    public static void testCreateOrthoBaseVector3dVector3dVector3d()
    {
        final Vector3d vect1 = new Vector3d(0, 0, 1);
        final Vector3d vect2 = new Vector3d(1, 0, 0);
        final Vector3d vect3 = new Vector3d(0, 1, 0);
        try
        {
            final double[][] matrix = MatrixMethod.createOrthoBase(vect1,
                    vect2, vect3);
            Assert.assertTrue(matrix[0][0] == 0);
            Assert.assertTrue(matrix[0][1] == 0);
            Assert.assertTrue(matrix[0][2] == 1);
            Assert.assertTrue(matrix[1][0] == 1);
            Assert.assertTrue(matrix[1][1] == 0);
            Assert.assertTrue(matrix[1][2] == 0);
            Assert.assertTrue(matrix[2][0] == 0);
            Assert.assertTrue(matrix[2][1] == 1);
            Assert.assertTrue(matrix[2][2] == 0);
        } catch (final SingularMatrixException e)
        {
            Assert.fail("Singular matrix !");
        }
    }

    /**
     * Test method for
     * {@link fr.nantes1900.utils.MatrixMethod#determinant(double[][])}.
     */
    @Test
    public static void testDeterminant()
    {
        final Vector3d vect = new Vector3d(0, 0, 1);
        try
        {
            final double[][] matrix = MatrixMethod.createOrthoBase(vect);
            Assert.assertTrue(MatrixMethod.determinant(matrix) == 1);
        } catch (final SingularMatrixException e)
        {
            Assert.fail("Singular matrix !");
        }
    }

    /**
     * Test method for
     * {@link fr.nantes1900.utils.MatrixMethod#getInversMatrix(double[][])}.
     */
    @Test
    public static void testGetInversMatrix()
    {
        final Vector3d vect = new Vector3d(0, 0, 1);
        try
        {
            final double[][] matrix = MatrixMethod.createOrthoBase(vect);
            final double[][] matrixInv = MatrixMethod.getInversMatrix(matrix);
            Assert.assertTrue(matrixInv[0][0] == -1);
            Assert.assertTrue(matrixInv[0][1] == 0);
            Assert.assertTrue(matrixInv[0][2] == 0);
            Assert.assertTrue(matrixInv[1][0] == 0);
            Assert.assertTrue(matrixInv[1][1] == -1);
            Assert.assertTrue(matrixInv[1][2] == 0);
            Assert.assertTrue(matrixInv[2][0] == 0);
            Assert.assertTrue(matrixInv[2][1] == 0);
            Assert.assertTrue(matrixInv[2][2] == 1);
        } catch (final SingularMatrixException e)
        {
            Assert.fail("Singular matrix !");
        }

        final Vector3d vect2 = new Vector3d(0, 0, 0);
        try
        {
            final double[][] matrix = MatrixMethod.createOrthoBase(vect2);
            MatrixMethod.getInversMatrix(matrix);
            Assert.fail("Should throw an exception !");
        } catch (final SingularMatrixException e)
        {
            // This exception must be catched.
        }
    }
}
