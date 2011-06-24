package CodeFinal;

import javax.vecmath.Vector3d;

/**
 * A set of untested methods designed to handle basis change
 * 
 *
 */

public class MatrixMethod {

	public static double[][] createOrthoBase(Vector3d baseVector){
		Vector3d b = new Vector3d(baseVector);
		b.normalize();

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

	//	public static double[][] createOrthoBase3Dfrom2D(Vector3d vect1, Vector3d vect2) {
	//		double[][] matrix1 = new double[3][3];
	//		
	//		Vector3d b = new Vector3d(vect1), c = new Vector3d(vect2);
	//		b.normalize();
	//		c.normalize();
	//			
	//		matrix1[0][0]=b.x;
	//		matrix1[1][0]=b.y;
	//		matrix1[0][1]=c.x;
	//		matrix1[1][1]=c.y;
	//		matrix1[2][2] = 1;
	//		matrix1[0][2] = 0;
	//		matrix1[1][2] = 0;
	//		matrix1[2][0] = 0;
	//		matrix1[2][1] = 0;
	//		
	//		return matrix1;
	//	}
	//	
	//	public static double[][] getInversOrthoBase3Dfrom2D(double[][] matrix) {
	//		double[][] matrix1 = new double[3][3];
	//		
	//		matrix1[0][0]=matrix[1][1];
	//		matrix1[0][1]=-matrix[0][1];
	//		matrix1[1][0]=-matrix[1][0];
	//		matrix1[1][1]=matrix[0][0];
	//		matrix1[2][2] = 1;
	//		matrix1[0][2] = 0;
	//		matrix1[1][2] = 0;
	//		matrix1[2][0] = 0;
	//		matrix1[2][1] = 0;
	//		
	//		return matrix1;
	//	}
	//	
	//	public static double[][] createOrthoBase2D(Vector3d vect1, Vector3d vect2){
	//		Vector3d b = new Vector3d(vect1), c = new Vector3d(vect2);
	//		b.normalize();
	//		c.normalize();
	//	
	//		//Gram-Schmidt
	//		double[][]matrix1= new double[2][2];
	//		
	//		matrix1[0][0]=b.x;
	//		matrix1[1][0]=b.y;
	//		matrix1[0][1]=c.x;
	//		matrix1[1][1]=c.y;
	//	
	//		return matrix1;
	//	}
	//	
	//	public static double[][] getInverseOrthoBase2D(double[][] matrix) {
	//		double[][] matrix1 = new double[2][2];
	//		
	//		matrix1[0][0]=matrix[1][1];
	//		matrix1[0][1]=-matrix[0][1];
	//		matrix1[1][0]=-matrix[1][0];
	//		matrix1[1][1]=matrix[0][0];
	//		
	//		return matrix1;
	//	}

	public static double[][] getInversMatrix(double[][] matrix) {
		double[][] matrix1 = new double[3][3];
		
		double a=matrix[0][0];
		double d=matrix[1][0];
		double g=matrix[2][0];
		double b=matrix[0][1];
		double e=matrix[1][1];
		double h=matrix[2][1];
		double c=matrix[0][2];
		double f=matrix[1][2];
		double i=matrix[2][2];

		matrix1[0][0]=e*i-f*h;
		matrix1[0][1]=c*h-b*i;
		matrix1[0][2]=b*f-c*e;
		matrix1[1][0]=f*g-d*i;
		matrix1[1][1]=a*i-c*g;
		matrix1[1][2]=c*d-a*f;
		matrix1[2][0]=d*h-e*g;
		matrix1[2][1]=b*g-a*h;
		matrix1[2][2]=a*e-b*d;

		return matrix1;
	}

	public static double[][] createOrthoBase(Vector3d vect1, Vector3d vect2, Vector3d vect3){
		Vector3d b = new Vector3d(vect1), c = new Vector3d(vect2), d  = new Vector3d(vect3);
		b.normalize();
		c.normalize();
		d.normalize();

		//Gram-Schmidt
		double[][]matrix1= new double[3][3];

//		matrix1[0][0]=b.x;
//		matrix1[1][0]=b.y;
//		matrix1[2][0]=b.z;
//		matrix1[0][1]=c.x;
//		matrix1[1][1]=c.y;
//		matrix1[2][1]=c.z;
//		matrix1[0][2]=d.x;
//		matrix1[1][2]=d.y;
//		matrix1[2][2]=d.z;
		
		matrix1[0][0]=b.x;
		matrix1[0][1]=b.y;
		matrix1[0][2]=b.z;
		matrix1[1][0]=c.x;
		matrix1[1][1]=c.y;
		matrix1[1][2]=c.z;
		matrix1[2][0]=d.x;
		matrix1[2][1]=d.y;
		matrix1[2][2]=d.z;

		return matrix1;
	}

	public static double[] changeBase(double[] coord, double[][]matrix){
		int n=coord.length;
		double[] sol = new double[n];
		for(int i=0;i<n;i++){
			sol[i]=0;
			for(int j=0;j<n;j++){
				sol[i]=sol[i]+matrix[i][j]*coord[j];
			}
		}
		return sol;
	}

	public static Vector3d changeBase(Vector3d vect, double[][] matrix){
		double[] coord = new double[3];
		coord[0]=vect.x;coord[1]=vect.y;coord[2]=vect.z;
		return new Vector3d(MatrixMethod.changeBase(coord, matrix));
	}
}
