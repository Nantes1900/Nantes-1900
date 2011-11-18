/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import fr.nantes1900.view.isletprocess.IsletTreeView;

/**
 * @author Camille
 */
// FIXME : Javadoc
public class IsletTreeController
{
    private IsletTreeView          itView;
    private IsletProcessController parentController;

    public IsletTreeController(IsletProcessController parentController)
    {
        this.parentController = parentController;
        this.itView = new IsletTreeView();
    }

    public IsletTreeView getView()
    {
        return itView;
    }

}
