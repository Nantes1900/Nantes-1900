/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JToolBar;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.control.isletprocess.Functions3DToolbarController;
import fr.nantes1900.utils.FileTools;


/**
 * @author Camille
 */
public class Functions3DToolbarView extends JToolBar
{
    private JButton bRotationCenter;
    private JButton bLockButton;
    private JButton bDisplayType;
    private JButton bDeselectTriangles;
    private JButton bDeselectMeshes;
    private JButton bSelectionMode;
    /**
     * Default generated serial UID.
     */
    private static final long serialVersionUID = 1L;

    public Functions3DToolbarView()
    {
        super(JToolBar.VERTICAL);
        
        // TODO puts icons instead
        bRotationCenter = new JButton("o");
        bRotationCenter.setToolTipText(FileTools.readElementText(TextsKeys.KEY_ROTATIONCENTER));
        
        bDisplayType = new JButton("$");
        bDisplayType.setToolTipText(FileTools.readElementText(TextsKeys.KEY_DISPLAYMESHES));
        bDisplayType.setName(Functions3DToolbarController.ACTION_MESHES);
        bLockButton= new JButton("p");
        bLockButton.setToolTipText(FileTools.readElementText(TextsKeys.KEY_LOCKMESH));
        bLockButton.setName("lock");
        bDeselectMeshes = new JButton("cm");
        bDeselectMeshes.setToolTipText(FileTools.readElementText(TextsKeys.KEY_DESELECTMESHES));
        bDeselectTriangles = new JButton("ct");
        bDeselectTriangles.setToolTipText(FileTools.readElementText(TextsKeys.KEY_DESELECTTRIANGLES));
        bSelectionMode = new JButton("t");
        bSelectionMode.setToolTipText(FileTools.readElementText(TextsKeys.KEY_SELECTTRIANGLES));
        bLockButton.setName(Functions3DToolbarController.ACTION_TRIANGLES);
        
        this.add(bRotationCenter);
        setMinimumSize(new Dimension(30, 0));
    }
    
    public JButton getLockButton()
    {
        return this.bLockButton;
    }
    
    public JButton getRotationCenterButton()
    {
        return this.bRotationCenter;
    }
    
    public JButton getDisplayTypeButton()
    {
        return this.bDisplayType;
    }
    
    public JButton getDeselectMeshesButton()
    {
        return this.bDeselectMeshes;
    }
    
    public JButton getDeselectTrianglesButton()
    {
        return this.bDeselectTriangles;
    }
    
    public JButton getSelectionModeButton()
    {
        return this.bSelectionMode;
    }
    
    public void showLockButton(boolean show)
    {
        if (show)
        {
            this.add(bLockButton);
        } else
        {
            this.remove(bLockButton);
        }
    }

    public void showTypeDisplayButton(boolean show)
    {
        if (show)
        {
            this.add(bDisplayType);
        } else
        {
            this.remove(bDisplayType);
        }
    }
    
    public void showDeselectMeshesButton(boolean show)
    {
        if (show)
        {
            this.add(bDeselectMeshes);
        } else
        {
            this.remove(bDeselectMeshes);
        }
    }
    
    public void showDeselectTrianglesButton(boolean show)
    {
        if (show)
        {
            this.add(bDeselectTriangles);
        } else
        {
            this.remove(bDeselectTriangles);
        }
    }

    
    public void showSelectionModeButton(boolean show)
    {
        if (show)
        {
            this.add(bSelectionMode);
        } else
        {
            this.remove(bSelectionMode);
        }
    }
}
