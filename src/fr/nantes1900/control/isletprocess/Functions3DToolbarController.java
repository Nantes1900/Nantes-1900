/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

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

    public static final String ACTION_TRIANGLES = "triangles";
    public static final String ACTION_POLYGONS = "polygons";
    public static final String ACTION_MESHES = "meshes";

    public Functions3DToolbarController(IsletProcessController parentController) {
        this.toolbarView = new Functions3DToolbarView();
        this.parentController = parentController;
        selectionMode = Universe3DController.SELECTION_TRIANGLE_MODE;

        toolbarView.getRotationCenterButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        // TODO call 3D universe controller to modify rotation
                        // center
                    }
                });

        toolbarView.getLockButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JButton source = ((JButton) arg0.getSource());
                if (source.getName().equals("lock"))
                {
                    Functions3DToolbarController.this.parentController
                            .lock(true);
                    source.setName("unlock");
                    source.setText("/p");
                    source.setToolTipText(FileTools
                            .readElementText(TextsKeys.KEY_UNLOCKMESH));
                } else if (source.getName().equals("unlock"))
                {
                    Functions3DToolbarController.this.parentController
                            .lock(false);
                    source.setName("lock");
                    source.setText("p");
                    source.setToolTipText(FileTools
                            .readElementText(TextsKeys.KEY_LOCKMESH));
                }
            }
        });

        toolbarView.getDisplayTypeButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        JButton source = ((JButton) arg0.getSource());
                        if (source.getName().equals(ACTION_MESHES))
                        {
                            source.setName(ACTION_POLYGONS);
                            source.setText("#");
                            source.setToolTipText(FileTools
                                    .readElementText(TextsKeys.KEY_DISPLAYPOLYGONS));
                            Functions3DToolbarController.this.parentController
                                    .setDisplayMode(Universe3DController.DISPLAY_MESH_MODE);
                        } else if (source.getName().equals(ACTION_POLYGONS))
                        {
                            source.setName(ACTION_MESHES);
                            source.setText("$");
                            source.setToolTipText(FileTools
                                    .readElementText(TextsKeys.KEY_DISPLAYMESHES));
                            Functions3DToolbarController.this.parentController
                                    .setDisplayMode(Universe3DController.DISPLAY_POLYGON_MODE);
                        }
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

    public Functions3DToolbarView getToolbar() {
        return toolbarView;
    }

    public int getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(int selectionMode) {
        this.selectionMode = selectionMode;
        if (selectionMode == Universe3DController.SELECTION_TRIANGLE_MODE)
        {
            toolbarView.getSelectionModeTrianglesButton().setEnabled(false);
            toolbarView.getSelectionModeMeshesButton().setEnabled(true);
            toolbarView.setSelectionModeText(FileTools
                    .readElementText(TextsKeys.KEY_SELECTTRIANGLESLABEL));
            Functions3DToolbarController.this.parentController
                    .changeSelectionMode(Universe3DController.SELECTION_TRIANGLE_MODE);
        } else if (selectionMode == Universe3DController.SELECTION_SURFACE_MODE)
        {
            toolbarView.getSelectionModeMeshesButton().setEnabled(false);
            toolbarView.getSelectionModeTrianglesButton().setEnabled(true);
            toolbarView.setSelectionModeText(FileTools
                    .readElementText(TextsKeys.KEY_SELECTMESHESLABEL));
            Functions3DToolbarController.this.parentController
                    .changeSelectionMode(Universe3DController.SELECTION_SURFACE_MODE);
        }
    }
}
