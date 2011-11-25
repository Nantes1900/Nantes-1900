/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import fr.nantes1900.view.isletprocess.CharacteristicsView;

/**
 * @author Camille
 */
// FIXME : Javadoc
public class CharacteristicsController
{
    protected IsletProcessController parentController;
    protected CharacteristicsView     cView;

    public CharacteristicsController(IsletProcessController parentController)
    {
        this.parentController = parentController;
        cView = new CharacteristicsView();
    }

    public CharacteristicsView getView()
    {
        return cView;
    }
}
