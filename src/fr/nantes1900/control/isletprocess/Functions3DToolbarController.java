/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.Functions3DToolbarView;

/**
 * @author Camille
 */
public class Functions3DToolbarController
{
    private IsletProcessController parentController;
    private Functions3DToolbarView toolbarView;
    
    public Functions3DToolbarController(IsletProcessController parentController)
    {
        this.toolbarView = new Functions3DToolbarView();
        this.parentController = parentController;
        
        toolbarView.getRotationCenterButton().addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                // TODO call 3D universe controller to modify rotation center
            }
        });
        
        toolbarView.getLockButton().addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                JButton source = ((JButton) arg0.getSource());
                if (source.getName().equals("lock"))
                {
                    Functions3DToolbarController.this.parentController.lock(true);
                    source.setName("unlock");
                    source.setText("/p");
                    source.setToolTipText(FileTools.readElementText(TextsKeys.KEY_UNLOCKMESH));
                } else if (source.getName().equals("unlock"))
                {
                    Functions3DToolbarController.this.parentController.lock(false);
                    source.setName("lock");
                    source.setText("p");
                    source.setToolTipText(FileTools.readElementText(TextsKeys.KEY_LOCKMESH));
                }
            }
        });
        
        toolbarView.getDisplayTypeButton().addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                JButton source = ((JButton) arg0.getSource());
                if (source.getName().equals("meshes"))
                {
                    source.setName("triangles");
                    source.setText("#");
                    source.setToolTipText(FileTools.readElementText(TextsKeys.KEY_DISPLAYPOLYGONS));
                } else if (source.getName().equals("triangles"))
                {
                    source.setName("meshes");
                    source.setText("$");
                    source.setToolTipText(FileTools.readElementText(TextsKeys.KEY_DISPLAYMESHES));
                }
                // TODO call 3D universe controller to show meshes or polygons
            }
        });
        
        toolbarView.getDeselectMeshesButton().addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                // TODO call 3D universe controller to deselect all selected meshes
            }
        });

        toolbarView.getDeselectTrianglesButton().addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                // TODO call 3D universe controller to deselect all selected triangles
            }
        });
    }
    
    public Functions3DToolbarView getToolbar()
    {
        return toolbarView;
    }
}
