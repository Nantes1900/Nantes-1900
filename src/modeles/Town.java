package modeles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.vecmath.Vector3d;

import algos.SeparationFloorBuilding;

import utils.Algos;
import utils.MatrixMethod;
import utils.MatrixMethod.SingularMatrixException;
import utils.ParserSTL;
import utils.ParserSTL.BadFormedFileException;
import utils.WriterCityGML;

@SuppressWarnings("unused")
public class Town {
	private ArrayList<Edifice> buildings = new ArrayList<Edifice>();
	private ArrayList<Floor> floors = new ArrayList<Floor>();
	private ArrayList<Mesh> specialBuildings = new ArrayList<Mesh>();

	private double[][] matrix = new double[3][3];

	private Logger log = Logger.getLogger("logger");

	public Town() {
	}

	public void buildFromMesh(String directoryName) {

		Vector3d normalFloor = this.extractFloorNormal(directoryName
				+ "/floor.stl");
		this.createChangeBaseMatrix(normalFloor);

		this.treatFloors(directoryName + "/floors");
		this.treatWateries(directoryName + "/wateries");

		this.treatSpecialBuildings(directoryName + "/special_buildings");

		this.treatIndustries(directoryName + "/industries");
		this.treatResidentials(directoryName + "/residentials");
	}

	private void treatFloors(String directoryName) {
		int counterFloors = 1;

		while (new File(directoryName + "floor - " + counterFloors + ".stl")
				.exists()) {

			Floor floor = new Floor("floor");

			floor.buildFromMesh(this.parseFile(directoryName + "floor - "
					+ counterFloors + ".stl"));

			floor.changeBase(this.matrix);

			this.addFloor(floor);

			counterFloors++;
		}
	}

	private void treatWateries(String directoryName) {
		int counterWateries = 1;

		while (new File(directoryName + "watery - " + counterWateries + ".stl")
				.exists()) {

			Floor watery = new Floor("watery");

			watery.buildFromMesh(this.parseFile(directoryName + "watery - "
					+ counterWateries + ".stl"));

			watery.changeBase(this.matrix);

			this.addFloor(watery);

			counterWateries++;
		}
	}

	private void treatSpecialBuildings(String directoryName) {
		int counterSpecialBuildings = 1;

		while (new File(directoryName + "special_building - "
				+ counterSpecialBuildings + ".stl").exists()) {

			SpecialBuilding specialBuilding = new SpecialBuilding();
			Floor floor = new Floor("street");

			// FIXME : floor extraction first
			//
			//
			//

			specialBuilding
					.buildFromMesh(this.parseFile(directoryName
							+ "special_building - " + counterSpecialBuildings
							+ ".stl"));

			specialBuilding.changeBase(this.matrix);

			this.addSpecialBuilding(specialBuilding);
			this.addFloor(floor);

			counterSpecialBuildings++;
		}
	}

	private void treatIndustries(String directoryName) {
		int counterIndustries = 1;

		while (new File(directoryName + "industries - " + counterIndustries
				+ ".stl").exists()) {

			// FIXME : floor extraction
			// FIXME : carve all of this in many buildings, and be sure that
			// they really are buildings
			// FIXME : noise treatment, etc...
			// FIXME : then buildFromMesh new Edifices

			counterIndustries++;
		}

	}

	private void treatResidentials(String directoryName) {
		int counterResidentials = 1;

		while (new File(directoryName + "residentials - " + counterResidentials
				+ ".stl").exists()) {

			// FIXME : floor extraction
			// FIXME : carve all of this in many buildings, and be sure that
			// they really are buildings
			// FIXME : noise treatment, etc...
			// FIXME : then buildFromMesh new Edifices

			counterResidentials++;
		}
	}

	public void addBuilding(Edifice building) {
		if (!this.buildings.contains(building))
			this.buildings.add(building);
	}

	public void addFloor(Floor floor) {
		if (!this.floors.contains(floor))
			this.floors.add(floor);
	}

	public void addSpecialBuilding(Mesh specialBuilding) {
		if (!this.specialBuildings.contains(specialBuilding))
			this.specialBuildings.add(specialBuilding);
	}

	public void writeCityGML(String fileName) {
		WriterCityGML writer = new WriterCityGML(fileName);

		writer.addBuildings(this.buildings);
		writer.addFloors(this.floors);
		writer.addSpecialBuildings(this.specialBuildings);

		writer.write();
	}

	public void writeSTLFloors() {
		for (Floor f : this.floors)
			f.writeSTL("Files/floors.stl");
	}

	public void writeSTLBuildings() {
		int buildingCounter = 1;

		for (Edifice m : this.buildings) {
			m.writeSTL("Files/building - " + buildingCounter + ".stl");
			buildingCounter++;
		}
	}

	public void writeSTLSpecialBuildings() {

	}

	private Mesh parseFile(String directoryName) {

		log.info("Parsing...");

		try {
			Mesh mesh = new Mesh(ParserSTL.readSTL(directoryName + ""));

			log.info("Parsing ended !");
			return mesh;

		} catch (BadFormedFileException e) {
			log.severe("Error : the file is badly formed !");
			System.exit(1);
			return null;

		} catch (IOException e) {
			log.severe("Error : file not found or unreadable !");
			System.exit(1);
			return null;
		}
	}

	private Vector3d extractFloorNormal(String fileName) {

		try {
			Mesh floorBrut = new Mesh(ParserSTL.readSTL(fileName));

			// Extract of the normal of the floor
			return floorBrut.averageNormal();

		} catch (BadFormedFileException e) {
			log.severe("Error : the file is badly formed !");
			System.exit(1);
			return null;

		} catch (IOException e) {
			log.severe("Error : file not found or unreadable !");
			System.exit(1);
			return null;
		}
	}

	private void createChangeBaseMatrix(Vector3d normalFloor) {

		try {
			log.info("Base change...");

			// Base change
			this.matrix = MatrixMethod.createOrthoBase(normalFloor);
			MatrixMethod.changeBase(normalFloor, matrix);

			log.info("Base changed finished !");

		} catch (SingularMatrixException e) {
			log.severe("Error : the matrix is badly formed !");
			System.exit(1);
		}
	}

	// FIXME : should return a arraylist of floors
	private Mesh floorExtraction(Mesh mesh, Vector3d normalFloor) {

		log.info("Floor extraction...");

		// Searching for floor-oriented triangles with an error :
		// angleNormalErrorFactor
		Mesh meshOriented = mesh.orientedAs(normalFloor,
				SeparationFloorBuilding.angleNormalErrorFactor);

		// Floor-search algorithm
		// Select the lowest Triangle : it belongs to the floor
		// Take all of its neighbours
		// Select another Triangle which is not too high and repeat the above
		// step
		Mesh floors = Algos.floorExtract(meshOriented,
				SeparationFloorBuilding.altitudeErrorFactor);

		mesh.remove(floors);

		log.info("Floor extracted !");

		return floors;
	}

	private ArrayList<Mesh> buildingsExtraction(Mesh mesh, Mesh noise) {

		ArrayList<Mesh> buildingList = new ArrayList<Mesh>();

		// Extraction of the buildings
		log.info("Extracting building ...");

		ArrayList<Mesh> formsList = Algos.blockExtract(mesh);
		ArrayList<Mesh> thingsList = new ArrayList<Mesh>();

		// Separation of the little noises
		for (Mesh m : formsList) {
			if (m.size() > 1)
				thingsList.add(m);
			else
				noise.addAll(m);
		}

		// Algorithm : detection of buildings considering their size
		int number = 0;
		for (Mesh m : thingsList) {
			number += m.size();
		}

		for (Mesh m : thingsList) {
			if (m.size() >= SeparationFloorBuilding.blockSizeBuildingError
					* (double) number / (double) thingsList.size()) {

				buildingList.add(m);

			} else {

				noise.addAll(m);

			}
		}

		if (buildingList.size() == 0)
			log.severe("Error !");

		log.info("Buildings extracted !");

		return buildingList;
	}

	private void noiseTreatment(Mesh floors, Mesh noise) {

		Mesh floorsAndNoise = new Mesh(floors);
		floorsAndNoise.addAll(noise);
		ArrayList<Mesh> floorsList = Algos.blockExtract(floorsAndNoise);

		floors.clear();
		for (Mesh e : floorsList) {
			floors.addAll(e);
			noise.remove(e);
		}
	}
}
