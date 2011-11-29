/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import fr.nantes1900.constants.TextsKeys;
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
        bDisplayType.setName("meshes");
        bLockButton= new JButton("p");
        bLockButton.setToolTipText(FileTools.readElementText(TextsKeys.KEY_LOCKMESH));
        bLockButton.setName("lock");
        bDeselectMeshes = new JButton("cm");
        bDeselectMeshes.setToolTipText(FileTools.readElementText(TextsKeys.KEY_DESELECTMESHES));
        bDeselectTriangles = new JButton("ct");
        bDeselectTriangles.setToolTipText(FileTools.readElementText(TextsKeys.KEY_DESELECTTRIANGLES));
        
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
}
