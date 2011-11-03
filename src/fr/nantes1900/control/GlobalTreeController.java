/**
 * 
 */
package fr.nantes1900.control;

import fr.nantes1900.view.GlobalTreeView;

/**
 * @author Camille
 */
public class GlobalTreeController
{
    /**
     * View of the tree.
     */
    private GlobalTreeView gtView;
    
    /**
     * Creates a new controller to handle the tree used to select and view an islet.
     */
    public GlobalTreeController()
    {
        gtView = new GlobalTreeView();
    }
    
    /**
     * Returns the view of the tree associated with this controller.
     * 
     * @return
     *      The view of the tree.
     */
    public GlobalTreeView getGlobalTreeView()
    {
        return this.gtView;
    }
}
