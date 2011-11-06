/**
 * 
 */
package fr.nantes1900.control;

import java.io.File;

import fr.nantes1900.view.IsletSelectionView;

/**
 * @author Camille
 */
public class IsletSelectionController
{

    /**
     * The controller of the panel containing buttons to perform the different
     * actions.
     */
    private ActionsController    aController;

    /**
     * The controller of the tree used to select an islet.
     */
    private GlobalTreeController gtController;

    /**
     * The controller of the 3D view which shows a selected islet.
     */
    private Universe3DController u3DController;

    /**
     * View allowing to select an islet and launch a treatment.
     */
    private IsletSelectionView   isView;

    /**
     * The opened directory corresponding.
     */
    private File                 openedDirectory;

    /**
     * The selected file in the tree.
     */
    private File                 selectedFile;

    /**
     * The parent controller which handles this one.
     */
    private GlobalController     parentController;

    /**
     * Creates a new controller to handle the islet selection window.
     */
    public IsletSelectionController()
    {
        gtController = new GlobalTreeController(this);
        aController = new ActionsController(this);
        u3DController = new Universe3DController(this);

        isView = new IsletSelectionView(aController.getActionsView(),
                gtController.getGlobalTreeView(),
                u3DController.getUniverse3DView());
        isView.setVisible(true);
    }

    /**
     * Updates the directory containing the files of islets.
     * @param newDirectory
     *            The new directory.
     * @return true - the directory match a mockup part directory format.\n
     *         false - the directory doesn't match a mockup part directory
     *         format.
     * @todo Check the matching.
     */
    public boolean updateMockupDirectory(File newDirectory)
    {
        boolean isMockupDirectory = true;
        if (isMockupDirectory)
        {
            this.openedDirectory = newDirectory;
            gtController.updateDirectory(openedDirectory);
            isView.setStatusBarText("Sélectionnez un îlot à traiter");
        }

        return true;
    }

    /**
     * Launches the treatment of the selected file which is an islet file.
     * 
     * The verification that the selected file is an islet file is made at the selection in the tree.
     */
    public void launchIsletTreatment()
    {
        this.parentController.launchIsletTreatment(selectedFile);
    }
}
