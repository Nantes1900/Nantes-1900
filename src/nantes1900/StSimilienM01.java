package nantes1900;

import nantes1900.models.extended.Town;

public class StSimilienM01 {

	public static void main(String[] args) {

		Town town = new Town();
		town.buildFromMesh("Tests/St-Similien/m01");
		town.writeCityGML("Files/StSimilienM01.gml");
	}
}