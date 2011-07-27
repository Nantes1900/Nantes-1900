package nantes1900;

import nantes1900.models.extended.Town;

public class Main1 {

	public static void main(String[] args) {

		Town town = new Town();
		town.buildFromMesh("Tests/test - st-similien");
		town.writeCityGML("Files/test.gml");
		town.writeSTL("Files");
	}
}