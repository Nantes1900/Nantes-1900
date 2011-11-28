/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import javax.swing.JToolBar;

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
    }
    
    public JToolBar getToolbar()
    {
        return toolbarView;
    }
}
