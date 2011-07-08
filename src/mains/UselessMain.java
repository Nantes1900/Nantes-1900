package mains;

import javax.vecmath.Vector3d;

import modeles.Mesh;
import utils.MatrixMethod;
import utils.Parser;
import utils.Writer;

public class UselessMain {
	public static void main(String[] args) {
		try
		{
			Mesh mesh = new Mesh(Parser.readSTL("Files/batiments 1 - binary.stl"));
			Mesh floor = new Mesh(Parser.readSTL("Files/floor.stl"));
			
			Vector3d normalFloorBadOriented = floor.averageNormal();
			
			double[][] matrix = MatrixMethod.createOrthoBase(normalFloorBadOriented);
			
			mesh.changeBase(matrix);
			
			Writer.setWriteMode(Writer.BINARY_MODE);
			mesh.write("test.stl");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
