/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import fr.nantes1900.constants.SeparationGroundBuilding;
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
        this.setParent(parentController);
        this.pView = new ParametersView();
    }

    public ParametersView getView()
    {
        return pView;
    }

    public IsletProcessController getParent()
    {
        return parentController;
    }

    public void setParent(IsletProcessController parentController)
    {
        this.parentController = parentController;
    }
    
    public void loadNewParameters(){
        SeparationGroundBuilding.setAltitureError(this.pView.getValueProperty1());
        SeparationGroundBuilding.setAngleGroundError(this.pView.getValueProperty2());
        SeparationGroundBuilding.setLargeAngleGroundError(this.pView.getValueProperty3());
    }
}
