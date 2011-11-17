package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep5;
import fr.nantes1900.view.display3d.MeshView;

public class BuildingsIsletStep5Controller extends
        AbstractBuildingsIsletStepController
{

    public BuildingsIsletStep5Controller(BuildingsIsletStep5 biStepIn,
            BuildingsIsletController parentControllerIn)
    {
        super(parentControllerIn);
        // TODO Auto-generated constructor stub
    }

    private BuildingsIsletStep5 biStep5;

    public void viewStep()
    {
        for (Building building : this.biStep5.getBuildings())
        {
            for (Surface wall : building.getbStep5().getWalls())
            {
                this.getUniverse3DView().addMesh(new MeshView(wall.getMesh()));
            }
            for (Surface roof : building.getbStep5().getRoofs())
            {
                this.getUniverse3DView().addMesh(new MeshView(roof.getMesh()));
            }
        }
    }
}
