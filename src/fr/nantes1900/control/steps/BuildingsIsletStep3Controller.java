package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep3;
import fr.nantes1900.view.display3d.MeshView;

/**
 * Implements the controller of the 3th step of the treatment.
 * @author Daniel
 */
public class BuildingsIsletStep3Controller extends
        AbstractBuildingsIsletStepController
{

    /**
     * The 3th step of the treatment.
     */
    private BuildingsIsletStep3 biStep3;

    /**
     * Constructor.
     * @param biStepIn
     *            the 6th step of the treatment
     * @param parentControllerIn
     *            the parent controller
     */
    public BuildingsIsletStep3Controller(final BuildingsIsletStep3 biStepIn,
            final BuildingsIsletController parentControllerIn)
    {
        super(parentControllerIn);
        this.biStep3 = biStepIn;
    }

    @Override
    public final void viewStep()
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
