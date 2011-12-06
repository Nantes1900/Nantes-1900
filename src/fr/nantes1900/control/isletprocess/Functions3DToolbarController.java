package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.models.islets.buildings.exceptions.WeirdResultException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.Functions3DToolbarView;

/**
 * Controller of the toolbar for the 3D view.
 * @author Camille
 */
public class Functions3DToolbarController {

    /**
     * Parent controller of this controller.
     */
    private IsletProcessController parentController;

    /**
     * View of the toolbar.
     */
    private Functions3DToolbarView toolbarView;
    /**
     * Selection mode : surfaces or triangles.
     */
    private int selectionMode;
    /**
     * Display type : surfaces or polygons.
     */
    private int displayType;

    /**
     * Creates a new toolbar controller which creates the view and sets actions
     * to the different buttons.
     * @param parentController
     *            the parenet controller
     */
    public Functions3DToolbarController(IsletProcessController parentController) {
        this.toolbarView = new Functions3DToolbarView();
        this.parentController = parentController;
        selectionMode = Universe3DController.SELECTION_TRIANGLE_MODE;
        displayType = Universe3DController.DISPLAY_MESH_MODE;

        // Modifies the rotation center of the 3D view
        toolbarView.getRotationCenterButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        Functions3DToolbarController.this.parentController
                                .getU3DController().changeRotationCenter();
                    }
                });

        // Select the display type surfaces
        toolbarView.getDisplayTypeMeshesButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        setDisplayType(Universe3DController.DISPLAY_MESH_MODE);
                        try
                        {
                            Functions3DToolbarController.this
                                    .getParentController().getBiController()
                                    .display();
                        } catch (WeirdResultException e)
                        {
                            // TODO by Camille : pop-up
                            e.printStackTrace();
                        }
                    }
                });

        // selects the display type polygons
        toolbarView.getDisplayTypePolygonsButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        setDisplayType(Universe3DController.DISPLAY_POLYGON_MODE);
                        try
                        {
                            Functions3DToolbarController.this
                                    .getParentController().getBiController()
                                    .display();
                        } catch (WeirdResultException e)
                        {
                            // TODO by Camille : pop-up
                            e.printStackTrace();
                        }
                    }
                });

        // selects the selection mode triangles
        toolbarView.getSelectionModeTrianglesButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        setSelectionMode(Universe3DController.SELECTION_TRIANGLE_MODE);
                    }
                });

        // selects the selection mode surfaces
        toolbarView.getSelectionModeMeshesButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        setSelectionMode(Universe3DController.SELECTION_SURFACE_MODE);
                    }
                });
    }

    /**
     * Gets the parent controller.
     * @return the parent controller
     */
    public IsletProcessController getParentController() {
        return this.parentController;
    }

    /**
     * Gets the current selection mode selected.
     * @return
     *         the current selection mode
     */
    public int getSelectionMode() {
        return selectionMode;
    }

    /**
     * Gets the current display type selected.
     * @return the current display type
     */
    public int getDisplayType() {
        return displayType;
    }

    /**
     * Gets the toolbar view.
     * @return the toolbar
     */
    public Functions3DToolbarView getToolbar() {
        return toolbarView;
    }

    /**
     * Sets the selection mode.
     * @param selectionMode
     *            the new selection mode to set :
     *            Universe3DController.SELECTION_TRIANGLE_MODE or
     *            Universe3DController.SELECTION_SURFACE_MODE
     */
    public void setSelectionMode(int selectionMode) {
        if (selectionMode == Universe3DController.SELECTION_TRIANGLE_MODE)
        {
            this.selectionMode = selectionMode;
            toolbarView.getSelectionModeTrianglesButton().setEnabled(false);
            toolbarView.getSelectionModeMeshesButton().setEnabled(true);
            toolbarView
                    .getSelectionModeLabel()
                    .setText(
                            FileTools
                                    .readElementText(TextsKeys.KEY_SELECTTRIANGLESLABEL));
            Functions3DToolbarController.this.parentController
                    .changeSelectionMode(Universe3DController.SELECTION_TRIANGLE_MODE);
        } else
        {
            this.selectionMode = selectionMode;
            toolbarView.getSelectionModeMeshesButton().setEnabled(false);
            toolbarView.getSelectionModeTrianglesButton().setEnabled(true);
            toolbarView.getSelectionModeLabel().setText(
                    FileTools.readElementText(TextsKeys.KEY_SELECTMESHESLABEL));
            Functions3DToolbarController.this.parentController
                    .changeSelectionMode(Universe3DController.SELECTION_SURFACE_MODE);
        }
        checkAllDisabledSelectionMode();
    }

    /**
     * Sets the display type.
     * @param displayType
     *            the new display type to set :
     *            Universe3DController.DISPLAY_MESH_MODE or
     *            Universe3DController.DISPLAY_POLYGON_MODE
     */
    public void setDisplayType(int displayType) {
        if (displayType == Universe3DController.DISPLAY_MESH_MODE)
        {
            this.displayType = displayType;
            toolbarView.getDisplayTypeMeshesButton().setEnabled(false);
            toolbarView.getDisplayTypePolygonsButton().setEnabled(true);
            toolbarView
                    .getDisplayTypeLabel()
                    .setText(
                            FileTools
                                    .readElementText(TextsKeys.KEY_DISPLAYMESHESLABEL));
            Functions3DToolbarController.this.parentController
                    .getU3DController().setDisplayMode(
                            Universe3DController.DISPLAY_MESH_MODE);
        } else
        {
            this.displayType = displayType;
            toolbarView.getDisplayTypePolygonsButton().setEnabled(false);
            toolbarView.getDisplayTypeMeshesButton().setEnabled(true);
            toolbarView
                    .getDisplayTypeLabel()
                    .setText(
                            FileTools
                                    .readElementText(TextsKeys.KEY_DISPLAYPOLYGONSLABEL));
            Functions3DToolbarController.this.parentController
                    .getU3DController().setDisplayMode(
                            Universe3DController.DISPLAY_POLYGON_MODE);
        }

        checkAllDisabledDisplayType();
    }

    /**
     * Enables or disables the corresponding button of the selection mode.\n The
     * method setSelectionMode(int selectionMode) should be used after to be
     * sure that one mode is selected.
     * @param enable
     *            true - enables the button\n false - disables the button
     * @param selectionMode
     *            mode to enable or disable.
     */
    public void setEnableSelectionMode(boolean enable, int selectionMode) {
        if (selectionMode == Universe3DController.SELECTION_TRIANGLE_MODE)
        {
            toolbarView.getSelectionModeTrianglesButton().setEnabled(enable);
        } else if (selectionMode == Universe3DController.SELECTION_SURFACE_MODE)
        {
            toolbarView.getSelectionModeMeshesButton().setEnabled(enable);
        }
        checkAllDisabledSelectionMode();
    }

    /**
     * Enables or disables the corresponding button of the type display.\n The
     * method setDisplayType(int displayType) should be used after to be sure
     * that one display type is selected.
     * @param enable
     *            true - enables the button\n false - disables the button
     * @param selectionMode
     *            mode to enable or disable.
     */
    public void setEnableDisplayType(boolean enable, int dislayType) {
        if (dislayType == Universe3DController.DISPLAY_MESH_MODE)
        {
            toolbarView.getDisplayTypeMeshesButton().setEnabled(enable);
        } else if (dislayType == Universe3DController.DISPLAY_POLYGON_MODE)
        {
            toolbarView.getDisplayTypePolygonsButton().setEnabled(enable);
        }

        checkAllDisabledDisplayType();
    }

    /**
     * Checks if all selection mode buttons are disabled. If it is the case,
     * disabled the label associated with. If not, enables it.
     */
    private void checkAllDisabledSelectionMode() {
        if (!toolbarView.getSelectionModeMeshesButton().isEnabled()
                && !toolbarView.getSelectionModeTrianglesButton().isEnabled())
        {
            toolbarView.getSelectionModeLabel().setEnabled(false);
        } else
        {
            toolbarView.getSelectionModeLabel().setEnabled(true);
        }
    }

    /**
     * Checks if all display type buttons are disabled. If it is the case,
     * disabled the label associated with. If not, enables it.
     */
    private void checkAllDisabledDisplayType() {
        if (!toolbarView.getDisplayTypePolygonsButton().isEnabled()
                && !toolbarView.getDisplayTypeMeshesButton().isEnabled())
        {
            toolbarView.getDisplayTypeLabel().setEnabled(false);
        } else
        {
            toolbarView.getDisplayTypeLabel().setEnabled(true);
        }
    }

    /**
     * Enables or disables the rotation center button.
     * @param enable
     *            true - enables the rotation center button\n
     *            false - disables the rotation mode button
     */
    public void setRotationCenterEnable(boolean enable) {
        toolbarView.getRotationCenterButton().setEnabled(enable);
    }
}
