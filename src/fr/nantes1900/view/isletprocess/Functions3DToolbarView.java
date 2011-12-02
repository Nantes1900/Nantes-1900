/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;

/**
 * TODO .
 * @author Camille
 */
public class Functions3DToolbarView extends JToolBar {

    /**
     * TODO .
     */
    private JButton bRotationCenter;
    /**
     * TODO .
     */
    private JButton bDisplayTypePolygons;
    /**
     * TODO .
     */
    private JButton bDisplayTypeMeshes;
    /**
     * TODO .
     */
    private JButton bSelectionModeTriangles;
    /**
     * TODO .
     */
    private JButton bSelectionModeMeshes;
    private JLabel lSelectionMode;
    private JLabel lDisplayType;
    /**
     * Default generated serial UID.
     */
    private static final long serialVersionUID = 1L;

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
        setMinimumSize(new Dimension(30, 0));
    }

    public JButton getDisplayTypePolygonsButton() {
        return this.bDisplayTypePolygons;
    }

    public JButton getDisplayTypeMeshesButton() {
        return this.bDisplayTypeMeshes;
    }

    public JButton getRotationCenterButton() {
        return this.bRotationCenter;
    }

    public JButton getSelectionModeMeshesButton() {
        return this.bSelectionModeMeshes;
    }

    public JButton getSelectionModeTrianglesButton() {
        return this.bSelectionModeTriangles;
    }

    public JLabel getSelectionModeLabel() {
        return lSelectionMode;
    }

    public JLabel getDisplayTypeLabel() {
        return lDisplayType;
    }
}
