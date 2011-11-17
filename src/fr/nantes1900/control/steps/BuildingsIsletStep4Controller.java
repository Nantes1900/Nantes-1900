package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep4;
import fr.nantes1900.view.display3d.MeshView;

/**
 * Implements the controller of the 4th step of the treatment.
 * @author Daniel
 */
public class BuildingsIsletStep4Controller extends
        AbstractBuildingsIsletStepController
{

    /**
     * The 4th step of the treatment.
     */
    private BuildingsIsletStep4 biStep4;

    /**
     * Constructor.
     * @param biStepIn
     *            the 6th step of the treatment
     * @param parentControllerIn
     *            the parent controller
     */
    public BuildingsIsletStep4Controller(final BuildingsIsletStep4 biStepIn,
            final BuildingsIsletController parentControllerIn)
    {
        super(parentControllerIn);
        this.biStep4 = biStepIn;
    }

    @Override
    public final void viewStep()
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
