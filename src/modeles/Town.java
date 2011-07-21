package modeles;

import java.util.ArrayList;

import utils.WriterCityGML;

public class Town {
	private ArrayList<Edifice> buildings = new ArrayList<Edifice>();
	private ArrayList<Floor> floors = new ArrayList<Floor>();
	private ArrayList<Mesh> specialBuildings = new ArrayList<Mesh>();

	public Town() {
	}

	public void buildFromMesh() {

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
}
