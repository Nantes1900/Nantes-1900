package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep4;
import fr.nantes1900.view.display3d.MeshView;

public class BuildingsIsletStep4Controller extends
        AbstractBuildingsIsletStepController
{

    public BuildingsIsletStep4Controller(BuildingsIsletStep4 biStepIn,
            BuildingsIsletController parentControllerIn)
    {
        super(parentControllerIn);
        this.biStep4 = biStepIn;
    }

    private BuildingsIsletStep4 biStep4;

    @Override
    public void viewStep()
    {
        for (Building building : this.biStep4.getBuildings())
        {
            this.getUniverse3DView().addMesh(
                    new MeshView(building.getbStep4().getInitialWall()));
            this.getUniverse3DView().addMesh(
                    new MeshView(building.getbStep4().getInitialRoof()));
        }
    }
}
