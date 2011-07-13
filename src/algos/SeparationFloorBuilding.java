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
import utils.ParserSTL;
import utils.ParserSTL.BadFormedFileException;
import utils.WriterSTL;

/**
 * @author Daniel Lefevre
 * 
 */
public class SeparationFloorBuilding {

	private double altitudeErrorFactor = 10;
	private double angleNormalErrorFactor = 20;
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

	private int WRITING_MODE = WriterSTL.BINARY_MODE;
	private boolean DEBUG_MODE = false;

	private Logger log = Logger.getLogger("logger");

	/**
	 * @param fileName
	 */
	public SeparationFloorBuilding(String fileName) {

		this();
		this.townFileName = fileName;
	}

	/**
	 * 
	 */
	public SeparationFloorBuilding() {

		// Writing option set
		WriterSTL.setWriteMode(WRITING_MODE);

		this.log.setLevel(Level.INFO);
		this.log.setUseParentHandlers(false);
		this.log.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
	}

	/**
	 * 
	 */
	public void setDebugMode() {
		this.log.setLevel(Level.INFO);
		this.DEBUG_MODE = true;

		this.WRITING_MODE = WriterSTL.ASCII_MODE;
		WriterSTL.setWriteMode(WRITING_MODE);
	}

	/**
	 * 
	 */
	public void apply() {

		this.parseFiles();
		this.extractFloorNormal();
		this.changeBase();
		this.floorExtraction();
		this.buildingsExtraction();
		this.noiseTreatment();

		this.writeBuildings();
		this.writeFloor();

		if (DEBUG_MODE)
			noise.write("Files/noise.stl");
	}

	/**
	 * 
	 */
	private void writeFloor() {
		this.floors.write("Files/floors.stl");
	}

	/**
	 * 
	 */
	private void writeBuildings() {
		int buildingCounter = 1;

		for (Mesh m : this.buildingList) {
			m.write("Files/building - " + buildingCounter + ".stl");
			buildingCounter++;
		}
	}

	/**
	 * 
	 */
	private void parseFiles() {

		// Parsing
		log.info("Parsing...");

		try {
			this.mesh = new Mesh(ParserSTL.readSTL(townFileName));
			this.floorBrut = new Mesh(ParserSTL.readSTL(floorFileName));

		} catch (BadFormedFileException e) {
			log.severe("Error : the file is badly formed !");
			System.exit(1);

		} catch (IOException e) {
			log.severe("Error : file not found or inreadible !");
			System.exit(1);
		}

		log.info("Parsing ended !");
	}

	/**
	 * 
	 */
	private void extractFloorNormal() {

		// Extract of the normal of the floor
		this.normalFloor = this.floorBrut.averageNormal();
	}

	/**
	 * 
	 */
	private void changeBase() {

		try {
			log.info("Base change...");

			// Base change
			double[][] matrix = MatrixMethod.createOrthoBase(normalFloor);
			MatrixMethod.changeBase(normalFloor, matrix);

			this.mesh.changeBase(matrix);

			log.info("Base changed finished !");

		} catch (SingularMatrixException e) {
			log.severe("Error : the matrix is badly formed !");
			System.exit(1);
		}
	}

	/**
	 * 
	 */
	private void floorExtraction() {

		log.info("Floor extraction...");

		// Searching for floor-oriented triangles with an error :
		// angleNormalErrorFactor
		Mesh meshOriented = mesh
				.orientedAs(normalFloor, angleNormalErrorFactor);

		// Floor-search algorithm
		// Select the lowest Triangle : it belongs to the floor
		// Take all of its neighbours
		// Select another Triangle which is not too high and repeat the above
		// step
		this.floors = Algos
				.floorExtract(meshOriented, this.altitudeErrorFactor);

		this.buildings = new Mesh(this.mesh);

		this.buildings.remove(this.floors);

		log.info("Floor extracted !");
	}

	/**
	 * 
	 */
	private void buildingsExtraction() {

		// Extraction of the buildings
		log.info("Extracting building ...");
		ArrayList<Mesh> formsList = Algos.blockExtract(this.buildings);
		ArrayList<Mesh> thingsList = new ArrayList<Mesh>();

		// Separation of the little noises
		for (Mesh m : formsList) {
			if (m.size() > 1)
				thingsList.add(m);
			else
				this.noise.addAll(m);
		}

		// Algorithm : detection of buildings considering their size
		int number = 0;
		for (Mesh m : this.buildingList) {
			number += m.size();
		}

		log.info("Building extraction ...");

		for (Mesh m : thingsList) {
			if (m.size() >= this.blockSizeBuildingError * (double) number
					/ (double) this.buildingList.size()) {
				this.buildingList.add(m);
			} else {
				this.noise.addAll(m);
			}
		}

		if (this.buildingList.size() == 0)
			log.severe("Error !");

		log.info("Buildings extracted !");
	}

	/**
	 * 
	 */
	public void noiseTreatment() {

		Mesh floorsAndNoise = new Mesh(this.floors);
		floorsAndNoise.addAll(this.noise);
		this.floorsList = Algos.blockExtract(floorsAndNoise);

		this.floors.clear();
		for (Mesh e : this.floorsList) {
			this.floors.addAll(e);
			this.noise.remove(e);
		}
	}
}
