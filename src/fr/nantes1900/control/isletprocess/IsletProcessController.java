package fr.nantes1900.control.isletprocess;

import java.awt.Cursor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.control.GlobalController;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.islets.buildings.exceptions.WeirdResultException;
import fr.nantes1900.view.isletprocess.IsletProcessView;

/**
 * Controller of the process of an islet. This controller makes the link between
 * each elements in the window and also with the model which contains mesh
 * datas.
 * @author Camille Bouquet, Luc Jallerat
 */
public class IsletProcessController implements ElementsSelectedListener {

    /**
     * Parent controller of this one.
     */
    private GlobalController parentController;

    /**
     * The window containing all the needed panels to process an islet.
     */
    private IsletProcessView ipView;

    private Functions3DToolbarController f3DController;

    /**
     * Controller of the current characteristic panel.
     */
    private CharacteristicsController cController;

    /**
     * Controller of the tree showing the architecture of data in the current
     * step.
     */
    private IsletTreeController itController;

    /**
     * Controller of the navigation bar which allows to abort the process of an
     * islet and select a new one, launch a new process to go on a further step
     * and so on.
     */
    private NavigationBarController nbController;

    /**
     * Controller of the parameter panel which allows to modify parameters for
     * the next process.
     */
    private ParametersController pController;

    /**
     * Controller of the 3d View which displays meshes.
     */
    private Universe3DController u3DController;

    /**
     * Controller of the building islet currently under process. This one makes
     * the link with meshes data.
     */
    private BuildingsIsletController biController;

    /**
     * Creates a new islet process controller to launch different processes on
     * an islet.
     * @param parentControllerIn
     *            The parent controller which makes the link with other windows.
     * @param isletFile
     *            File containing data of the selectedd islet.
     * @param biControllerIn
     *            Controller to handle the islet data.
     */
    public IsletProcessController(final GlobalController parentControllerIn,
            final File isletFile, final BuildingsIsletController biControllerIn) {
        this.parentController = parentControllerIn;
        this.biController = biControllerIn;
        this.cController = new CharacteristicsController(this);
        this.itController = new IsletTreeController(this);
        this.nbController = new NavigationBarController(this);
        this.pController = new ParametersController(this);
        this.f3DController = new Functions3DToolbarController(this);
        this.u3DController = new Universe3DController();
        this.u3DController.getUniverse3DView().setToolbar(
                this.f3DController.getToolbar());

        this.biController.setUniverse3DController(this.u3DController);
        try {
            this.biController.display();
        } catch (WeirdResultException e) {
            // TODO by Camille : pop_up
            e.printStackTrace();
        }

        // creates the windiw with all needed panels
        this.ipView = new IsletProcessView(this.cController.getView(),
                this.itController.getView(), this.nbController.getView(),
                this.pController.getView(),
                this.u3DController.getUniverse3DView());

        setToolbarButtons();
        this.ipView.setVisible(true);
        this.u3DController.addElementsSelectedListener(this);
    }

    public void abortProcess() {
        this.parentController.launchIsletSelection();
        this.getBiController().abortProcess();
    }

    public void changeSelectionMode(int selectionMode) {
        if (selectionMode == Universe3DController.SELECTION_TRIANGLE_MODE) {
            u3DController
                    .changeSelectionMode(Universe3DController.SELECTION_TRIANGLE_MODE);
        } else {
            u3DController
                    .changeSelectionMode(Universe3DController.SELECTION_SURFACE_MODE);
        }
        setDefaultCharacterisitcsPanel();
    }

    public void deselectAllSurfaces() {
        u3DController.deselectEverySurfaces();
    }

    /**
     * Gets the building islet controller.
     * @return The current building islet controller
     */
    public final BuildingsIsletController getBiController() {
        return this.biController;
    }

    private int getProgression() {
        return this.getBiController().getIslet().getProgression();
    }

    public Universe3DController getU3DController() {
        return this.u3DController;
    }

    public void goToPreviousProcess() throws UnexistingStepException {
        if (this.getProgression() <= AbstractBuildingsIslet.FIRST_STEP) {
            throw new UnexistingStepException();
        }
        this.biController.getPreviousStep();
        setDefaultCharacterisitcsPanel();
        refreshViews();
        setToolbarButtons();
        this.pController.displayProcessingParameters(this.getProgression());
    }

    /**
     * Launches next process.
     */
    public final void launchProcess() throws UnexistingStepException {
        if (this.getProgression() >= AbstractBuildingsIslet.SIXTH_STEP) {
            throw new UnexistingStepException();
        }
        setDefaultCharacterisitcsPanel();
        this.ipView.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            this.biController.launchProcess();
        } catch (WeirdResultException e) {
            // TODO by Camille : pop-up
            e.printStackTrace();
        }
        refreshViews();
        this.ipView.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        setToolbarButtons();
        this.pController.displayProcessingParameters(this.getProgression());
    }

    public void refreshViews() {
        this.itController.refreshView();
        this.nbController.getView().refreshStepTitle(this.getProgression());
        try {
            this.biController.display();
        } catch (WeirdResultException e) {
            // TODO by Camille : pop-up
            e.printStackTrace();
        }
        setDefaultCharacterisitcsPanel();
    }

    public void loadParameters() {
        this.pController.loadNewParameters();
    }

    public void lock(boolean lock) {
        // TODO lock and unlock in the universe 3d and maybe the tree too.
    }

    public void refreshView() {
        try {
            this.biController.display();
        } catch (WeirdResultException e) {
            // TODO by Camille : pop-up.
            e.printStackTrace();
        }
    }

    private void setDefaultCharacterisitcsPanel() {
        this.cController = new CharacteristicsController(this);
        this.ipView.setCharacteristicsView(this.cController.getView());
    }

    public void setDisplayMode(int displayMode) {
        u3DController.setDisplayMode(displayMode);
        setDefaultCharacterisitcsPanel();
    }

    private void setToolbarButtons() {
        int step = getProgression();

        // Enabling / disabling specifics selection modes, beware of order of
        // methods call
        if (step == 1 || step == 5 || step == 6) {
            this.f3DController
                    .setSelectionMode(Universe3DController.SELECTION_SURFACE_MODE);
            this.f3DController.setEnableSelectionMode(false,
                    Universe3DController.SELECTION_TRIANGLE_MODE);
        } else {
            this.f3DController.setEnableSelectionMode(true,
                    Universe3DController.SELECTION_TRIANGLE_MODE);
            this.f3DController
                    .setSelectionMode(Universe3DController.SELECTION_TRIANGLE_MODE);
        }

        // Enabling / disabling specifics display types
        if (step == 6) {
            this.f3DController.setEnableDisplayType(true,
                    Universe3DController.DISPLAY_POLYGON_MODE);
            this.f3DController
                    .setDisplayType(Universe3DController.DISPLAY_POLYGON_MODE);
        } else {
            this.f3DController
                    .setDisplayType(Universe3DController.DISPLAY_MESH_MODE);
            this.f3DController.setEnableDisplayType(false,
                    Universe3DController.DISPLAY_POLYGON_MODE);
        }
        f3DController.setRotationCenterEnable(false);
    }

    @Override
    public void surfaceDeselected(final Surface surfaceSelected) {
        int step = this.getProgression();

        if (!this.cController.getClass()
                .equals(CharacteristicsController.class)
                && ((step == 3 && f3DController.getSelectionMode() == Universe3DController.SELECTION_SURFACE_MODE)
                        || step == 5 || step == 6)) {
            
            if(((AbstractCharacteristicsSurfacesController) cController)
                    .removeSurfaceSelected(surfaceSelected))
            {
                // if the selection is now empty
                setDefaultCharacterisitcsPanel();
            }
        }
        if (u3DController.getMeshesSelected().isEmpty())
        {
            f3DController.setRotationCenterEnable(false);
        }
    }

    @Override
    public void surfaceSelected(Surface surfaceSelected) {
        int step = this.getProgression();

        // step 3 in meshes selection mode or in step 5 or 6.
        if ((step == 3 && f3DController.getSelectionMode() == Universe3DController.SELECTION_SURFACE_MODE)
                || step == 5 || step == 6) {
            if (this.cController.getClass().equals(
                    CharacteristicsController.class)) {
                switch (this.getProgression()) {
                case 3:
                    this.cController = new CharacteristicsStep3ElementsController(
                            this, surfaceSelected);
                    break;
                case 5:
                    this.cController = new CharacteristicsStep5Controller(this,
                            surfaceSelected);
                    break;
                case 6:
                    this.cController = new CharacteristicsStep6Controller(this,
                            surfaceSelected,
                            (ArrayList<Surface>) surfaceSelected
                                    .getNeighbours());
                    break;

                }
                this.ipView.setCharacteristicsView(this.cController.getView());
            } else {
                ((AbstractCharacteristicsSurfacesController) this.cController)
                        .addSurfaceSelected(surfaceSelected);
            }
        }
        f3DController.setRotationCenterEnable(true);
    }

    // FIXME : Luc, please find some correct names.
    public final void throwInTheBin() {
        this.ipView.dispose();
        this.ipView.setVisible(false);

        // TODO : if it is not a really big object, don't do this : it's kinda
        // dirty.
        this.ipView = null;
    }

    @Override
    public void newTrianglesSelection(List<Triangle> trianglesSelected) {
        int step = this.getProgression();

        if ((step == 3 && f3DController.getSelectionMode() == Universe3DController.SELECTION_TRIANGLE_MODE)
                || step == 2 || step == 4) {
            if (trianglesSelected.isEmpty()) {
                setDefaultCharacterisitcsPanel();
                f3DController.setRotationCenterEnable(false);
            } else {
                switch (step) {
                case 2:
                    this.cController = new CharacteristicsStep2Controller(this,
                            trianglesSelected);
                    break;
                case 3:
                    this.cController = new CharacteristicsStep3TrianglesController(
                            this, trianglesSelected);
                    break;
                case 4:
                    this.cController = new CharacteristicsStep4Controller(this,
                            trianglesSelected);
                    break;

                }
                f3DController.setRotationCenterEnable(true);
            }
            this.ipView.setCharacteristicsView(this.cController.getView());
        }
    }

    public class UnexistingStepException extends Exception {

        /**
         * Version ID.
         */
        private static final long serialVersionUID = 1L;

        public UnexistingStepException() {
        }
    }

}
