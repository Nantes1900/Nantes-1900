package algos;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

import modeles.Mesh;
import modeles.Triangle;
import utils.MatrixMethod;
import utils.Parser;

public class Test1 {
	public static boolean DEBUG = false;
	
	public static void main(String[] args) {
		try
		{
			double altitudeErrorFactor = 10;
			double angleNormalErrorFactor = 0.6;
			double blockSizeBuildingError = 1;
			
			String townFileName = "Files/batiments 2 - binary.stl";
			String floorFileName = "Files/floor.stl";

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
				Vector3d normalFloorBadOriented = floorBrut.averageNormal();

				//Base change
				double[][] matrix = MatrixMethod.createOrthoBase(normalFloorBadOriented);
				Vector3d normalFloor = MatrixMethod.changeBase(normalFloorBadOriented, matrix);
				mesh.changeBase(matrix);
				System.out.println("Base changed finished !");

				//Searching for floor-oriented triangles with an error : angleNormalErrorFactor
				//TODO : convertir l'erreur de radians en degrés
				System.out.println("Searching for floor-oriented triangles...");
				Mesh meshOriented = mesh.orientedAs(normalFloor, angleNormalErrorFactor);
				System.out.println("Searching finished !");

				//Index creation
				//FIXME : bien régler les paramétres de tuilage: cela crée des pbs parfois.
//				new Grid(meshOriented).findNeighbours();
				meshOriented.findNeighbours();

				//Floor-search algorithm
				//Select the lowest Triangle : it belongs to the floor
				//Take all of its neighbours
				//Select another Triangle which is not too high and repeat the above step			
				int size = meshOriented.size();
				Mesh temp = new Mesh();

				Triangle lowestTriangle = meshOriented.zMinFace();
				double lowestZ = lowestTriangle.zMin();

				//TODO : on peut même enlever le lowestTriangle.sMin() < lowestZ, etc... parce que c'est au fond de la boucle...
				while(lowestTriangle != null && lowestTriangle.zMin() < lowestZ + altitudeErrorFactor)
				{
					System.out.println("Number of triangles left : " + meshOriented.size() + " on : " + size);
					temp.clear();

					lowestTriangle.returnNeighbours(temp);
					floors.addAll(temp);

					meshOriented.remove(temp);
					mesh.remove(temp);
					lowestTriangle = meshOriented.zMinFaceUnder(lowestZ + altitudeErrorFactor);
				}

				mesh.clearNeighbours();

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
				//Neighbours index creation
				//TODO : gérer tout seul les coeff
				buildings.findNeighbours();

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
				Mesh floorsAndNoise = new Mesh(floors);
				floorsAndNoise.addAll(noise);

				ArrayList<Mesh> floorsList = new ArrayList<Mesh>();

				//TODO : faire le blockExtract(floorsAndNoise) plutôt !
				floorsList = Algos.blockExtract(floors);

				//If a noise block is a neighbour of a the real floor, it's added to the real floor
				floorsAndNoise.findNeighbours();

				floors.clear();

				for(Mesh e : floorsList) {
					e.getOne().returnNeighbours(floors);
				}
				
				//TODO : remettre le reste des bruits dans un autre fichier

				floors.write("Files/wholeFloors.stl");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
