/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import fr.nantes1900.view.isletprocess.NavigationBarView;

/**
 * @author Camille
 *
 */
public class NavigationBarController
{
    private NavigationBarView nbView;
    private IsletProcessController parentController;
    
    public NavigationBarController(IsletProcessController parentController)
    {
        this.parentController = parentController;
        this.nbView = new NavigationBarView();
    }

    public NavigationBarView getView()
    {
        return nbView;
    }

}
