/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.control.isletprocess.Functions3DToolbarController;
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
    private JButton bLockButton;
    /**
     * TODO .
     */
    private JButton bDisplayType;
    /**
     * TODO .
     */
    private JButton bSelectionModeTriangles;
    /**
     * TODO .
     */
    private JButton bSelectionModeMeshes;
    private JLabel lSelectionMode;
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

        bDisplayType = new JButton("$");
        bDisplayType.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_DISPLAYMESHES));
        bDisplayType.setName(Functions3DToolbarController.ACTION_MESHES);

        bLockButton = new JButton("p");
        bLockButton.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_LOCKMESH));
        bLockButton.setName("lock");

        bSelectionModeTriangles = new JButton("t");
        bSelectionModeTriangles.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_SELECTTRIANGLES));
        bSelectionModeMeshes = new JButton("m");
        bSelectionModeMeshes.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_SELECTMESHES));
        lSelectionMode = new JLabel("");

        this.add(bRotationCenter);
        this.add(bSelectionModeTriangles);
        this.add(bSelectionModeMeshes);
        this.add(lSelectionMode);
        setMinimumSize(new Dimension(30, 0));
    }

    public JButton getLockButton() {
        return this.bLockButton;
    }

    public JButton getRotationCenterButton() {
        return this.bRotationCenter;
    }

    public JButton getDisplayTypeButton() {
        return this.bDisplayType;
    }

    public JButton getSelectionModeTrianglesButton() {
        return this.bSelectionModeTriangles;
    }

    public JButton getSelectionModeMeshesButton() {
        return this.bSelectionModeMeshes;
    }

    public void showLockButton(boolean show) {
        if (show)
        {
            this.add(bLockButton);
        } else
        {
            this.remove(bLockButton);
        }
    }

    public void showTypeDisplayButton(boolean show) {
        if (show)
        {
            this.add(bDisplayType);
        } else
        {
            this.remove(bDisplayType);
        }
    }

    public void setSelectionModeText(String readElementText) {
        lSelectionMode.setText(readElementText);
    }
}
