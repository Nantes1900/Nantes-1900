package fr.nantes1900;

import fr.nantes1900.models.extended.Town;

public final class StSimilienM01 {

	public static void main(final String[] args) {
		final Town town = new Town();
		town.buildFromMesh("Tests/St-Similien/m01");
		// town.writeCityGML("Tests/St-Similien/m01/results/StSimilienM01.gml");
		town.writeSTL("Tests/St-Similien/m01/results/");
	}

	private StSimilienM01() {
	}
}