package algos;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

import modeles.Mesh;
import modeles.Triangle;
import utils.MatrixMethod;
import utils.Parser;
import utils.Writer;

public class Test1 {
	public static boolean DEBUG = true;
	
	public static void main(String[] args) {
		try
		{
			double altitudeErrorFactor = 10;
			double angleNormalErrorFactor = 60;
			double blockSizeBuildingError = 1;
			
			String townFileName = "Originals/batiments 2 - binary.stl";
			String floorFileName = "Originals/floor.stl";
			
			Writer.setWriteMode(Writer.BINARY_MODE);

			Mesh floors = new Mesh();
			Mesh buildings = new Mesh();

			ArrayList<Mesh> buildingList = new ArrayList<Mesh>();
			Mesh noise = new Mesh();

			{
				//First part !
				//Parsing
				System.out.println("Parsing ...");
				Mesh mesh = new Mesh(Parser.readSTL(townFileName));
				Mesh floorBrut = new Mesh(Parser.readSTL(floorFileName));

				//Extract of the normal of the floor
				Vector3d normalFloor = floorBrut.averageNormal();

				//Base change
				double[][] matrix = MatrixMethod.createOrthoBase(normalFloor);
				MatrixMethod.changeBase(normalFloor, matrix);
				mesh = mesh.changeBase(matrix);
				System.out.println("Base changed finished !");

				//Searching for floor-oriented triangles with an error : angleNormalErrorFactor
				//TODO : convertir l'erreur de radians en degrés
				System.out.println("Searching for floor-oriented triangles...");
				Mesh meshOriented = mesh.orientedAs(normalFloor, angleNormalErrorFactor);
				System.out.println("Searching finished !");

				//Floor-search algorithm
				//Select the lowest Triangle : it belongs to the floor
				//Take all of its neighbours
				//Select another Triangle which is not too high and repeat the above step			
				int size = meshOriented.size();
				Mesh temp = new Mesh();

				Triangle lowestTriangle = meshOriented.zMinFace();
				double lowestZ = lowestTriangle.zMin();

				while(lowestTriangle != null)
				{
					System.out.println("Number of triangles left : " + meshOriented.size() + " on " + size);

					temp.clear();
					lowestTriangle.returnNeighbours(temp, meshOriented);
					
					floors.addAll(temp);
					
					meshOriented.remove(temp);
					
					lowestTriangle = meshOriented.zMinFaceUnder(lowestZ + altitudeErrorFactor);
				}
				
				mesh.remove(floors);
				
				buildings = mesh;
			}

			if(DEBUG) {
				//Floor writing
				floors.write("Files/floorMesh.stl");
				System.out.println("Floor written !");

				//Building writing
				buildings.write("Files/buildingMesh.stl");
				System.out.println("Building written !");
			}


			{
				//Second part !
				//Extraction of the buildings
				System.out.println("Extracting building ...");
				ArrayList<Mesh> formsList = Algos.blockExtract(buildings);

				//Separation of the little noises
				for(Mesh m : formsList) {
					if(m.size() > 1)
						buildingList.add(m);
					else
						noise.addAll(m);
				}

				//Algorithm : detection of buildings considering their size
				int number = 0;
				for(Mesh m : buildingList) {
					number += m.size();
				}

				int buildingCounter = 1;

				System.out.println("Building writing ...");
				for(Mesh m : buildingList) {
					if(m.size() > blockSizeBuildingError * (double)number/(double)buildingList.size()) {
						m.write("Files/building - " + buildingCounter + ".stl");
						buildingCounter ++;
					}
					else {
						noise.addAll(m);
					}
				}

				noise.write("Files/noise.stl");
			}

			{
				//Third part !
//				Mesh floorsAndNoise = new Mesh(floors);
//				floorsAndNoise.addAll(noise);
//
//				ArrayList<Mesh> floorsList = new ArrayList<Mesh>();
//
//				//TODO : faire le blockExtract(floorsAndNoise) plutôt !
//				floorsList = Algos.blockExtract(floors);
//
//				//If a noise block is a neighbour of a the real floor, it's added to the real floor
////				floorsAndNoise.findNeighbours();
//
//				floors.clear();
//
//				for(Mesh e : floorsList) {
//					e.getOne().returnNeighbours(floors);
//				}
//				
//				//TODO : remettre le reste des bruits dans un autre fichier
//
//				floors.write("Files/wholeFloors.stl");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
