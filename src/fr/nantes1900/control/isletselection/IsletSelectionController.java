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
import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.utils.WriterSTL;
import fr.nantes1900.view.isletselection.GlobalTreeView.FileNode;
import fr.nantes1900.view.isletselection.IsletSelectionView;

/**
 * TODO.
 * @author Camille Bouquet
 */
public class IsletSelectionController implements ElementsSelectedListener {
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
     * View allowing to select an islet and launch a process.
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
    public IsletSelectionController(final GlobalController parentControllerIn)
    {
        this.parentController = parentControllerIn;
        this.gtController = new GlobalTreeController(this);
        this.aController = new ActionsController(this);
        this.u3DController = new Universe3DController(this);
        this.biController = new BuildingsIsletController(this,
                this.u3DController);

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
    // TODO by Camille or Luc : correct this. This method should not return a
    // boolean, but nothing.
    // If an error happens (no file or triangles not selected, then throw an
    // exception : it has been created for that case !

    public final boolean computeGravityNormal() {
        boolean normalSaved = false;
        if (this.selectedFile != null
                && !this.u3DController.getTrianglesSelected().isEmpty())
        {
            // TODO by Daniel : Move this code
            WriterSTL writer = new WriterSTL(this.openedDirectory.getPath()
                    + "/gravity_normal.stl");
            Point point1 = new Point(1, 1, 1);
            Point point2 = new Point(2, 2, 2);
            Point point3 = new Point(0, 0, 0);
            Edge edge1 = new Edge(point1, point2);
            Edge edge2 = new Edge(point1, point3);
            Edge edge3 = new Edge(point3, point2);
            Triangle triangle = new Triangle(point1, point2, point3, edge1,
                    edge2, edge3,
                    this.biController.computeNormalWithTrianglesSelected());
            Mesh mesh = new Mesh();
            mesh.add(triangle);
            writer.setMesh(mesh);
            writer.write();
            System.out.println("Enregistre");
            normalSaved = true;
        } else
        {
            // TODO : put this text in the text file (or XML for Luc).
            JOptionPane
                    .showMessageDialog(
                            this.isView,
                            "Sélectionnez un îlot dans l'arbre\npuis "
                                    + "sélectionnez des triangles pour créer la normale.",
                            "Sauvegarde impossible", JOptionPane.ERROR_MESSAGE);
        }

        return normalSaved;
    }

    /**
     * Computes the ground normal.
     */
    public final void computeGroundNormal() {
        this.biController.setGroundNormal(this.biController
                .computeNormalWithTrianglesSelected());
    }

    /**
     * Displays a file in the 3d universe selected in the tree.
     * @param node
     *            The node of the tree corresponding to the file to display.
     */
    public final void displayFile(final DefaultMutableTreeNode node) {
        this.isView.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        // Reads the file object of the Tree
        FileNode fileNode = (FileNode) node.getUserObject();

        if (fileNode.isFile())
        {
            try
            {
                this.biController.readFile(fileNode.getEntireName());
            } catch (IOException e)
            {
                // FIXME : make a pop-up :)
                e.printStackTrace();
            }
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
     * TODO.
     * @return TODO
     */
    public final JFrame getWindow() {
        return this.isView;
    }

    /**
     * Launches the process of the selected file which is an islet file. The
     * verification that the selected file is an islet file is made at the
     * selection in the tree.
     * @return true - the process of the selected islet has been correctly
     *         launched\n false - the process wasn't launch, because no islet or
     *         ground normal are selected.
     */
    public final boolean launchIsletProcess() {
        boolean processLaunched = false;

        if ((!this.u3DController.getTrianglesSelected().isEmpty() || this.aController
                .getActionsView().isGravityGroundCheckBoxSelected())
                && this.selectedFile != null)
        {
            this.isView.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if (this.aController.getActionsView()
                    .isGravityGroundCheckBoxSelected())
            {
                this.biController.useGravityNormalAsGroundNormal();
            } else
            {
                this.computeGroundNormal();
            }
            this.parentController.launchIsletProcess(this.selectedFile,
                    this.biController);
            processLaunched = true;
        } else
        {
            // TODO by Luc : put this text in a XML file.
            JOptionPane
                    .showMessageDialog(
                            this.isView,
                            "Veuillez sélectionner un îlot et une normale pour "
                                    + "lancer le traitement\nou sélectionnez \"Utiliser la normale "
                                    + "orientée selon la gravité\n",
                            "Traitement impossible", JOptionPane.ERROR_MESSAGE);
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
        if (!gravityNormal.exists())
        {
            JOptionPane.showMessageDialog(this.isView,
                    "La normale orientée selon la gravité n'a pas été trouvée "
                            + "dans le dossier ouvert.\nVeuillez en créer "
                            + "une nouvelle.",
                    "Normale orientée selon la gravité inexistante",
                    JOptionPane.INFORMATION_MESSAGE);
            this.aController.setComputeNormalMode();
            this.isView.setStatusBarText(FileTools.readHelpMessage(
                    "ISGravityNormal", TextsKeys.MESSAGETYPE_STATUSBAR));
        } else
        {
            try
            {
                // Reads the gravity normal in the file, and keeps it in memory.
                this.biController.readGravityNormal(gravityNormal.getPath());
            } catch (IOException e)
            {
                // If the file can not be read or is not well built.
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.isView.setStatusBarText(FileTools.readHelpMessage(
                    "ISLaunchProcess", TextsKeys.MESSAGETYPE_STATUSBAR));
            this.aController.setLaunchMode();
        }
    }

    // FIXME : is that useful ? Is it useful to implement
    // ElementsSelectedListener ?
    @Override
    public void triangleSelected(Triangle triangleSelected) {
        // TODO Auto-generated method stub

    }

    @Override
    public void triangleDeselected(Triangle triangleSelected) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceSelected(Surface surfaceSelected) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDeselected(Surface surfaceSelected) {
        // TODO Auto-generated method stub

    }
}
