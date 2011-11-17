package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep7;
import fr.nantes1900.view.display3d.MeshView;

public class BuildingsIsletStep7Controller extends
        AbstractBuildingsIsletStepController
{

    public BuildingsIsletStep7Controller(BuildingsIsletStep7 biStepIn,
            BuildingsIsletController parentControllerIn)
    {
        super(parentControllerIn);
        this.biStep7 = biStepIn;
    }

    private BuildingsIsletStep7 biStep7;

    @Override
    public final void viewStep()
    {
        for (Building building : this.biStep7.getBuildings())
        {
            for (Surface wall : building.getbStep7().getWalls())
            {
                this.getUniverse3DView().addMesh(new MeshView(wall.getMesh()));
            }
            for (Surface roof : building.getbStep7().getRoofs())
            {
                this.getUniverse3DView().addMesh(new MeshView(roof.getMesh()));
            }
        }
    }
}
