package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep8;
import fr.nantes1900.view.display3d.PolygonView;

public class BuildingsIsletStep8Controller extends
        AbstractBuildingsIsletStepController
{

    public BuildingsIsletStep8Controller(BuildingsIsletStep8 biStepIn,
            BuildingsIsletController parentControllerIn)
    {
        super(parentControllerIn);
        this.biStep8 = biStepIn;
    }

    private BuildingsIsletStep8 biStep8;

    @Override
    public void viewStep()
    {
        for (Building building : this.biStep8.getBuildings())
        {
            for (Surface wall : building.getbStep8().getWalls())
            {
                this.getUniverse3DView().addPolygonView(
                        new PolygonView(wall.getPolygone()));
            }
            for (Surface roof : building.getbStep8().getRoofs())
            {
                this.getUniverse3DView().addPolygonView(
                        new PolygonView(roof.getPolygone()));
            }
        }
    }

}
