package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep3;
import fr.nantes1900.view.display3d.MeshView;

public class BuildingsIsletStep3Controller extends
        AbstractBuildingsIsletStepController
{

    public BuildingsIsletStep3Controller(BuildingsIsletStep3 biStepIn,
            BuildingsIsletController parentControllerIn)
    {
        super(parentControllerIn);
        this.biStep3 = biStepIn;
    }

    private BuildingsIsletStep3 biStep3;

    @Override
    public void viewStep()
    {
        this.getUniverse3DView().addMesh(
                new MeshView(this.biStep3.getGrounds()));

        for (Building building : this.biStep3.getBuildings())
        {
            this.getUniverse3DView().addMesh(
                    new MeshView(building.getbStep3().getInitialTotalMesh()));
        }
    }
}
