package fr.nantes1900.control.isletprocess;

import fr.nantes1900.view.isletprocess.characteristics.CharacteristicsView;

/**
 * Controller of basic characteristic controller.
 * @author Camille Bouquet, Luc Jallerat
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
     * @param parentControllerIn
     *            the parent controller
     */
    public CharacteristicsController(
            final IsletProcessController parentControllerIn) {
        this.parentController = parentControllerIn;
        this.cView = new CharacteristicsView();
    }

    /**
     * Gets the characteristic view associated with this panel.
     * @return the characteristic view
     */
    public final CharacteristicsView getView() {
        return this.cView;
    }
}
