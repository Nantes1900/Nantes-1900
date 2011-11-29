/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.extended.Surface;
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
        this.addTreeController();
    }

    public IsletProcessController getParentController()
    {
        return this.parentController;
    }

    public void addTreeController()
    {
        this.itView.getTree().addTreeSelectionListener(
                new TreeSelectionListener() {

                    @Override
                    public void valueChanged(final TreeSelectionEvent e)
                    {

                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
                                .getPath().getLastPathComponent();

                        if (node.getUserObject() instanceof Surface)
                        {
                            IsletTreeController.this
                                    .getParentController()
                                    .getU3DController()
                                    .selectMeshFromTree(
                                            (Surface) node.getUserObject());
                        } else
                        {
                            // TODO : throw exception if it is not a mesh.
                        }

                    }
                });
        // FIXME : what if a object is deselected in the tree ? What if more
        // than ONE object is selected ?
    }

    public IsletTreeView getView()
    {
        return this.itView;
    }

    public void refreshView()
    {
        this.buildTreeView();
        this.itView.repaint();
    }

    private void buildTreeView()
    {
        try
        {
            this.itView.buildTree(this.parentController.getBiController()
                    .returnNode());
        } catch (InvalidCaseException e)
        {
            // TODO Auto-generated catch block
            System.out.println(FileTools.readErrorMessage(
                    TextsKeys.KEY_RETURNNODE, TextsKeys.MESSAGETYPE_MESSAGE));
        }
    }
}
