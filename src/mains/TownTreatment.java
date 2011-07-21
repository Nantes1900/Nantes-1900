package mains;

import modeles.Town;

public class TownTreatment {

	public static void main(String[] args) {

		Town town = new Town();
		town.buildFromMesh("m01"); // The location of the 5 directories
		town.writeCityGML("m01.xml"); // Write the CityGML file

	}

}
