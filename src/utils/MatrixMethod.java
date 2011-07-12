package utils;

import javax.vecmath.Vector3d;

/**
 * @author Eric Berthe, Valentin Roger, Daniel Lefèvre
 *
 */
/**
 * @author CFV
 *
 */
public class MatrixMethod {

	/**
	 * @param baseVector
	 * @return
	 * @throws SingularMatrixException
	 */
	public static double[][] createOrthoBase(Vector3d baseVector) throws SingularMatrixException{
		Vector3d b = new Vector3d(baseVector);
		b.normalize();

		if(b.z > -0.95 && b.z < -1.05)
			throw new SingularMatrixException();

		//Gram-Schmidt
		double[][]matrix1= new double[3][3];

		matrix1[0][2]=b.x;
		matrix1[1][2]=b.y;
		matrix1[2][2]=b.z;
		matrix1[2][0]=b.x;
		matrix1[2][1]=b.y;

		matrix1[1][1]=-1+(1/(b.z+1))*b.y*b.y;
		matrix1[0][1]=(1/(b.z+1))*b.y*b.x;
		matrix1[1][0]=(1/(b.z+1))*b.y*b.x;
		matrix1[0][0]=-1+(1/(b.z+1))*b.x*b.x;

		return matrix1;
	}

	/**
	 * @param matrix
	 * @return
	 */
	public static double determinant(double[][] matrix) {
		double a = matrix[0][0];
		double d = matrix[1][0];
		double g = matrix[2][0];
		double b = matrix[0][1];
		double e = matrix[1][1];
		double h = matrix[2][1];
		double c = matrix[0][2];
		double f = matrix[1][2];
		double i = matrix[2][2];

		return a*e*i + b*f*g + c*d*h - c*e*g - f*h*a - i*b*d;
	}

	/**
	 * @param matrix
	 * @return
	 * @throws SingularMatrixException
	 */
	public static double[][] getInversMatrix(double[][] matrix) throws SingularMatrixException {
		double[][] matrix1 = new double[3][3];

		if(MatrixMethod.determinant(matrix) < 0.95 
				|| MatrixMethod.determinant(matrix) > 1.05)
			throw new SingularMatrixException();

		double a = matrix[0][0];
		double d = matrix[1][0];
		double g = matrix[2][0];
		double b = matrix[0][1];
		double e = matrix[1][1];
		double h = matrix[2][1];
		double c = matrix[0][2];
		double f = matrix[1][2];
		double i = matrix[2][2];

		matrix1[0][0] = e*i - f*h;
		matrix1[0][1] = c*h - b*i;
		matrix1[0][2] = b*f - c*e;
		matrix1[1][0] = f*g - d*i;
		matrix1[1][1] = a*i - c*g;
		matrix1[1][2] = c*d - a*f;
		matrix1[2][0] = d*h - e*g;
		matrix1[2][1] = b*g - a*h;
		matrix1[2][2] = a*e - b*d;

		return matrix1;
	}

	/**
	 * @param vect1
	 * @param vect2
	 * @param vect3
	 * @return
	 * @throws SingularMatrixException
	 */
	public static double[][] createOrthoBase(Vector3d vect1, Vector3d vect2, Vector3d vect3) throws SingularMatrixException{
		Vector3d b = new Vector3d(vect1), c = new Vector3d(vect2), d  = new Vector3d(vect3);
		b.normalize();
		c.normalize();
		d.normalize();

		//Gram-Schmidt
		double[][]matrix1 = new double[3][3];

		matrix1[0][0] = b.x;
		matrix1[0][1] = b.y;
		matrix1[0][2] = b.z;
		matrix1[1][0] = c.x;
		matrix1[1][1] = c.y;
		matrix1[1][2] = c.z;
		matrix1[2][0] = d.x;
		matrix1[2][1] = d.y;
		matrix1[2][2] = d.z;

		if(MatrixMethod.determinant(matrix1) < 0.95 
				|| MatrixMethod.determinant(matrix1) > 1.05)
			throw new SingularMatrixException();

		return matrix1;
	}

	/**
	 * @param coord
	 * @param matrix
	 * @return
	 */
	public static double[] changeBase(double[] coord, double[][]matrix){
		int n = coord.length;
		double[] sol = new double[n];

		for(int i = 0 ; i < n ; i ++) {
			sol[i] = 0;
			for(int j = 0 ; j < n ; j ++) {
				sol[i] += matrix[i][j] * coord[j];
			}
		}
		return sol;
	}

	/**
	 * @param vect
	 * @param matrix
	 */
	public static void changeBase(Vector3d vect, double[][] matrix){
		double[] coord = {vect.x, vect.y, vect.z};
		vect.set(MatrixMethod.changeBase(coord, matrix));
	}

	//FIXME : changer de nom : elle existe déjà dans Vecmath !
	/**
	 * @author CFV
	 *
	 */
	public static class SingularMatrixException extends Exception {
		private static final long serialVersionUID = 1L;
	}
}
