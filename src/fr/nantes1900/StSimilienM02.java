package fr.nantes1900;

import fr.nantes1900.models.extended.Town;

/**
 * Implements a main class. Here it is the treatment of the first part of St
 * Similien mock-up.
 * 
 * @author Daniel Lefevre
 */
public final class StSimilienM02 {

    /**
     * Private constructor.
     */
    private StSimilienM02() {
    }

    /**
     * Main function.
     * 
     * @param args
     *            arguments (the program does not take them into account)
     */
    public static void main(final String[] args) {

        // Creates a new town.
        final Town town = new Town();
        // Applies the algorithm to it.
        town.buildFromMesh("files/St-Similien/m02");
        // Writes the results under STL
        town.writeSTL("files/St-Similien/m02/results/");
    }
}
