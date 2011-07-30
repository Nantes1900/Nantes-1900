package nantes1900;

import nantes1900.models.extended.Town;

public abstract class StSimilienM01 {

	private StSimilienM01() {

	}

	public static void main(final String[] args) {
		final Town town = new Town();
		town.buildFromMesh("Tests/St-Similien/m01");
		// town.writeCityGML("Tests/St-Similien/m01/results/StSimilienM01.gml");
		town.writeSTL("Tests/St-Similien/m01/results/");
	}
}