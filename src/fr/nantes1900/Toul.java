package fr.nantes1900;

import fr.nantes1900.models.extended.Town;

/**
 * Main for Toul mock-up.
 * 
 * @author Daniel Lefevre
 */
public final class Toul {

    /**
     * Private constructor.
     */
    private Toul() {
    }

    /**
     * Main method. Not documentation necessary.
     * 
     * @param args
     *            nothing to care about
     */
    public static void main(final String[] args) {

        final Town town = new Town();
        town.buildFromMesh("Tests/test - maquette toul");
        town.writeCityGML("test.xml");
    }
}
