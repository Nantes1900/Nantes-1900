/**
 * 
 */
package fr.nantes1900.control;

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
        isletSelectionController = new IsletSelectionController();
    }
}
