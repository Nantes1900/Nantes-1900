package fr.nantes1900.control.isletselection;

import java.awt.Cursor;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.control.GlobalController;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.exceptions.WeirdResultException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.utils.STLWriter;
import fr.nantes1900.view.isletselection.GlobalTreeView.FileNode;
import fr.nantes1900.view.isletselection.IsletSelectionView;

/**
 * Controller for the islet selection window. Manages child components such as
 * the action panel, the tree and the 3D view.
 * @author Camille Bouquet
 * @author Luc Jallerat
 */
public class IsletSelectionController {

    /**
     * The controller of the panel containing buttons to perform the different
     * actions.
     */
    private ActionsController aController;

    /**
     * The controller of the tree used to select an islet.
     */
    private GlobalTreeController gtController;

    /**
     * The controller of the 3D view which shows a selected islet.
     */
    private Universe3DController u3DController;

    /**
     * The controller of the selected islet.
     */
    private BuildingsIsletController biController;

    /**
     * View allowing to select an islet and LAUNCH a process.
     */
    private IsletSelectionView isView;

    /**
     * The opened directory corresponding.
     */
    private File openedDirectory;

    /**
     * The selected file in the tree.
     */
    private File selectedFile;

    /**
     * The parent controller which handles this one.
     */
    private GlobalController parentController;

    /**
     * Creates a new controller to handle the islet selection window.
     * @param parentControllerIn
     *            Controller which handles this one.
     */
    public IsletSelectionController(final GlobalController parentControllerIn) {
        this.parentController = parentControllerIn;
        this.gtController = new GlobalTreeController(this);
        this.aController = new ActionsController(this);
        this.u3DController = new Universe3DController();
        this.biController = new BuildingsIsletController(this.u3DController);

        this.isView = new IsletSelectionView(this.aController.getActionsView(),
                this.gtController.getGlobalTreeView(),
                this.u3DController.getUniverse3DView());
        this.isView.setVisible(true);
    }

    /**
     * Computes the gravity normal and stores it in a gravity_normal.stl file in
     * the opened directory.
     * @return true - the normal has been correctly saved\n false - the normal
     *         couldn't been stored because no file or triangles are selected.
     */
    public final boolean computeGravityNormal() {
        boolean normalSaved = false;
        if (this.selectedFile != null
                && !this.u3DController.getTrianglesSelected().isEmpty()) {
            STLWriter writer = new STLWriter(this.openedDirectory.getPath()
                    + "/gravity_normal.stl");
            writer.setMesh(new Mesh(this.u3DController.getTrianglesSelected()));
            writer.write();
            this.biController.getIslet().setGravityNormal(
                    new Mesh(this.u3DController.getTrianglesSelected())
                            .averageNormal());
            normalSaved = true;
        } else {
            JOptionPane.showMessageDialog(this.isView, FileTools
                    .readHelpMessage(TextsKeys.KEY_COMPUTEGRAVITY,
                            TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                    .readHelpMessage(TextsKeys.KEY_COMPUTEGRAVITY,
                            TextsKeys.MESSAGETYPE_TITLE),
                    JOptionPane.ERROR_MESSAGE);
        }

        return normalSaved;
    }

    /**
     * Computes the ground normal.
     */
    public final void computeGroundNormal() {
        this.biController.setGroundNormal(new Mesh(this.u3DController
                .getTrianglesSelected()).averageNormal());
    }

    /**
     * Displays a file in the 3d universe selected in the tree.
     * @param node
     *            The node of the tree corresponding to the file to display.
     * @throws WeirdResultException
     *             if something weird behaves
     * @throws IOException
     *             if the file cannot be read
     */
    public final void displayFile(final DefaultMutableTreeNode node)
            throws WeirdResultException, IOException {
        this.isView.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        // Reads the file object of the Tree
        FileNode fileNode = (FileNode) node.getUserObject();

        if (fileNode.isFile()) {
            this.biController.readFile(fileNode.getEntireName());
            this.selectedFile = fileNode;
            this.biController.display();
        }
        this.isView.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Returns the building islet controller.
     * @return The building islet controller.
     */
    public final BuildingsIsletController getBiController() {
        return this.biController;
    }

    /**
     * Gets the islet selection view.
     * @return the islet selection view
     */
    public final JFrame getWindow() {
        return this.isView;
    }

    /**
     * Launches the process of the selected file which is an islet file. The
     * verification that the selected file is an islet file is made at the
     * selection in the tree.
     * @return true - the process of the selected islet has been correctly
     *         launched\n false - the process wasn't LAUNCH, because no islet or
     *         ground normal are selected.
     */
    public final boolean launchIsletProcess() {
        boolean processLaunched = false;

        if ((!this.u3DController.getTrianglesSelected().isEmpty())
                && this.selectedFile != null) {
            this.isView.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            this.computeGroundNormal();
            this.parentController.launchIsletProcess(this.selectedFile,
                    this.biController);
            processLaunched = true;
        } else {
            JOptionPane.showMessageDialog(this.isView, FileTools
                    .readHelpMessage(TextsKeys.KEY_LAUNCHISLET,
                            TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                    .readHelpMessage(TextsKeys.KEY_LAUNCHISLET,
                            TextsKeys.MESSAGETYPE_TITLE),
                    JOptionPane.ERROR_MESSAGE);
        }

        return processLaunched;
    }

    /**
     * Updates the directory containing the files of islets.
     * @param newDirectory
     *            The new directory.
     */
    public final void updateMockupDirectory(final File newDirectory) {
        this.openedDirectory = newDirectory;
        this.gtController.updateDirectory(this.openedDirectory);

        // checks if the gravity normal already exists
        File gravityNormal = new File(this.openedDirectory.getPath()
                + "/gravity_normal.stl");
        if (!gravityNormal.exists()) {
            JOptionPane.showMessageDialog(this.isView, FileTools
                    .readHelpMessage(TextsKeys.KEY_UPDATEMOCKUP,
                            TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                    .readHelpMessage(TextsKeys.KEY_UPDATEMOCKUP,
                            TextsKeys.MESSAGETYPE_TITLE),
                    JOptionPane.INFORMATION_MESSAGE);
            this.aController.setComputeNormalMode();
            this.isView.setStatusBarText(FileTools.readHelpMessage(
                    TextsKeys.KEY_IS_GRAVITYNORMAL,
                    TextsKeys.MESSAGETYPE_STATUSBAR));
        } else {
            try {
                // Reads the gravity normal in the file, and keeps it in memory.
                this.biController.readGravityNormal(gravityNormal.getPath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this.isView, FileTools
                        .readHelpMessage(TextsKeys.KEY_ERROR_IOEXCEPTION,
                                TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                        .readHelpMessage(TextsKeys.KEY_ERROR_IOEXCEPTION,
                                TextsKeys.MESSAGETYPE_TITLE),
                        JOptionPane.ERROR_MESSAGE);
            }
            this.isView.setStatusBarText(FileTools.readHelpMessage(
                    TextsKeys.KEY_IS_LAUNCHPROCESS,
                    TextsKeys.MESSAGETYPE_STATUSBAR));
            this.aController.setLaunchMode();
        }
    }
}
