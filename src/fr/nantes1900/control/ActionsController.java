/**
 * 
 */
package fr.nantes1900.control;

import fr.nantes1900.view.ActionsView;

/**
 * @author Camille
 */
public class ActionsController
{
    /**
     * The panel containing buttons to launch the different actions.
     */
    private ActionsView aView;

    /**
     * Creates a new controller to handle the panel containing buttons to launch
     * the different actions.
     */
    public ActionsController()
    {
        aView = new ActionsView();
    }
    
    /**
     * Returns the actions view associated with this controller.
     * @return
     *      The actions view.
     */
    public ActionsView getActionsView()
    {
        return this.aView;
    }
}
