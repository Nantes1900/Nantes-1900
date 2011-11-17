package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep6;
import fr.nantes1900.view.display3d.MeshView;

public class BuildingsIsletStep6Controller extends
        AbstractBuildingsIsletStepController
{

    public BuildingsIsletStep6Controller(final BuildingsIsletStep6 biStepIn,
            final BuildingsIsletController parentControllerIn)
    {
        super(parentControllerIn);
        this.biStep6 = biStepIn;
    }

    private BuildingsIsletStep6 biStep6;

    @Override
    public void viewStep()
    {
        for (Building building : this.biStep6.getBuildings())
        {
            for (Surface wall : building.getbStep6().getWalls())
            {
                this.getUniverse3DView().addMesh(new MeshView(wall.getMesh()));
            }
            for (Surface roof : building.getbStep6().getRoofs())
            {
                this.getUniverse3DView().addMesh(new MeshView(roof.getMesh()));
            }
        }
    }
}
