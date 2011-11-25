/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.utils.FileTools;
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
        this.buildTreeView();
    }

    public IsletTreeView getView()
    {
        return this.itView;
    }
    public void refreshView(){
        this.buildTreeView();
        this.itView.repaint();
    }
    private void buildTreeView(){
        try
        {
            this.itView.buildTree(this.parentController.getBiController().returnNode());
        } catch (InvalidCaseException e)
        {
            // TODO Auto-generated catch block
            System.out.println(FileTools.readErrorMessage(TextsKeys.KEY_RETURNNODE, TextsKeys.MESSAGETYPE_MESSAGE));
        }
    }
}
