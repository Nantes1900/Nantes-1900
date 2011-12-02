/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.Functions3DToolbarView;

/**
 * @author Camille
 */
public class Functions3DToolbarController {

    private IsletProcessController parentController;
    private Functions3DToolbarView toolbarView;
    private int selectionMode;
    private int displayType;

    public Functions3DToolbarController(IsletProcessController parentController) {
        this.toolbarView = new Functions3DToolbarView();
        this.parentController = parentController;
        selectionMode = Universe3DController.SELECTION_TRIANGLE_MODE;
        displayType = Universe3DController.DISPLAY_MESH_MODE;

        toolbarView.getRotationCenterButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        // TODO call 3D universe controller to modify rotation
                        // center
                    }
                });

        toolbarView.getDisplayTypeMeshesButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        setDisplayType(Universe3DController.DISPLAY_MESH_MODE);
                    }
                });

        toolbarView.getDisplayTypePolygonsButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        setDisplayType(Universe3DController.DISPLAY_POLYGON_MODE);
                    }
                });

        toolbarView.getSelectionModeTrianglesButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        setSelectionMode(Universe3DController.SELECTION_TRIANGLE_MODE);
                    }
                });

        toolbarView.getSelectionModeMeshesButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        setSelectionMode(Universe3DController.SELECTION_SURFACE_MODE);
                    }
                });
    }

    public int getSelectionMode() {
        return selectionMode;
    }

    public int getDisplayType() {
        return displayType;
    }

    public Functions3DToolbarView getToolbar() {
        return toolbarView;
    }

    public void setSelectionMode(int selectionMode) {
        if (selectionMode == Universe3DController.SELECTION_TRIANGLE_MODE)
        {
            this.selectionMode = selectionMode;
            toolbarView.getSelectionModeTrianglesButton().setEnabled(false);
            toolbarView.getSelectionModeMeshesButton().setEnabled(true);
            toolbarView.getSelectionModeLabel().setText(FileTools
                    .readElementText(TextsKeys.KEY_SELECTTRIANGLESLABEL));
            Functions3DToolbarController.this.parentController
                    .changeSelectionMode(Universe3DController.SELECTION_TRIANGLE_MODE);
        } else if (selectionMode == Universe3DController.SELECTION_SURFACE_MODE)
        {
            this.selectionMode = selectionMode;
            toolbarView.getSelectionModeMeshesButton().setEnabled(false);
            toolbarView.getSelectionModeTrianglesButton().setEnabled(true);
            toolbarView.getSelectionModeLabel().setText(FileTools
                    .readElementText(TextsKeys.KEY_SELECTMESHESLABEL));
            Functions3DToolbarController.this.parentController
                    .changeSelectionMode(Universe3DController.SELECTION_SURFACE_MODE);
        }
        checkAllDisabledSelectionMode();
    }

    public void setDisplayType(int displayType) {
        if (displayType == Universe3DController.DISPLAY_MESH_MODE)
        {
            this.displayType = displayType;
            toolbarView.getDisplayTypeMeshesButton().setEnabled(false);
            toolbarView.getDisplayTypePolygonsButton().setEnabled(true);
            toolbarView.getDisplayTypeLabel().setText(FileTools
                    .readElementText(TextsKeys.KEY_DISPLAYMESHESLABEL));
            Functions3DToolbarController.this.parentController
                    .getU3DController().setDisplayMode(
                            Universe3DController.DISPLAY_MESH_MODE);
        } else if (displayType == Universe3DController.DISPLAY_POLYGON_MODE)
        {
            this.displayType = displayType;
            toolbarView.getDisplayTypePolygonsButton().setEnabled(false);
            toolbarView.getDisplayTypeMeshesButton().setEnabled(true);
            toolbarView.getDisplayTypeLabel().setText(FileTools
                    .readElementText(TextsKeys.KEY_DISPLAYPOLYGONSLABEL));
            Functions3DToolbarController.this.parentController
                    .getU3DController().setDisplayMode(
                            Universe3DController.DISPLAY_POLYGON_MODE);
        }
        
        checkAllDisabledDisplayType();
    }

    /**
     * Enables or disables the corresponding button of the selection mode.\n
     * The method setSelectionMode(int selectionMode) should be used after to be
     * sure that one mode is selected.
     * @param enable
     *          true - enables the button\n
     *          false - disables the button
     * @param selectionMode
     *          mode to enable or disable.
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
     * Enables or disables the corresponding button of the type display.\n
     * The method setDisplayType(int displayType) should be used after to be
     * sure that one display type is selected.
     * @param enable
     *          true - enables the button\n
     *          false - disables the button
     * @param selectionMode
     *          mode to enable or disable.
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
    
    private void checkAllDisabledSelectionMode()
    {
        if (!toolbarView.getSelectionModeMeshesButton().isEnabled() && !toolbarView.getSelectionModeTrianglesButton().isEnabled())
        {
            toolbarView.getSelectionModeLabel().setEnabled(false);
        } else
        {
            toolbarView.getSelectionModeLabel().setEnabled(true);
        }
    }

    private void checkAllDisabledDisplayType()
    {
        if (!toolbarView.getDisplayTypePolygonsButton().isEnabled() && !toolbarView.getDisplayTypeMeshesButton().isEnabled())
        {
            toolbarView.getDisplayTypeLabel().setEnabled(false);
        } else
        {
            toolbarView.getDisplayTypeLabel().setEnabled(true);
        }
    }
}
