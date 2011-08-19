package fr.nantes1900;

import fr.nantes1900.models.extended.Town;

/**
 * Implements a main class. Here it is the treatment of the first part of St
 * Similien mock-up.
 * 
 * @author Daniel Lefevre
 */
public final class StSimilienM01 {

    /**
     * Private constructor.
     */
    private StSimilienM01() {
    }

    /**
     * Main function.
     * 
     * @param args
     *            arguments (the program does not take them into account)
     */
    public static void main(final String[] args) {

        // Create new town
        final Town town = new Town();
        // Apply the algorithm to it
        town.buildFromMesh("files/St-Similien/m01");
        // Write the results
        town.writeCityGML("files/St-Similien/m01/results/StSimilienM01.gml");
    }
}
