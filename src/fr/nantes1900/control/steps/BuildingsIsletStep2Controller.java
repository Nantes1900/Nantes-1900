package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep2;
import fr.nantes1900.view.display3d.MeshView;

public class BuildingsIsletStep2Controller extends
        AbstractBuildingsIsletStepController
{

    public BuildingsIsletStep2Controller(BuildingsIsletStep2 biStepIn,
            BuildingsIsletController parentControllerIn)
    {
        super(parentControllerIn);
        this.biStep2 = biStepIn;
    }

    private BuildingsIsletStep2 biStep2;

    @Override
    public final void viewStep()
    {
        this.getUniverse3DView().addMesh(
                new MeshView(this.biStep2.getInitialBuildings()));
        this.getUniverse3DView().addMesh(
                new MeshView(this.biStep2.getInitialGrounds()));
    }

}
