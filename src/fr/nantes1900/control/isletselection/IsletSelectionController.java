/**
 * 
 */
package fr.nantes1900.control.isletselection;

import java.io.File;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.control.GlobalController;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.islets.buildings.ResidentialIslet;
import fr.nantes1900.models.middle.Mesh;
import fr.nantes1900.utils.ParserSTL;
import fr.nantes1900.utils.WriterSTL;
import fr.nantes1900.view.isletselection.IsletSelectionView;

/**
 * @author Camille
 */
public class IsletSelectionController
{

    /**
     * The controller of the panel containing buttons to perform the different
     * actions.
     */
    private ActionsController        aController;

    /**
     * The controller of the tree used to select an islet.
     */
    private GlobalTreeController     gtController;

    /**
     * The controller of the 3D view which shows a selected islet.
     */
    private Universe3DController     u3DController;

    /**
     * The controller of the selected islet.
     */
    private BuildingsIsletController biController;

    /**
     * View allowing to select an islet and launch a treatment.
     */
    private IsletSelectionView       isView;

    /**
     * The opened directory corresponding.
     */
    private File                     openedDirectory;

    /**
     * The selected file in the tree.
     */
    private File                     selectedFile;

    /**
     * The parent controller which handles this one.
     */
    private GlobalController         parentController;

    /**
     * Creates a new controller to handle the islet selection window.
     */
    public IsletSelectionController()
    {
        this.gtController = new GlobalTreeController(this);
        this.aController = new ActionsController(this);
        this.u3DController = new Universe3DController(this);

        this.isView = new IsletSelectionView(this.aController.getActionsView(),
                this.gtController.getGlobalTreeView(),
                this.u3DController.getUniverse3DView());
        this.isView.setVisible(true);
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
            this.gtController.updateDirectory(this.openedDirectory);
            this.isView.setStatusBarText("Sélectionnez un îlot à traiter");
        }
        return true;
    }

    /**
     * Launches the treatment of the selected file which is an islet file. The
     * verification that the selected file is an islet file is made at the
     * selection in the tree.
     */
    public void launchIsletTreatment()
    {
        this.parentController.launchIsletTreatment(this.selectedFile);
    }

    public void displayFile(DefaultMutableTreeNode node)
    {
        this.biController = new BuildingsIsletController(this,
                this.u3DController);
        // Reads the file object of the Tree
        ParserSTL parser = new ParserSTL(node.getUserObject().toString());
        AbstractBuildingsIslet resIslet;
        try
        {
            resIslet = new ResidentialIslet(parser.read());
            this.getBiController().setIslet(resIslet);
            this.getBiController().display();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void computeGravityNormal()
    {
        WriterSTL writer = new WriterSTL(this.openedDirectory
                + "gravity_normal.stl");
        Point point = new Point(1, 1, 1);
        Edge edge = new Edge(point, point);
        Triangle triangle = new Triangle(point, point, point, edge, edge, edge,
                this.biController.computeNormalWithTrianglesSelected());
        Mesh mesh = new Mesh();
        mesh.add(triangle);
        writer.setMesh(mesh);
        writer.write();
    }

    public BuildingsIsletController getBiController()
    {
        return this.biController;
    }

    public void computeGroundNormal()
    {
        this.biController.setGroundNormal(this.biController
                .computeNormalWithTrianglesSelected());
    }
}
