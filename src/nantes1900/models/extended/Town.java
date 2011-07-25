package nantes1900.models.extended;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.vecmath.Vector3d;

import nantes1900.coefficients.SeparationFloorBuilding;
import nantes1900.models.Mesh;
import nantes1900.utils.Algos;
import nantes1900.utils.MatrixMethod;
import nantes1900.utils.ParserSTL;
import nantes1900.utils.WriterCityGML;
import nantes1900.utils.Algos.NoFloorException;
import nantes1900.utils.MatrixMethod.SingularMatrixException;
import nantes1900.utils.ParserSTL.BadFormedFileException;



@SuppressWarnings("unused")
public class Town {

	private ArrayList<Building> residentials = new ArrayList<Building>();
	private ArrayList<Building> industrials = new ArrayList<Building>();
	private ArrayList<Floor> floors = new ArrayList<Floor>();
	private ArrayList<Floor> wateries = new ArrayList<Floor>();
	private ArrayList<Mesh> specialBuildings = new ArrayList<Mesh>();

	private double[][] matrix = new double[3][3];

	private Logger log = Logger.getLogger("logger");

	public Town() {
		this.log.setLevel(Level.FINE);
		this.log.setUseParentHandlers(false);
		this.log.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
	}

	public void addFloor(Floor floor) {
		if (!this.floors.contains(floor))
			this.floors.add(floor);
	}

	public void addIndustrial(Building building) {
		if (!this.industrials.contains(building))
			this.industrials.add(building);
	}

	public void addResidential(Building building) {
		if (!this.residentials.contains(building))
			this.residentials.add(building);
	}

	public void addSpecialBuilding(Mesh specialBuilding) {
		if (!this.specialBuildings.contains(specialBuilding))
			this.specialBuildings.add(specialBuilding);
	}

	public void addWatery(Floor watery) {
		if (!this.floors.contains(watery))
			this.floors.add(watery);
	}

	public void buildFromMesh(String directoryName) {

		Vector3d normalFloor = this.extractFloorNormal(directoryName
				+ "/floor.stl");
		this.createChangeBaseMatrix(normalFloor);

		this.treatFloors(directoryName + "/floors/");
		this.treatWateries(directoryName + "/wateries/");

		log.info("Numbers of floors : " + this.floors.size());
		log.info("Numbers of wateries : " + this.wateries.size());

		this.treatSpecialBuildings(directoryName + "/special_buildings/");

		log.info("Numbers of special buildings : "
				+ this.specialBuildings.size());

		this.treatIndustrials(directoryName + "/industrials/", normalFloor);
		this.treatResidentials(directoryName + "/residentials/", normalFloor);

		log.info("Numbers of industrials zones : " + this.industrials.size());
		log.info("Numbers of residentials zones : " + this.residentials.size());
	}

	private ArrayList<Mesh> buildingsExtraction(Mesh mesh, Mesh noise) {

		ArrayList<Mesh> buildingList = new ArrayList<Mesh>();

		// Extraction of the buildings
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

		return buildingList;
	}

	private ArrayList<Mesh> carveRealBuildings(ArrayList<Mesh> buildings) {

		// FIXME
		return null;
	}

	private void createChangeBaseMatrix(Vector3d normalFloor) {

		try {

			// Base change
			this.matrix = MatrixMethod.createOrthoBase(normalFloor);
			MatrixMethod.changeBase(normalFloor, matrix);

		} catch (SingularMatrixException e) {
			log.severe("Error : the matrix is badly formed !");
			System.exit(1);
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

	private Mesh floorExtraction(Mesh mesh, Vector3d normalFloor)
			throws NoFloorException {

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

		return floors;
	}

	private ArrayList<Mesh> noiseTreatment(Mesh floors, Mesh noise) {

		Mesh floorsAndNoise = new Mesh(floors);
		floorsAndNoise.addAll(noise);
		ArrayList<Mesh> floorsList = Algos.blockExtract(floorsAndNoise);

		floors.clear();
		for (Mesh e : floorsList) {
			floors.addAll(e);
			noise.remove(e);
		}

		return floorsList;
	}

	private Mesh parseFile(String fileName) {

		try {
			return new Mesh(ParserSTL.readSTL(fileName));

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

	private void recollerCorrectementLesSolsAuxMurs(Building e,
			ArrayList<Mesh> floors2) {
		// FIXME
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

	private void treatIndustrials(String directoryName, Vector3d normalFloor) {
		int counterIndustrial = 1;
		// FIXME
	}

	private void treatResidentials(String directoryName, Vector3d normalFloor) {
		int counterResidentials = 1;

		while (new File(directoryName + "residential - " + counterResidentials
				+ ".stl").exists()) {

			Mesh mesh = this.parseFile((directoryName + "residential - "
					+ counterResidentials + ".stl"));

			mesh.changeBase(this.matrix);

			Mesh noise = new Mesh();

			Mesh wholeFloor;
			ArrayList<Mesh> buildings;

			try {
				wholeFloor = this.floorExtraction(mesh, normalFloor);

				buildings = this.buildingsExtraction(mesh, noise);

				ArrayList<Mesh> floors = this.noiseTreatment(wholeFloor, noise);

				ArrayList<Mesh> formsList = this.carveRealBuildings(buildings);

				for (Mesh m : buildings) {
					Building e = new Building();
					e.buildFromMesh(m, normalFloor);
					this.recollerCorrectementLesSolsAuxMurs(e, floors);
					this.addResidential(e);
				}

				//FIXME
				System.exit(0);

				for (Mesh m : floors) {
					Floor floor = new Floor("street");
					floor.buildFromMesh(m);
					this.addFloor(floor);
				}

			} catch (NoFloorException e1) {
				// If there is no floor to extract

				buildings = this.buildingsExtraction(mesh, noise);

				ArrayList<Mesh> formsList = this.carveRealBuildings(buildings);

				for (Mesh m : buildings) {
					Building e = new Building();
					e.buildFromMesh(m, normalFloor);
					this.addResidential(e);
				}
			}

			// FIXME : I don't know what to do with the formsList : little
			// walls, forms on the ground...

			counterResidentials++;
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

	private void treatWateries(String directoryName) {
		int counterWateries = 1;

		while (new File(directoryName + "watery - " + counterWateries + ".stl")
				.exists()) {

			Floor watery = new Floor("watery");

			watery.buildFromMesh(this.parseFile(directoryName + "watery - "
					+ counterWateries + ".stl"));

			watery.changeBase(this.matrix);

			this.addWatery(watery);

			counterWateries++;
		}
	}

	public void writeCityGML(String fileName) {
		WriterCityGML writer = new WriterCityGML(fileName);

		writer.addBuildings(this.residentials);
		writer.addBuildings(this.industrials);
		writer.addFloors(this.floors);
		writer.addFloors(this.wateries);
		writer.addSpecialBuildings(this.specialBuildings);

		writer.write();
	}

	public void writeSTLFloors() {
		for (Floor f : this.floors)
			f.writeSTL("Files/floors.stl");
	}

	public void writeSTLIndustrials() {
		int buildingCounter = 1;

		for (Building m : this.industrials) {
			m.writeSTL("Files/building - " + buildingCounter + ".stl");
			buildingCounter++;
		}
	}

	public void writeSTLResidentials() {
		int buildingCounter = 1;

		for (Building m : this.residentials) {
			m.writeSTL("Files/building - " + buildingCounter + ".stl");
			buildingCounter++;
		}
	}

	public void writeSTLSpecialBuildings() {

	}

	public void writeSTLWateries() {
		for (Floor f : this.wateries)
			f.writeSTL("Files/floors.stl");
	}
}
