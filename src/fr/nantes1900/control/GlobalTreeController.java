/**
 * 
 */
package fr.nantes1900.control;

import java.io.File;

import fr.nantes1900.view.GlobalTreeView;

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
        // TODO Auto-generated method stub
        // pour vérifier que tout se passe bien. A enlever quand le visuel sera
        // mis en place.
        System.out.println("Dossier mis à jour");
    }
}
