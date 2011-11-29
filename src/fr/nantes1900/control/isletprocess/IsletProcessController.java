/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.Cursor;
import java.io.File;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.control.GlobalController;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.view.isletprocess.IsletProcessView;

/**
 * Controller of the process of an islet. This controller makes the link between
 * each elements in the window and also with the model which contains mesh
 * datas.
 * @author Camille Bouquet
 * @author Luc Jallerat
 */
public class IsletProcessController implements ElementsSelectedListener
{
    /**
     * Parent controller of this one.
     */
    private GlobalController          parentController;

    /**
     * The window containing all the needed panels to process an islet.
     */
    private IsletProcessView          ipView;
    
    private Functions3DToolbarController f3DController;

    /**
     * Controller of the current characteristic panel.
     */
    private CharacteristicsController cController;

    /**
     * Controller of the tree showing the architecture of data in the current
     * step.
     */
    private IsletTreeController       itController;

    /**
     * Controller of the navigation bar which allows to abort the treatment of
     * an islet and select a new one, launch a new process to go on a further
     * step and so on.
     */
    private NavigationBarController   nbController;

    /**
     * Controller of the parameter panel which allows to modify parameters for
     * the next process.
     */
    private ParametersController      pController;

    /**
     * Controller of the 3d View which displays meshes.
     */
    private Universe3DController      u3DController;

    /**
     * Controller of the building islet currently under process. This one makes
     * the link with meshes data.
     */
    private BuildingsIsletController  biController;

    /**
     * Indicates the current step.
     */
    private int                       progression;

    /**
     * Creates a new islet process controller to launch different processes on
     * an islet.
     * @param parentController
     *            The parent controller which makes the link with other windows.
     * @param isletFile
     *            File containing data of the selectedd islet.
     * @param biController
     *            Controller to handle the islet data.
     */
    public IsletProcessController(GlobalController parentController,
            File isletFile, BuildingsIsletController biController)
    {
        this.parentController = parentController;
        this.progression = 1;
        this.biController = biController;
        this.cController = new CharacteristicsController(this);
        this.itController = new IsletTreeController(this);
        this.nbController = new NavigationBarController(this);
        this.pController = new ParametersController(this);
        this.f3DController = new Functions3DToolbarController(this);
        this.u3DController = new Universe3DController();
        this.u3DController.getUniverse3DView().setToolbar(f3DController.getToolbar());
        this.biController.setUniverse3DController(u3DController);
        this.biController.display();

        // creates the windiw with all needed panels
        this.ipView = new IsletProcessView(cController.getView(),
                itController.getView(), nbController.getView(),
                pController.getView(), u3DController.getUniverse3DView());

        this.ipView.setVisible(true);
        this.u3DController.addElementsSelectedListener(this);
    }

    /**
     * Gets the building islet controller.
     * @return The current building islet controller
     */
    public BuildingsIsletController getBiController()
    {
        return this.biController;
    }

    /**
     * Launches next process.
     */
    public void launchProcess()
    {
        this.ipView.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        this.biController.launchTreatment();
        this.progression++;
        this.itController.refreshView();
        this.ipView.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.nbController.getView().refreshStepTitle(this.progression);
    }
    public void abortTreatment()
    {
        this.progression=1;
        this.itController.refreshView();
        this.nbController.getView().refreshStepTitle(this.progression);
    }

    public void getPreviousTreatment()
    {
        this.progression--;
        this.itController.refreshView();
        this.nbController.getView().refreshStepTitle(this.progression);
    }

    @Override
    public void triangleSelected(Triangle triangleSelected)
    {
        switch (progression)
        {
            case 2:
                // If the characteristic panel is of another type.
                if (!(this.cController instanceof CharacteristicsStep2Controller))
                {
                    this.cController = new CharacteristicsStep2Controller(this,
                            triangleSelected);
                    this.ipView.setCharacteristicsView(cController.getView());
                } else
                {
                    ((CharacteristicsStep2Controller) this.cController)
                            .addTriangleSelected(triangleSelected);
                }
            break;
            case 3:
                // TODO check the selection mode when it will be created
                // If the characteristic panel is of another type.
                if (!(this.cController instanceof CharacteristicsStep3TrianglesController))
                {
                    this.cController = new CharacteristicsStep3TrianglesController(
                            this, triangleSelected);
                    this.ipView.setCharacteristicsView(cController.getView());
                } else
                {
                    ((CharacteristicsStep3TrianglesController) this.cController)
                            .addTriangleSelected(triangleSelected);
                }
            break;
            case 4:
                // If the characteristic panel is of another type.
                if (!(this.cController instanceof CharacteristicsStep4Controller))
                {
                    this.cController = new CharacteristicsStep4Controller(this,
                            triangleSelected);
                    this.ipView.setCharacteristicsView(cController.getView());
                } else
                {
                    ((CharacteristicsStep4Controller) this.cController)
                            .addTriangleSelected(triangleSelected);
                }
            break;
        }
    }

    @Override
    public void polygonSelected(Polygon trianglesSelected)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void triangleDeselected(Triangle triangleSelected)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void polygonDeselected(Polygon trianglesSelected)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void meshSelected(Mesh meshSelected)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void meshDeselected(Mesh meshSelected)
    {
        // TODO Auto-generated method stub

    }
}
