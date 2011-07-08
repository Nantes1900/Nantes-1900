package mains;

import java.io.File;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import modeles.Mesh;
import utils.MatrixMethod;
import utils.Parser;

public class Main2 {
	public static void main(String[] args) {
		try
		{
			//Parsing
			int buildingCounter = 1;

			ArrayList<Mesh> buildingList = new ArrayList<Mesh>();
			while(new File("building - " + buildingCounter + ".stl").exists()) {
				buildingList.add(new Mesh(Parser.readSTL("building - " + buildingCounter + ".stl")));
				buildingCounter ++;
			}

			Mesh floors = new Mesh(Parser.readSTL("sol.stl"));

			//Floor normal
			Vector3d normalFloor = floors.averageNormal();

			//Base change
			double[][] matrix = MatrixMethod.createOrthoBase(normalFloor);
			MatrixMethod.changeBase(normalFloor, matrix);
			
			System.out.println(normalFloor);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}