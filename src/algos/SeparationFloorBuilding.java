package algos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private Mesh noise = new Mesh();
	private Mesh mesh = new Mesh();
	private Mesh floorBrut = new Mesh();

	private ArrayList<Mesh> buildingList = new ArrayList<Mesh>();

	private Logger log = Logger.getLogger("logger");

	private Vector3d normalFloor = new Vector3d();

	public SeparationFloorBuilding(String fileName) {

		this.townFileName = fileName;

		//Writing option set
		Writer.setWriteMode(WRITING_MODE);

		this.log.setLevel(Level.INFO);
	}

	public SeparationFloorBuilding() {

		//Writing option set
		Writer.setWriteMode(WRITING_MODE);

		this.log.setLevel(Level.INFO);
	}

	public void setModeDebug() {
		this.WRITING_MODE = Writer.ASCII_MODE;
		Writer.setWriteMode(WRITING_MODE);

		this.log.setLevel(Level.FINEST);
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
		//Parsing
		log.info("Parsing...");

		try {

			this.mesh = new Mesh(Parser.readSTL(townFileName));
			this.floorBrut = new Mesh(Parser.readSTL(floorFileName));

		} catch (BadFormedFileException e) {

			log.severe("Error : the file is badly formed !");
			//FIXME : les exit sont pas géniaux...
			System.exit(1);

		} catch (IOException e) {

			log.severe("Error : file not found or inreadible !");
			//FIXME : les exit sont pas géniaux...
			System.exit(1);

		}

		log.info("Parsing ended !");
	}

	private void extractFloorNormal() {
		//Extract of the normal of the floor
		this.normalFloor = this.floorBrut.averageNormal();
	}

	private void changeBase() {
		try {

			log.info("Base change...");

			//Base change
			double[][] matrix = MatrixMethod.createOrthoBase(normalFloor);
			MatrixMethod.changeBase(normalFloor, matrix);

			this.mesh.changeBase(matrix);

			log.info("Base changed finished !");

		} catch (SingularMatrixException e) {
			log.severe("Error : the matrix is badly formed !");
			//FIXME : les exit sont pas géniaux...
			System.exit(1);
		}
	}

	private void floorExtraction() {

		log.info("Floor extraction...");

		//Searching for floor-oriented triangles with an error : angleNormalErrorFactor
		log.info("Searching for floor-oriented triangles...");
		Mesh meshOriented = mesh.orientedAs(normalFloor, angleNormalErrorFactor);
		log.info("Searching finished !");

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
			log.info("Number of triangles left : " + meshOriented.size() + " on " + size);

			temp.clear();

			lowestTriangle.returnNeighbours(temp, meshOriented);

			floors.addAll(temp);

			meshOriented.remove(temp);

			lowestTriangle = meshOriented.faceUnderZ(lowestZ + altitudeErrorFactor);
		}

		mesh.remove(floors);

		buildings = mesh;

		log.info("Floor extracted !");
	}

	private void buildingsExtraction() {

		//Extraction of the buildings
		log.info("Extracting building ...");
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

		log.info("Building writing ...");
		for(Mesh m : buildingList) {
			if(m.size() > blockSizeBuildingError * (double)number/(double)buildingList.size()) {
				m.write("Files/building - " + buildingCounter + ".stl");
				buildingCounter ++;
			}
			else {
				noise.addAll(m);
			}
		}

		if(log.getLevel() == Level.FINEST)
			noise.write("Files/noise.stl");

		log.info("Buildings extracted !");
	}

	public void noiseTreatment() {

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
