/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;

/**
 * Toolbar used to manage the 3D view.
 * @author Camille Bouquet
 * @author Luc Jallerat
 */
public class Functions3DToolbarView extends JToolBar {

    /**
     * Button to modify the rotation center of the 3D view.
     */
    private JButton bRotationCenter;
    /**
     * Button to set the display type polygons.
     */
    private JButton bDisplayTypePolygons;
    /**
     * Buttons to set the display type meshes.
     */
    private JButton bDisplayTypeMeshes;
    /**
     * Button to select the mode of selection triangles.
     */
    private JButton bSelectionModeTriangles;
    /**
     * Button to select the selection mode meshes.
     */
    private JButton bSelectionModeMeshes;
    /**
     * Label to indicate the current selection mode selected.
     */
    private JLabel lSelectionMode;
    /**
     * Label to indicate the current display type set.
     */
    private JLabel lDisplayType;
    /**
     * Default generated serial UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor. Creates and adds every elements to the toolbar.
     */
    public Functions3DToolbarView() {
        super(JToolBar.HORIZONTAL);
        this.setFloatable(false);
        // TODO puts icons instead
        bRotationCenter = new JButton("o");
        bRotationCenter.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_ROTATIONCENTER));

        // Display type elements
        bDisplayTypePolygons = new JButton("#");
        bDisplayTypePolygons.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_DISPLAYPOLYGONS));
        bDisplayTypeMeshes = new JButton("$");
        bDisplayTypeMeshes.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_DISPLAYMESHES));
        lDisplayType = new JLabel("");

        // Selection mode elements
        bSelectionModeTriangles = new JButton("t");
        bSelectionModeTriangles.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_SELECTTRIANGLES));
        bSelectionModeMeshes = new JButton("m");
        bSelectionModeMeshes.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_SELECTMESHES));
        lSelectionMode = new JLabel("");

        // Adds elements to the toolbar
        FlowLayout fl = new FlowLayout(FlowLayout.LEADING, 5, 5);
        this.setLayout(fl);

        this.add(bRotationCenter);
        this.addSeparator();
        this.add(bSelectionModeTriangles);
        this.add(bSelectionModeMeshes);
        this.add(lSelectionMode);
        this.addSeparator();
        this.add(bDisplayTypeMeshes);
        this.add(bDisplayTypePolygons);
        this.add(lDisplayType);
    }

    /**
     * Gets the display type polygons button.
     * @return the display type polygons button
     */
    public JButton getDisplayTypePolygonsButton() {
        return this.bDisplayTypePolygons;
    }

    /**
     * Gets the display type meshes button.
     * @return the display type meshes button
     */
    public JButton getDisplayTypeMeshesButton() {
        return this.bDisplayTypeMeshes;
    }

    /**
     * Gets the rotation center button.
     * @return the rotation center button
     */
    public JButton getRotationCenterButton() {
        return this.bRotationCenter;
    }

    /**
     * Gets selection mode meshes button.
     * @return the selection mode meshes button
     */
    public JButton getSelectionModeMeshesButton() {
        return this.bSelectionModeMeshes;
    }

    /**
     * Gets selection mode triangles button.
     * @return the selection mode meshes button
     */
    public JButton getSelectionModeTrianglesButton() {
        return this.bSelectionModeTriangles;
    }

    /**
     * Gets selection mode label.
     * @return the selection mode label
     */
    public JLabel getSelectionModeLabel() {
        return lSelectionMode;
    }

    /**
     * Gets display type label.
     * @return the display type label
     */
    public JLabel getDisplayTypeLabel() {
        return lDisplayType;
    }
}
