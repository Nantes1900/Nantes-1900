/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import fr.nantes1900.view.isletprocess.CharacteristicsView;

/**
 * Controller of basic characteristic controller;
 * @author Camille Bouquet
 * @author Luc Jallerat
 */
public class CharacteristicsController {
    /**
     * Parent controller of this one.
     */
    protected IsletProcessController parentController;
    /**
     * The characteristics panel.
     */
    protected CharacteristicsView cView;

    /**
     * Creates a new basic characteristic controller which will create the view.
     * @param parentController
     *            the parent controller
     */
    public CharacteristicsController(IsletProcessController parentController) {
        this.parentController = parentController;
        cView = new CharacteristicsView();
    }

    /**
     * Gets the characteristic view associated with this panel.
     * @return
     *         the characteristic view
     */
    public CharacteristicsView getView() {
        return cView;
    }
}
