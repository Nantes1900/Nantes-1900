/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import fr.nantes1900.view.isletprocess.ParametersView;

/**
 * @author Camille
 */
// FIXME : Javadoc
public class ParametersController
{
    private ParametersView         pView;
    private IsletProcessController parentController;

    public ParametersController(IsletProcessController parentController)
    {
        this.parentController = parentController;
        this.pView = new ParametersView();
    }

    public ParametersView getView()
    {
        return pView;
    }

}
