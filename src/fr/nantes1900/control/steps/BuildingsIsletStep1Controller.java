package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep1;
import fr.nantes1900.view.display3d.MeshView;

/**
 * Implements the controller of the 1th step of the treatment.
 * @author Daniel
 */
public class BuildingsIsletStep1Controller extends
        AbstractBuildingsIsletStepController
{

    /**
     * The 1th step of the treatment.
     */
    private BuildingsIsletStep1 biStep1;

    /**
     * Constructor.
     * @param biStepIn
     *            the 6th step of the treatment
     * @param parentControllerIn
     *            the parent controller
     */
    public BuildingsIsletStep1Controller(final BuildingsIsletStep1 biStepIn,
            final BuildingsIsletController parentControllerIn)
    {
        super(parentControllerIn);
        this.biStep1 = biStepIn;
    }

    @Override
    public final void viewStep()
    {
        this.getParentController()
                .getU3DController()
                .getUniverse3DView()
                .addMesh(
                        new MeshView(this.biStep1
                                .getInitialTotalMeshAfterBaseChange()));
    }
}
