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
	
	public static double[][] createOrthoBase(Vector3d vect1, Vector3d vect2){
		Vector3d b = new Vector3d(vect1), c = new Vector3d(vect2);
		b.normalize();
		c.normalize();
		
		Vector3d d = new Vector3d();
		d.cross(b, c);
	
		//Gram-Schmidt
		double[][]matrix1= new double[3][3];
		
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
