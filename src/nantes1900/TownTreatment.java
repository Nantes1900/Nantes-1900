package nantes1900;

import nantes1900.models.extended.Town;

public class TownTreatment {

	public static void main(String[] args) {

		Town town = new Town();
		town.buildFromMesh("Tests/test - towntreatment"); // The location of the 5 directories
		town.writeCityGML("test.gml"); // Write the CityGML file

	}

}
