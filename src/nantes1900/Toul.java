package nantes1900;

import nantes1900.models.extended.Town;

public final class Toul {

	private Toul() {
	}

	public static void main(final String[] args) {

		final Town town = new Town();
		town.buildFromMesh("Tests/test - maquette toul");
		town.writeCityGML("test.xml");
	}
}