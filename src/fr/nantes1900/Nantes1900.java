/**
 * 
 */
package fr.nantes1900;

import fr.nantes1900.control.GlobalController;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.view.display3d.Universe3DView;
import fr.nantes1900.view.isletprocess.CaracteristicsView;
import fr.nantes1900.view.isletprocess.IsletProcessView;
import fr.nantes1900.view.isletprocess.IsletTreeView;
import fr.nantes1900.view.isletprocess.NavigationBarView;
import fr.nantes1900.view.isletprocess.ParametersView;

/**
 * Implements a main class. Launch the UI.
 * @author Nicolas Bouillon, Camille Bouquet, Luc Jallerat, Daniel Lefevre, Siju
 *         Wu
 */
public final class Nantes1900
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
    @SuppressWarnings("unused")
    public static void main(final String[] args)
    {
        new GlobalController();
    }
}
