package algos;

import java.io.IOException;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import modeles.Mesh;
import modeles.Triangle;
import utils.MatrixMethod;
import utils.MatrixMethod.SingularMatrixException;
import utils.Parser;
import utils.Parser.BadFormedFileException;
import utils.Writer;

public class SeparationFloorBuilding {

	private double altitudeErrorFactor = 10;
	private double angleNormalErrorFactor = 60;
	private double blockSizeBuildingError = 1;

	private String townFileName = "Originals/batiments 3 - binary.stl";
	private String floorFileName = "Originals/floor.stl";

	private int WRITING_MODE = Writer.BINARY_MODE;

	private Mesh floors = new Mesh();
	private Mesh buildings = new Mesh();

	private ArrayList<Mesh> buildingList = new ArrayList<Mesh>();
	private Mesh noise = new Mesh();

	public SeparationFloorBuilding() {
		altitudeErrorFactor = 10;
		angleNormalErrorFactor = 60;
		blockSizeBuildingError = 1;

		townFileName = "Originals/batiments 3 - binary.stl";
		floorFileName = "Originals/floor.stl";

		Writer.setWriteMode(WRITING_MODE);

		floors = new Mesh();
		buildings = new Mesh();

		buildingList = new ArrayList<Mesh>();
		noise = new Mesh();
	}

	public void apply() {
		this.parseFiles();
		this.extractFloorNormal();
		this.changeBase();
		this.floorExtraction();
		this.buildingsExtraction();
		this.noiseTreatment();
	}
	
	private void parseFiles() {
		
	}
	
	private void extractFloorNormal() {
		
	}
	
	private void changeBase() {
		
	}

	private void floorExtraction() {
		{
			//First part !
			//Parsing
			System.out.println("Parsing ...");
			try {
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

					lowestTriangle = meshOriented.faceUnderZ(lowestZ + altitudeErrorFactor);
				}

				mesh.remove(floors);

				buildings = mesh;

			} catch (BadFormedFileException e) {
				System.err.println("Error : the file is badly formed !");
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Error : file not found or inreadible !");
				System.exit(1);
			} catch (SingularMatrixException e) {
				System.err.println("Error : the matrix is badly formed !");
				System.exit(1);
			}
		}
	}

	private void buildingsExtraction() {
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
	}

	public void noiseTreatment() {
		//Third part !
		Mesh floorsAndNoise = new Mesh(floors);
		floorsAndNoise.addAll(noise);

		ArrayList<Mesh> floorsList = new ArrayList<Mesh>();

		//TODO : faire le blockExtract(floorsAndNoise) plutôt !
		floorsList = Algos.blockExtract(floors);

		//If a noise block is a neighbour of a the real floor, it's added to the real floor

		floors.clear();

		for(Mesh e : floorsList) {
			e.getOne().returnNeighbours(floors, floorsAndNoise);
		}

		//TODO : remettre le reste des bruits dans un autre fichier

		floors.write("Files/wholeFloors.stl");
	}
}
