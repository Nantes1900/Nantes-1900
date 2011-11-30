/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.IsletTreeView;

/**
 * @author Luc Jallerat, Camille Bouquet
 */
public class IsletTreeController
{
    /**
     * The view displaying the JTree.
     */
    private IsletTreeView          itView;

    /**
     * The parent controller.
     */
    private IsletProcessController parentController;

    public IsletTreeController(IsletProcessController parentControllerIn)
    {
        this.parentController = parentControllerIn;
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
                        IsletTreeController.this.getParentController()
                                .getU3DController().deselectEverySurfaces();

                        TreePath[] pathsSelected = e.getPaths();
                        List<DefaultMutableTreeNode> nodesSelected = new ArrayList<>();

                        for (TreePath path : pathsSelected)
                        {
                            nodesSelected.add((DefaultMutableTreeNode) path
                                    .getLastPathComponent());
                        }

                        for (DefaultMutableTreeNode node : nodesSelected)
                        {
                            if (node.getUserObject() instanceof Surface)
                            {
                                IsletTreeController.this
                                        .getParentController()
                                        .getU3DController()
                                        .selectSurfaceFromTree(
                                                (Surface) node.getUserObject());
                            } else
                            {
                                // TODO : throw exception if it is not a mesh.
                            }
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
        this.addTreeController();
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
