/**
 * 
 */
package fr.nantes1900.control.isletselection;

import java.io.File;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.view.isletselection.GlobalTreeView;

/**
 * @author Camille
 */
public class GlobalTreeController
{
    /**
     * View of the tree.
     */
    private GlobalTreeView           gtView;

    /**
     * The parent controller to give feedback to.
     */
    private IsletSelectionController parentController;

    /**
     * Creates a new controller to handle the tree used to select and view an
     * islet.
     * @param isletSelectionController
     */
    public GlobalTreeController(
            IsletSelectionController isletSelectionController)
    {
        this.parentController = isletSelectionController;
        this.gtView = new GlobalTreeView();

        this.gtView.getTree().addTreeSelectionListener(
                new TreeSelectionListener() {
                    @Override
                    public void valueChanged(TreeSelectionEvent e)
                    {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
                                .getPath().getLastPathComponent();
                        GlobalTreeController.this.parentController
                                .displayFile(node);
                    }
                });
    }

    /**
     * Returns the view of the tree associated with this controller.
     * @return The view of the tree.
     */
    public GlobalTreeView getGlobalTreeView()
    {
        return this.gtView;
    }

    /**
     * Updates the root directory.
     * @param mockupDirectory
     *            The new root directory.
     */
    public void updateDirectory(File newDirectory)
    {
        this.gtView.displayDirectory(newDirectory);
        // TODO Auto-generated method stub
        // pour vérifier que tout se passe bien. A enlever quand le visuel sera
        // mis en place.
        System.out.println("Dossier mis à jour");
    }
}
