package algos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.vecmath.Vector3d;

import modeles.Mesh;
import utils.MatrixMethod;
import utils.MatrixMethod.SingularMatrixException;
import utils.Parser;
import utils.Parser.BadFormedFileException;
import utils.Writer;

public class SeparationFloorBuilding {

	private double altitudeErrorFactor = 30;
	private double angleNormalErrorFactor = 15;
	private double blockSizeBuildingError = 1;

	private String townFileName = "Originals/batiments 1 - binary.stl";
	private String floorFileName = "Originals/floor.stl";

	private Mesh floorBrut = new Mesh();
	private Vector3d normalFloor = new Vector3d();

	private Mesh mesh = new Mesh();
	
	private Mesh buildings = new Mesh();
	private Mesh floors = new Mesh();
	private Mesh noise = new Mesh();

	private ArrayList<Mesh> buildingList = new ArrayList<Mesh>();
	private ArrayList<Mesh> floorsList = new ArrayList<Mesh>();


	private int WRITING_MODE = Writer.BINARY_MODE;
	private boolean DEBUG_MODE = false;

	private Logger log = Logger.getLogger("logger");


	public SeparationFloorBuilding(String fileName) {

		this();
		this.townFileName = fileName;
	}

	public SeparationFloorBuilding() {

		//Writing option set
		Writer.setWriteMode(WRITING_MODE);

		this.log.setLevel(Level.INFO);
		this.log.setUseParentHandlers(false);
		this.log.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
	}

	public void setDebugMode() {
		this.log.setLevel(Level.INFO);
		this.DEBUG_MODE = true;

		this.WRITING_MODE = Writer.ASCII_MODE;
		Writer.setWriteMode(WRITING_MODE);
	}


	public void apply() {

		this.parseFiles();
		this.extractFloorNormal();
		this.changeBase();
		this.floorExtraction();
		this.buildingsExtraction();
		this.noiseTreatment();

		//TODO : remettre le reste des bruits dans un autre fichier
	}


	private void parseFiles() {

		//Parsing
		log.info("Parsing...");

		try {
			this.mesh = new Mesh(Parser.readSTL(townFileName));
			this.floorBrut = new Mesh(Parser.readSTL(floorFileName));

		} catch (BadFormedFileException e) {
			log.severe("Error : the file is badly formed !");
			System.exit(1);

		} catch (IOException e) {
			log.severe("Error : file not found or inreadible !");
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
			System.exit(1);
		}
	}

	private void floorExtraction() {

		log.info("Floor extraction...");

		//Searching for floor-oriented triangles with an error : angleNormalErrorFactor
		Mesh meshOriented = mesh.orientedAs(normalFloor, angleNormalErrorFactor);
		meshOriented.write("test.stl");

		//Floor-search algorithm
		//Select the lowest Triangle : it belongs to the floor
		//Take all of its neighbours
		//Select another Triangle which is not too high and repeat the above step		
		this.floors = Algos.floorExtract(meshOriented, this.altitudeErrorFactor);

		this.buildings = new Mesh(this.mesh);

		this.buildings.remove(this.floors);

		log.info("Floor extracted !");
	}

	private void buildingsExtraction() {

		//Extraction of the buildings
		log.info("Extracting building ...");
		ArrayList<Mesh> formsList = Algos.blockExtract(this.buildings);

		//Separation of the little noises
		for(Mesh m : formsList) {
			if(m.size() > 1)
				this.buildingList.add(m);
			else
				this.noise.addAll(m);
		}

		//Algorithm : detection of buildings considering their size
		int number = 0;
		for(Mesh m : this.buildingList) {
			number += m.size();
		}

		int buildingCounter = 1;

		log.info("Building writing ...");
		for(Mesh m : this.buildingList) {
			if(m.size() > this.blockSizeBuildingError * (double)number/(double)this.buildingList.size()) {
				//FIXME : faire une autre méthode private pour l'écriture.
				m.write("Files/building - " + buildingCounter + ".stl");
				buildingCounter ++;
			}
			else {
				this.noise.addAll(m);
			}
		}

		if(DEBUG_MODE)
			noise.write("Files/noise.stl");

		log.info("Buildings extracted !");
	}

	public void noiseTreatment() {

		Mesh floorsAndNoise = new Mesh(floors);
		floorsAndNoise.addAll(noise);
		floorsList = Algos.blockExtract(floorsAndNoise);
		
		floors.clear();
		for(Mesh e : floorsList) {
			floors.addAll(e);
		}
	}
}
