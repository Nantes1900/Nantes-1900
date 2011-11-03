/**
 * 
 */
package fr.nantes1900.control;

/**
 * @author Camille
 */
public class IsletSelectionController
{
    /**
     * The controller of the tree used to select an islet.
     */
    private GlobalTreeController gtController;

    /**
     * The controller of the panel containing buttons to perform the different
     * actions.
     */
    private ActionsController    aController;

    public IsletSelectionController()
    {
        gtController = new GlobalTreeController();
        aController = new ActionsController();
    }
}
