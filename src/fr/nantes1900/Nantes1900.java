/**
 * 
 */
package fr.nantes1900;

import fr.nantes1900.control.GlobalController;

/**
 * Implements a main class. Launch the UI.
 * @author Camille Bouquet
 */
public class Nantes1900
{

    /**
     * Private constructor.
     */
    private Nantes1900()
    {
    }

    /**
     * Main function.
     * @param args
     *            arguments (the program does not take them into account)
     */
    public static void main(final String[] args)
    {
        final GlobalController globalController = new GlobalController();
    }

}
