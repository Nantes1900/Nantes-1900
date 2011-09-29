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

        // Begins a step by step execution, waiting at each step that the user
        // has finished to treat.
        Town.setStepByStepMode();

        // Applies the algorithms to the directory.
        town.buildFromMesh("files/St-Similien/m02/");

        // Writes the results in STL files.
        town.writeSTL("files/St-Similien/m02/results/");

        // Writes the results in cityGML files.
        town.writeCityGML("files/St-Similien/m02/results/St-Similien - m02.gml");
    }
}
