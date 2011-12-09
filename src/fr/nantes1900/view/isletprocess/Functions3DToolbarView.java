package fr.nantes1900.view.isletprocess;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import fr.nantes1900.constants.Icons;
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
        this.bRotationCenter = new JButton(new ImageIcon(Icons.rotation_center));
        this.bRotationCenter.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_ROTATIONCENTER));

        // Display type elements
        this.bDisplayTypePolygons = new JButton(new ImageIcon(Icons.polygon));
        this.bDisplayTypePolygons.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_DISPLAYPOLYGONS));
        this.bDisplayTypeMeshes = new JButton(new ImageIcon(Icons.meshes));
        this.bDisplayTypeMeshes.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_DISPLAYMESHES));
        this.lDisplayType = new JLabel("");

        // Selection mode elements
        this.bSelectionModeTriangles = new JButton(new ImageIcon(Icons.triangles));
        this.bSelectionModeTriangles.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_SELECTTRIANGLES));
        this.bSelectionModeMeshes = new JButton(new ImageIcon(Icons.surface));
        this.bSelectionModeMeshes.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_SELECTMESHES));
        this.lSelectionMode = new JLabel("");

        // Adds elements to the toolbar
        FlowLayout fl = new FlowLayout(FlowLayout.LEADING, 5, 5);
        this.setLayout(fl);

        this.add(this.bRotationCenter);
        this.addSeparator();
        this.add(this.bSelectionModeTriangles);
        this.add(this.bSelectionModeMeshes);
        this.add(this.lSelectionMode);
        this.addSeparator();
        this.add(this.bDisplayTypeMeshes);
        this.add(this.bDisplayTypePolygons);
        this.add(this.lDisplayType);
    }

    /**
     * Gets the display type polygons button.
     * @return the display type polygons button
     */
    public final JButton getDisplayTypePolygonsButton() {
        return this.bDisplayTypePolygons;
    }

    /**
     * Gets the display type meshes button.
     * @return the display type meshes button
     */
    public final JButton getDisplayTypeMeshesButton() {
        return this.bDisplayTypeMeshes;
    }

    /**
     * Gets the rotation center button.
     * @return the rotation center button
     */
    public final JButton getRotationCenterButton() {
        return this.bRotationCenter;
    }

    /**
     * Gets selection mode meshes button.
     * @return the selection mode meshes button
     */
    public final JButton getSelectionModeMeshesButton() {
        return this.bSelectionModeMeshes;
    }

    /**
     * Gets selection mode triangles button.
     * @return the selection mode meshes button
     */
    public final JButton getSelectionModeTrianglesButton() {
        return this.bSelectionModeTriangles;
    }

    /**
     * Gets selection mode label.
     * @return the selection mode label
     */
    public final JLabel getSelectionModeLabel() {
        return this.lSelectionMode;
    }

    /**
     * Gets display type label.
     * @return the display type label
     */
    public final JLabel getDisplayTypeLabel() {
        return this.lDisplayType;
    }
}
