package fr.nantes1900.utils;

import javax.vecmath.Vector3d;

/**
 * A set of methods to create and change base.
 * 
 * @author Eric Berthe, Valentin Roger, Daniel Lefevre
 */
public final class MatrixMethod {

    /**
     * Precision (as a double) to check if a matrix determinant is too close to
     * zero or not.
     */
    private static final double ERROR_PRECISION = 0.01;

    /**
     * The dimension of the matrix used in some algorithms. Here, it's 3.
     */
    public static final int MATRIX_DIMENSION = 3;

    /**
     * Private constructor.
     */
    private MatrixMethod() {
    }

    /**
     * Compute the result of a base change of a double[].
     * 
     * @param coord
     *            the coordinates to base change
     * @param matrix
     *            the base change matrix
     * @return the new coordinates
     */
    public static double[] changeBase(final double[] coord,
        final double[][] matrix) {
        final int n = coord.length;
        final double[] sol = new double[n];

        for (int i = 0; i < n; i = i + 1) {
            sol[i] = 0;
            for (int j = 0; j < n; j = j + 1) {
                sol[i] += matrix[i][j] * coord[j];
            }
        }
        return sol;
    }

    /**
     * Compute the result of a base change of a Vector3d. vect is modified to
     * contain the result of the compute.
     * 
     * @param vect
     *            the vector to base change
     * @param matrix
     *            the base change matrix
     */
    public static void changeBase(final Vector3d vect, final double[][] matrix) {
        final double[] coord = {vect.x, vect.y, vect.z, };
        vect.set(MatrixMethod.changeBase(coord, matrix));
    }

    /**
     * Create an orthobase from one vector. The baseVector will be the z-axis
     * after the base change. The matrix will have a determinant equals to 1.
     * 
     * @param baseVector
     *            the vector which will be the z-axis after the base change
     * @return a double[][] as a matrix
     * @throws SingularMatrixException
     *             if the baseVector is oriented as (0, 0, -1), because the
     *             matrix would be singular
     */
    public static double[][] createOrthoBase(final Vector3d baseVector)
        throws SingularMatrixException {

        final Vector3d b = new Vector3d(baseVector);
        b.normalize();

        if (b.z > -1 - MatrixMethod.ERROR_PRECISION
            && b.z < -1 + MatrixMethod.ERROR_PRECISION) {
            throw new SingularMatrixException();
        }

        if (new Double(b.x).isNaN() || new Double(b.y).isNaN() || new Double(b.z).isNaN()) {
            throw new SingularMatrixException();
        }

        // Gram-Schmidt
        final double[][] matrix1 =
            new double[MatrixMethod.MATRIX_DIMENSION][MatrixMethod.MATRIX_DIMENSION];

        matrix1[0][2] = b.x;
        matrix1[1][2] = b.y;
        matrix1[2][2] = b.z;
        matrix1[2][0] = b.x;
        matrix1[2][1] = b.y;

        // The next base is choosen to have the baseVector as z-axis, and an
        // orthobase matrix.
        matrix1[1][1] = -1 + (1 / (b.z + 1)) * b.y * b.y;
        matrix1[0][1] = (1 / (b.z + 1)) * b.y * b.x;
        matrix1[1][0] = (1 / (b.z + 1)) * b.y * b.x;
        matrix1[0][0] = -1 + (1 / (b.z + 1)) * b.x * b.x;

        return matrix1;
    }

    /**
     * Create an orthobase from three vectors. Create the matrix which pass from
     * the actual base ((1,0,0),(0,1,0),(0,0,1)) to the parameter base (vect1,
     * vect2, vect3).
     * 
     * @param vect1
     *            the first vector of the base
     * @param vect2
     *            the second vector of the base
     * @param vect3
     *            the third vector of the base
     * @return the matrix of base change
     * @throws SingularMatrixException
     *             if the matrix is singular
     */
    public static double[][] createOrthoBase(final Vector3d vect1,
        final Vector3d vect2, final Vector3d vect3)
        throws SingularMatrixException {

        final Vector3d b = new Vector3d(vect1);
        final Vector3d c = new Vector3d(vect2);
        final Vector3d d = new Vector3d(vect3);

        // Gram-Schmidt
        final double[][] matrix1 =
            new double[MatrixMethod.MATRIX_DIMENSION][MatrixMethod.MATRIX_DIMENSION];

        matrix1[0][0] = b.x;
        matrix1[0][1] = b.y;
        matrix1[0][2] = b.z;
        matrix1[1][0] = c.x;
        matrix1[1][1] = c.y;
        matrix1[1][2] = c.z;
        matrix1[2][0] = d.x;
        matrix1[2][1] = d.y;
        matrix1[2][2] = d.z;

        return matrix1;
    }

    /**
     * Returns the matrix determinant.
     * 
     * @param matrix
     *            the matrix to compute
     * @return the determinant
     */
    public static double determinant(final double[][] matrix) {
        final double a = matrix[0][0];
        final double d = matrix[1][0];
        final double g = matrix[2][0];
        final double b = matrix[0][1];
        final double e = matrix[1][1];
        final double h = matrix[2][1];
        final double c = matrix[0][2];
        final double f = matrix[1][2];
        final double i = matrix[2][2];

        return a * e * i + b * f * g + c * d * h - c * e * g - f * h * a - i
            * b * d;
    }

    /**
     * Returns the invers of the matrix.
     * 
     * @param matrix
     *            the matrix to invers
     * @return the invers matrix
     * @throws SingularMatrixException
     *             if the matrix is singular.
     */
    public static double[][] getInversMatrix(final double[][] matrix)
        throws SingularMatrixException {
        final double[][] matrix1 =
            new double[MatrixMethod.MATRIX_DIMENSION][MatrixMethod.MATRIX_DIMENSION];

        if (MatrixMethod.determinant(matrix) < MatrixMethod.ERROR_PRECISION
            && MatrixMethod.determinant(matrix) > -MatrixMethod.ERROR_PRECISION) {
            throw new SingularMatrixException();
        }

        final double a = matrix[0][0];
        final double d = matrix[1][0];
        final double g = matrix[2][0];
        final double b = matrix[0][1];
        final double e = matrix[1][1];
        final double h = matrix[2][1];
        final double c = matrix[0][2];
        final double f = matrix[1][2];
        final double i = matrix[2][2];

        matrix1[0][0] = (e * i - f * h) / MatrixMethod.determinant(matrix);
        matrix1[0][1] = (c * h - b * i) / MatrixMethod.determinant(matrix);
        matrix1[0][2] = (b * f - c * e) / MatrixMethod.determinant(matrix);
        matrix1[1][0] = (f * g - d * i) / MatrixMethod.determinant(matrix);
        matrix1[1][1] = (a * i - c * g) / MatrixMethod.determinant(matrix);
        matrix1[1][2] = (c * d - a * f) / MatrixMethod.determinant(matrix);
        matrix1[2][0] = (d * h - e * g) / MatrixMethod.determinant(matrix);
        matrix1[2][1] = (b * g - a * h) / MatrixMethod.determinant(matrix);
        matrix1[2][2] = (a * e - b * d) / MatrixMethod.determinant(matrix);

        return matrix1;
    }

    /**
     * An exception sub-class to signal a singular matrix.
     * 
     * @author Daniel Lefevre
     */
    public static final class SingularMatrixException extends Exception {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        private SingularMatrixException() {
        }
    }
}
