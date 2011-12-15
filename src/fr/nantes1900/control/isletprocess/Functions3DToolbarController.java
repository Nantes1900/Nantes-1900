package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.models.exceptions.WeirdResultException;
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
     * Selection mode : surfaces or TRIANGLES.
     */
    private int selectionMode;
    /**
     * Display type : surfaces or polygons.
     */
    private int displayType;
    /**
     * Is the selection mode button enabled. 0 : selection mode TRIANGLES; 1 :
     * selection mode MESHES
     */
    private boolean[] selectionModeEnable = {true, true};
    /**
     * Is the display type button enabled. 0 : display type MESHES; 1 : display
     * type polygons
     */
    private boolean[] displayTypeEnable = {true, true};

    /**
     * Creates a new toolbar controller which creates the view and sets actions
     * to the different buttons.
     * @param parentControllerIn
     *            the parenet controller
     */
    public Functions3DToolbarController(
            final IsletProcessController parentControllerIn) {
        this.toolbarView = new Functions3DToolbarView();
        this.parentController = parentControllerIn;
        this.selectionMode = Universe3DController.SELECTION_TRIANGLE_MODE;
        this.displayType = Universe3DController.DISPLAY_MESH_MODE;

        // Modifies the rotation center of the 3D view
        this.toolbarView.getRotationCenterButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent arg0) {
                        parentController.getU3DController()
                                .changeRotationCenter();
                    }
                });

        // Select the display type surfaces
        this.toolbarView.getDisplayTypeMeshesButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent arg0) {
                        setDisplayType(Universe3DController.DISPLAY_MESH_MODE);
                        try
                        {
                            Functions3DToolbarController.this
                                    .getParentController().getBiController()
                                    .display();
                        } catch (WeirdResultException e)
                        {
                            JOptionPane.showMessageDialog(null, e.getMessage(), FileTools
                                    .readInformationMessage(TextsKeys.KEY_ERROR_WEIRDRESULT,
                                            TextsKeys.MESSAGETYPE_TITLE),
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

        // selects the display type polygons
        this.toolbarView.getDisplayTypePolygonsButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent arg0) {
                        setDisplayType(Universe3DController.DISPLAY_POLYGON_MODE);
                        try
                        {
                            Functions3DToolbarController.this
                                    .getParentController().getBiController()
                                    .display();
                        } catch (WeirdResultException e)
                        {
                            JOptionPane.showMessageDialog(null, e.getMessage(), FileTools
                                    .readInformationMessage(TextsKeys.KEY_ERROR_WEIRDRESULT,
                                            TextsKeys.MESSAGETYPE_TITLE),
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

        // selects the selection mode TRIANGLES
        this.toolbarView.getSelectionModeTrianglesButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent arg0) {
                        setSelectionMode(Universe3DController.SELECTION_TRIANGLE_MODE);
                    }
                });

        // selects the selection mode surfaces
        this.toolbarView.getSelectionModeMeshesButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent arg0) {
                        setSelectionMode(Universe3DController.SELECTION_SURFACE_MODE);
                    }
                });

        // modifies the distance used for TRIANGLES selection
        this.toolbarView.getDistanceSlider().addChangeListener(
                new ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent event) {
                        double newValue = new Double(((JSlider) event
                                .getSource()).getValue());
                        newValue = (newValue < 1.0) ? 1.0 : newValue;
                        Functions3DToolbarController.this.parentController
                                .getU3DController()
                                .setTriangleSelectionDistance(newValue);
                    }

                });
    }

    /**
     * Gets the parent controller.
     * @return the parent controller
     */
    public final IsletProcessController getParentController() {
        return this.parentController;
    }

    /**
     * Gets the current selection mode selected.
     * @return the current selection mode
     */
    public final int getSelectionMode() {
        return this.selectionMode;
    }

    /**
     * Gets the current display type selected.
     * @return the current display type
     */
    public final int getDisplayType() {
        return this.displayType;
    }

    /**
     * Gets the toolbar view.
     * @return the toolbar
     */
    public final Functions3DToolbarView getToolbar() {
        return this.toolbarView;
    }

    /**
     * Sets the selection mode.
     * @param selectionMode
     *            the new selection mode to set :
     *            Universe3DController.SELECTION_TRIANGLE_MODE or
     *            Universe3DController.SELECTION_SURFACE_MODE
     */
    public final void setSelectionMode(final int selectionMode) {
        if (selectionMode == Universe3DController.SELECTION_TRIANGLE_MODE)
        {
            this.toolbarView.getSelectionModeTrianglesButton()
                    .setEnabled(false);
            this.toolbarView.getDistanceSlider().setEnabled(true);
            if (this.selectionModeEnable[1])
            {
                this.toolbarView.getSelectionModeMeshesButton()
                        .setEnabled(true);
            }

            this.toolbarView
                    .getSelectionModeLabel()
                    .setText(
                            FileTools
                                    .readElementText(TextsKeys.KEY_SELECTTRIANGLESLABEL));
            Functions3DToolbarController.this.parentController
                    .changeSelectionMode(Universe3DController.SELECTION_TRIANGLE_MODE);

        } else
        {

            this.toolbarView.getSelectionModeMeshesButton().setEnabled(false);
            this.toolbarView.getDistanceSlider().setEnabled(false);

            if (this.selectionModeEnable[0])
            {
                this.toolbarView.getSelectionModeTrianglesButton().setEnabled(
                        true);
            }

            this.toolbarView.getSelectionModeLabel().setText(
                    FileTools.readElementText(TextsKeys.KEY_SELECTMESHESLABEL));
            Functions3DToolbarController.this.parentController
                    .changeSelectionMode(Universe3DController.SELECTION_SURFACE_MODE);
        }

        this.selectionMode = selectionMode;
        checkAllDisabledSelectionMode();
    }

    /**
     * Sets the display type.
     * @param displayTypeIn
     *            the new display type to set :
     *            Universe3DController.DISPLAY_MESH_MODE or
     *            Universe3DController.DISPLAY_POLYGON_MODE
     */
    public final void setDisplayType(final int displayTypeIn) {
        if (displayTypeIn == Universe3DController.DISPLAY_MESH_MODE)
        {

            this.toolbarView.getDisplayTypeMeshesButton().setEnabled(false);

            if (this.displayTypeEnable[1])
            {
                this.toolbarView.getDisplayTypePolygonsButton()
                        .setEnabled(true);
            }

            this.toolbarView
                    .getDisplayTypeLabel()
                    .setText(
                            FileTools
                                    .readElementText(TextsKeys.KEY_DISPLAYMESHESLABEL));

            Functions3DToolbarController.this.parentController
                    .getU3DController().setDisplayMode(
                            Universe3DController.DISPLAY_MESH_MODE);

        } else
        {
            this.toolbarView.getDisplayTypePolygonsButton().setEnabled(false);

            if (this.displayTypeEnable[0])
            {
                this.toolbarView.getDisplayTypeMeshesButton().setEnabled(true);
            }

            this.toolbarView
                    .getDisplayTypeLabel()
                    .setText(
                            FileTools
                                    .readElementText(TextsKeys.KEY_DISPLAYPOLYGONSLABEL));

            Functions3DToolbarController.this.parentController
                    .getU3DController().setDisplayMode(
                            Universe3DController.DISPLAY_POLYGON_MODE);
        }

        this.displayType = displayTypeIn;
        checkAllDisabledDisplayType();
    }

    /**
     * Enables or disables the corresponding button of the selection mode.\n The
     * method setSelectionMode(int selectionMode) should be used after to be
     * sure that one mode is selected.
     * @param enable
     *            true - enables the button\n false - disables the button
     * @param selectionModeIn
     *            mode to enable or disable.
     */
    public final void setEnableSelectionMode(final boolean enable,
            final int selectionModeIn) {

        if (selectionModeIn == Universe3DController.SELECTION_TRIANGLE_MODE)
        {
            this.toolbarView.getSelectionModeTrianglesButton().setEnabled(
                    enable);
            this.selectionModeEnable[0] = enable;

        } else if (selectionModeIn == Universe3DController.SELECTION_SURFACE_MODE)
        {
            this.toolbarView.getSelectionModeMeshesButton().setEnabled(enable);
            this.selectionModeEnable[1] = enable;
        }

        checkAllDisabledSelectionMode();
    }

    /**
     * Enables or disables the corresponding button of the type display.\n The
     * method setDisplayType(int displayType) should be used after to be sure
     * that one display type is selected.
     * @param enable
     *            true - enables the button\n false - disables the button
     * @param newDisplayType
     *            the new dislay type
     */
    public final void setEnableDisplayType(final boolean enable,
            final int newDisplayType) {

        if (newDisplayType == Universe3DController.DISPLAY_MESH_MODE)
        {
            this.toolbarView.getDisplayTypeMeshesButton().setEnabled(enable);
            this.displayTypeEnable[0] = enable;

        } else if (newDisplayType == Universe3DController.DISPLAY_POLYGON_MODE)
        {
            this.toolbarView.getDisplayTypePolygonsButton().setEnabled(enable);
            this.displayTypeEnable[1] = enable;
        }

        checkAllDisabledDisplayType();
    }

    /**
     * Checks if all selection mode buttons are disabled. If it is the case,
     * disabled the label associated with. If not, enables it.
     */
    private void checkAllDisabledSelectionMode() {

        if (!this.toolbarView.getSelectionModeMeshesButton().isEnabled()
                && !this.toolbarView.getSelectionModeTrianglesButton()
                        .isEnabled())
        {
            this.toolbarView.getSelectionModeLabel().setEnabled(false);

        } else
        {
            this.toolbarView.getSelectionModeLabel().setEnabled(true);
        }
    }

    /**
     * Checks if all display type buttons are disabled. If it is the case,
     * disabled the label associated with. If not, enables it.
     */
    private void checkAllDisabledDisplayType() {
        if (!this.toolbarView.getDisplayTypePolygonsButton().isEnabled()
                && !this.toolbarView.getDisplayTypeMeshesButton().isEnabled())
        {
            this.toolbarView.getDisplayTypeLabel().setEnabled(false);

        } else
        {
            this.toolbarView.getDisplayTypeLabel().setEnabled(true);
        }
    }

    /**
     * Enables or disables the rotation center button.
     * @param enable
     *            true - enables the rotation center button\n false - disables
     *            the rotation mode button
     */
    public final void setRotationCenterEnable(final boolean enable) {
        this.toolbarView.getRotationCenterButton().setEnabled(enable);
    }
}
