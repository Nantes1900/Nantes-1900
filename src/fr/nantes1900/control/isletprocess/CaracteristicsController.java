/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import fr.nantes1900.view.isletprocess.CaracteristicsView;

/**
 * @author Camille
 */
// FIXME : Javadoc
public class CaracteristicsController
{
    private IsletProcessController parentController;
    private CaracteristicsView     cView;

    public CaracteristicsController(IsletProcessController parentController)
    {
        this.parentController = parentController;
        this.cView = new CaracteristicsView();
    }

    public CaracteristicsView getView()
    {
        return cView;
    }

}
