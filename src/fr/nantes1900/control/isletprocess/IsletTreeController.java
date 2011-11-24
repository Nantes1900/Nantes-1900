/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
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
        try
        {
            this.itView.buildTree(this.parentController.getBiController().returnNode());
        } catch (InvalidCaseException e)
        {
            // TODO Auto-generated catch block
            System.out.println("Probleme dans le returnNode de BuildingsIsletController");
        }
    }

    public IsletTreeView getView()
    {
        return itView;
    }

}
