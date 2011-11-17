package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.islets.buildings.steps.AbstractBuildingsIsletStep;
import fr.nantes1900.view.display3d.Universe3DView;

public abstract class AbstractBuildingsIsletStepController
{

    private BuildingsIsletController parentController;

    public abstract void viewStep();

    public AbstractBuildingsIsletStepController(
            BuildingsIsletController parentControllerIn)
    {
        this.parentController = parentControllerIn;
    }

    public BuildingsIsletController getParentController()
    {
        return this.parentController;
    }

    public Universe3DView getUniverse3DView()
    {
        return this.parentController.getU3DController().getUniverse3DView();
    }
}
