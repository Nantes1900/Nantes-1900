/**
 * 
 */
package fr.nantes1900.control;

import java.io.File;

/**
 * @author Camille
 */
public class GlobalController
{
    /**
     * The controller of the selection window.
     */
    private IsletSelectionController isletSelectionController;

    /**
     * Creates a new global controller which creates others global controllers.
     */
    public GlobalController()
    {
        this.isletSelectionController = new IsletSelectionController();
    }

    /**
     * Launches the treatment of an islet and opens the new window.
     * 
     * @param isletFile
     *              The file containing data of the islet to treat.
     */
    public void launchIsletTreatment(File isletFile)
    {
        // TODO
    }
}
