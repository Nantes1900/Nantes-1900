package fr.nantes1900;

import fr.nantes1900.models.extended.Town;

/**
 * Implements a main class. Here it is the treatment of the first part of St
 * Similien mock-up.
 * @author Daniel Lefevre
 */
public final class StSimilienM01 {

    /**
     * Main function.
     * @param args
     *            arguments (the program does not take them into account
     */
    public static void main(final String[] args) {
        final Town town = new Town();
        town.buildFromMesh("Tests/St-Similien/m01");
        // town.writeCityGML("Tests/St-Similien/m01/results/StSimilienM01.gml");
        town.writeSTL("Tests/St-Similien/m01/results/");
    }

    /**
     * Private constructor.
     */
    private StSimilienM01() {
    }
}
