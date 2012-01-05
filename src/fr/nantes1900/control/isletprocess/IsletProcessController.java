package fr.nantes1900.control.isletprocess;

import java.awt.Cursor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.control.GlobalController;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.control.isletprocess.characteristics.AbstractCharacteristicsSurfacesController;
import fr.nantes1900.control.isletprocess.characteristics.CharacteristicsController;
import fr.nantes1900.control.isletprocess.characteristics.CharacteristicsStep2Controller;
import fr.nantes1900.control.isletprocess.characteristics.CharacteristicsStep3Controller;
import fr.nantes1900.control.isletprocess.characteristics.CharacteristicsStep4Controller;
import fr.nantes1900.control.isletprocess.characteristics.CharacteristicsStep5Controller;
import fr.nantes1900.control.isletprocess.characteristics.CharacteristicsStep6Controller;
import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.listener.ProgressListener;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.exceptions.WeirdResultException;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.AbstractBuildingsIslet;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.IsletProcessView;

/**
 * Controller of the process of an islet. This controller makes the link between
 * each elements in the window and also with the model which contains mesh data.
 * @author Camille Bouquet,
 * @author Luc Jallerat
 */
public class IsletProcessController implements ElementsSelectedListener,
        ProgressListener {

    /**
     * Parent controller of this one.
     */
    private GlobalController parentController;

    /**
     * The window containing all the needed panels to process an islet.
     */
    private IsletProcessView ipView;

    /**
     * The reference to the toolbar containing some functions for the universe
     * 3D.
     */
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
     * islet and select a new one, LAUNCH a new process to go on a further step
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
     * Creates a new islet process controller to LAUNCH different processes on
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
            JOptionPane.showMessageDialog(ipView, e.getMessage(), FileTools
                    .readInformationMessage(TextsKeys.KEY_ERROR_WEIRDRESULT,
                            TextsKeys.MESSAGETYPE_TITLE),
                    JOptionPane.ERROR_MESSAGE);
        }

        // Creates the window with all needed panels
        this.ipView = new IsletProcessView(this.cController.getView(),
                this.itController.getView(), this.nbController.getView(),
                this.pController.getView(),
                this.u3DController.getUniverse3DView());

        // Initialization
        this.f3DController
                .setSelectionMode(Universe3DController.SELECTION_SURFACE_MODE);
        this.f3DController
                .setDisplayType(Universe3DController.DISPLAY_MESH_MODE);
        this.setToolbarButtons();
        this.setStatusBarStep();
        this.ipView.setVisible(true);
        this.u3DController.addElementsSelectedListener(this);
    }

    /**
     * Resets all the processes and go BACK to the islet file selection.
     */
    public final void abortProcess() {
        this.parentController.launchIsletSelection();
        this.getBiController().abortProcess();
    }

    /**
     * Modifies the display type.
     * @param displayType
     *            the new selected display type
     */
    public final void changeDisplayType(final int displayType) {
        this.u3DController.setDisplayMode(displayType);
        this.setDefaultCharacteristicsPanel();
    }

    /**
     * Modifies the selection mode.
     * @param selectionMode
     *            the new selected selection mode
     */
    public final void changeSelectionMode(final int selectionMode) {
        if (selectionMode == Universe3DController.SELECTION_TRIANGLE_MODE) {
            this.u3DController
                    .changeSelectionMode(Universe3DController.SELECTION_TRIANGLE_MODE);
        } else {
            this.u3DController
                    .changeSelectionMode(Universe3DController.SELECTION_SURFACE_MODE);
        }
        setDefaultCharacteristicsPanel();
    }

    /**
     * Removes the window.
     */
    public final void disposeWindow() {
        this.ipView.dispose();
        this.ipView.setVisible(false);
    }

    /**
     * Gets the building islet controller.
     * @return The current building islet controller
     */
    public final BuildingsIsletController getBiController() {
        return this.biController;
    }

    /**
     * Getter.
     * @return the progression of the process of the current islet.
     */
    public int getProgression() {
        return this.getBiController().getIslet().getProgression();
    }

    /**
     * Gets the 3D controller.
     * @return the 3D controller
     */
    public final Universe3DController getU3DController() {
        return this.u3DController;
    }

    /**
     * Go BACK to the previous process.
     * @throws UnexistingStepException
     *             if the step does not exist (progression < 1)
     */
    public final void goToPreviousProcess() throws UnexistingStepException {
        if (this.getProgression() <= AbstractBuildingsIslet.FIRST_STEP) {
            throw new UnexistingStepException();
        }
        this.biController.getPreviousStep();
        setDefaultCharacteristicsPanel();
        setToolbarButtons();
        refreshViews();
        this.pController.displayProcessingParameters(this.getProgression());
    }

    /**
     * Launches next process.
     * @throws UnexistingStepException
     *             if the step does not exist (progression > 6)
     */
    public final void launchProcess() throws UnexistingStepException {

        if (this.getProgression() >= AbstractBuildingsIslet.SIXTH_STEP) {
            throw new UnexistingStepException();
        }

        if (this.getProgression() == AbstractBuildingsIslet.FOURTH_STEP) {
            // ipView.showProgressBar(true);

            // display the JOptionPane showConfirmDialog
            int reply = JOptionPane.showConfirmDialog(ipView, FileTools
                    .readInformationMessage(TextsKeys.KEY_WARNING_LONGPROCESS,
                            TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                    .readInformationMessage(TextsKeys.KEY_WARNING_LONGPROCESS,
                            TextsKeys.MESSAGETYPE_TITLE),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            // if the answer is OK, then the process is launched, else it is
            // aborted.
            if (reply != JOptionPane.OK_OPTION) {
                return;
            }
        }

        this.setDefaultCharacteristicsPanel();
        this.ipView.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            this.biController.launchProcess();
        } catch (WeirdResultException e) {
            JOptionPane.showMessageDialog(ipView, e.getMessage(), FileTools
                    .readInformationMessage(TextsKeys.KEY_ERROR_WEIRDRESULT,
                            TextsKeys.MESSAGETYPE_TITLE),
                    JOptionPane.ERROR_MESSAGE);
        }

        if (this.getProgression() == AbstractBuildingsIslet.FIFTH_STEP) {
            // ipView.showProgressBar(false);
        }

        this.setToolbarButtons();
        this.refreshViews();
        this.ipView.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.pController.displayProcessingParameters(this.getProgression());
    }

    /**
     * Calls the ParametersController to load the parameters.
     */
    public final void loadParameters() {
        this.pController.loadNewParameters();
    }

    /**
     * Locks or unlocks a surface selected.
     * @param lock
     *            true - locks the surface\n false - unlocks the surface
     */
    public final void lock(final boolean lock) {
        if (lock) {
            // Locks when only one surface is selected.
            if (this.u3DController.getSurfacesSelected().size() == 1) {
                this.u3DController.setSurfaceLocked(this.u3DController
                        .getSurfacesSelected().get(0));
                // Changes to LOCK mode.
                this.u3DController.setLockMode(true);
            }
        } else {
            // Changes to UNLOCK mode.
            this.u3DController.setLockMode(false);
            // Clears the surfaceLocked.
            this.u3DController.setSurfaceLocked(null);
            this.u3DController.setSurfaceLockedNeighbours(null);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.listener.ElementsSelectedListener#newTrianglesSelection
     * (java.util.List)
     */
    @Override
    public final void newTrianglesSelection(
            final List<Triangle> trianglesSelected) {
        int step = this.getProgression();

        if (step == 2 || step == 4) {
            if (trianglesSelected.isEmpty()) {
                setDefaultCharacteristicsPanel();
                this.f3DController.setRotationCenterEnable(false);
            } else {
                switch (step) {
                case 2:
                    this.cController = new CharacteristicsStep2Controller(this,
                            trianglesSelected);
                    break;
                case 4:
                    this.cController = new CharacteristicsStep4Controller(this,
                            trianglesSelected);
                    break;
                default:
                    break;

                }
                this.f3DController.setRotationCenterEnable(true);
            }
            this.ipView.setCharacteristicsView(this.cController.getView());
        }
    }

    /**
     * Calls the BuildingIsletController to clear and display the current step.
     */
    public final void refreshView() {
        try {
            this.biController.display();
        } catch (WeirdResultException e) {
            JOptionPane.showMessageDialog(ipView, e.getMessage(), FileTools
                    .readInformationMessage(TextsKeys.KEY_ERROR_WEIRDRESULT,
                            TextsKeys.MESSAGETYPE_TITLE),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Refreshes views : tree, 3D view, navigation bar and characteristics
     * panel.
     */
    public final void refreshViews() {
        this.itController.refreshView();
        setStatusBarStep();
        this.nbController.getView().refreshStepTitle(this.getProgression());
        try {
            this.biController.display();
        } catch (WeirdResultException e) {
            JOptionPane.showMessageDialog(ipView, e.getMessage(), FileTools
                    .readInformationMessage(TextsKeys.KEY_ERROR_WEIRDRESULT,
                            TextsKeys.MESSAGETYPE_TITLE),
                    JOptionPane.ERROR_MESSAGE);
        }
        setDefaultCharacteristicsPanel();
    }

    /**
     * Sets the default characteristics panel as current one.
     */
    private void setDefaultCharacteristicsPanel() {
        this.cController = new CharacteristicsController(this);
        this.ipView.setCharacteristicsView(this.cController.getView());
        this.ipView.getRootPane().getInputMap().clear();
        this.ipView.getRootPane().getActionMap().clear();
    }

    /**
     * Sets the new display mode.
     * @param displayMode
     *            the new display mode
     */
    public final void setDisplayMode(final int displayMode) {
        this.u3DController.setDisplayMode(displayMode);
        setDefaultCharacteristicsPanel();
    }

    /**
     * Sets default status bar text for the current step.
     */
    private void setStatusBarStep() {
        int step = getProgression();
        String text = "";
        text = FileTools.readHelpMessage(TextsKeys.KEY_HELP_STEP + step,
                TextsKeys.MESSAGETYPE_STATUSBAR);

        this.ipView.setStatusBarText(text);
    }

    /**
     * Sets toolbar buttons depending of the step.
     */
    private void setToolbarButtons() {
        int step = getProgression();

        // Enabling / disabling specifics selection modes, beware of order of
        // methods call
        if (step == 1 || step == 3 || step == 5 || step == 6) {
            this.f3DController.setEnableSelectionMode(false,
                    Universe3DController.SELECTION_TRIANGLE_MODE);
            this.f3DController
                    .setSelectionMode(Universe3DController.SELECTION_SURFACE_MODE);
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
            this.f3DController.setEnableDisplayType(false,
                    Universe3DController.DISPLAY_POLYGON_MODE);
            this.f3DController
                    .setDisplayType(Universe3DController.DISPLAY_MESH_MODE);
        }
        this.f3DController.setRotationCenterEnable(false);
    }

    @Override
    public final void surfaceDeselected(final Surface surfaceSelected) {
        int step = this.getProgression();

        // case 6 : more complicated
        if (step == 6) {
            if (this.u3DController.isLocked()) {
                ((AbstractCharacteristicsSurfacesController) this.cController)
                        .removeSurfaceSelected(surfaceSelected);
            } else {
                if (this.u3DController.getSurfacesSelected().size() == 1) {
                    this.cController = new CharacteristicsStep6Controller(this,
                            surfaceSelected,
                            (ArrayList<Surface>) surfaceSelected
                                    .getNeighbours());
                    this.ipView.setCharacteristicsView(this.cController
                            .getView());
                } else {
                    setDefaultCharacteristicsPanel();
                }
            }
        }

        if (!this.cController.getClass()
                .equals(CharacteristicsController.class)
                && ((step == 3 && this.f3DController.getSelectionMode() == Universe3DController.SELECTION_SURFACE_MODE) || step == 5)) {

            ((AbstractCharacteristicsSurfacesController) this.cController)
                    .removeSurfaceSelected(surfaceSelected);
            if (this.u3DController.getSurfacesSelected().isEmpty()) {
                // if the selection is now empty
                setDefaultCharacteristicsPanel();
            }
        }
        if (this.u3DController.getSurfacesSelected().isEmpty()) {
            this.f3DController.setRotationCenterEnable(false);
        }
    }

    @Override
    public final void surfaceSelected(final Surface surfaceSelected) {
        int step = this.getProgression();

        // case 6 : more complicated
        if (step == 6) {
            if (this.u3DController.isLocked()) {
                ((AbstractCharacteristicsSurfacesController) this.cController)
                        .addSurfaceSelected(surfaceSelected);
            } else {
                if (this.u3DController.getSurfacesSelected().size() == 1) {
                    this.cController = new CharacteristicsStep6Controller(this,
                            surfaceSelected, new ArrayList<>(
                                    surfaceSelected.getNeighbours()));
                    this.ipView.setCharacteristicsView(this.cController
                            .getView());
                } else {
                    setDefaultCharacteristicsPanel();
                }
            }
        }
        // step 3 in meshes selection mode or in step 5

        if ((step == 3 && this.f3DController.getSelectionMode() == Universe3DController.SELECTION_SURFACE_MODE)
                || step == 5) {
            if (this.cController.getClass().equals(
                    CharacteristicsController.class)) {
                switch (this.getProgression()) {
                case 3:
                    this.cController = new CharacteristicsStep3Controller(this,
                            surfaceSelected);
                    break;
                case 5:
                    this.cController = new CharacteristicsStep5Controller(this,
                            surfaceSelected);
                    break;
                default:
                    break;

                }
                this.ipView.setCharacteristicsView(this.cController.getView());
            } else {
                ((AbstractCharacteristicsSurfacesController) this.cController)
                        .addSurfaceSelected(surfaceSelected);
            }
        }
        this.f3DController.setRotationCenterEnable(true);
    }

    /**
     * Implements an exception if the program asked for a step that doesn't
     * exist (progression < 1 or progression > 6).
     * @author Daniel Lefevre
     */
    public class UnexistingStepException extends Exception {

        /**
         * Version ID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Constructor.
         */
        public UnexistingStepException() {
        }
    }

    /*
     * (non-Javadoc)
     * @see fr.nantes1900.listener.ProgressListener#updateProgress(double)
     */
    @Override
    public void updateProgress(final double progress) {
        // this.ipView.updatesProgressBar(progress);
    }

    /**
     * Adds a shortcut to the window.
     * @param key
     *          the shortcut key
     * @param actionName
     *          the name of the action
     * @param action
     *          the action to associate with this shortcut
     */
    public void addShortcut(KeyStroke key, String actionName, AbstractAction action)
    {
        ipView.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(key, actionName);
        ipView.getRootPane().getActionMap().put(actionName, action);
    }
}
