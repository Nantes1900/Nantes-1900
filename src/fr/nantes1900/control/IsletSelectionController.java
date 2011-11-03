/**
 * 
 */
package fr.nantes1900.control;

import fr.nantes1900.view.IsletSelectionView;

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
    
    /**
     * View allowing to select an islet and launch a treatment.
     */
    private IsletSelectionView isView;

    /**
     * Creates a new controller to handle the islet selection window.
     */
    public IsletSelectionController()
    {
        gtController = new GlobalTreeController();
        aController = new ActionsController();
        
        isView = new IsletSelectionView(aController.getActionsView());
        isView.setVisible(true);
    }
}
