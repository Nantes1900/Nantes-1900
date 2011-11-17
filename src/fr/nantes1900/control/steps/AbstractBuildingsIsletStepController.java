package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.view.display3d.Universe3DView;

/**
 * Implements the abstraction of a step of the treatment of a building islet.
 * @author Daniel
 */
public abstract class AbstractBuildingsIsletStepController
{

    /**
     * The parent controller : a buildings islet controller.
     */
    private BuildingsIsletController parentController;

    /**
     * Abstract method which display the content of the islet, depending on the
     * step.
     */
    public abstract void viewStep();

    /**
     * Constructor.
     * @param parentControllerIn
     *            the parent controller
     */
    public AbstractBuildingsIsletStepController(
            final BuildingsIsletController parentControllerIn)
    {
        this.parentController = parentControllerIn;
    }

    /**
     * Getter.
     * @return the parent : buildings islet controller.
     */
    public final BuildingsIsletController getParentController()
    {
        return this.parentController;
    }

    /**
     * Getter.
     * @return the Universe3D from the parent : buildings islet controller.
     */
    public final Universe3DView getUniverse3DView()
    {
        return this.parentController.getU3DController().getUniverse3DView();
    }
}
