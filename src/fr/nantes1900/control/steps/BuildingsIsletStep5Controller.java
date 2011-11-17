package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep5;
import fr.nantes1900.view.display3d.MeshView;

/**
 * Implements the controller of the 5th step of the treatment.
 * @author Daniel
 */
public class BuildingsIsletStep5Controller extends
        AbstractBuildingsIsletStepController
{

    /**
     * The 5th step of the treatment.
     */
    private BuildingsIsletStep5 biStep5;

    /**
     * Constructor.
     * @param biStepIn
     *            the 6th step of the treatment
     * @param parentControllerIn
     *            the parent controller
     */
    public BuildingsIsletStep5Controller(final BuildingsIsletStep5 biStepIn,
            final BuildingsIsletController parentControllerIn)
    {
        super(parentControllerIn);
        // TODO Auto-generated constructor stub
    }

    @Override
    public final void viewStep()
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
