package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep2;
import fr.nantes1900.view.display3d.MeshView;

/**
 * Implements the controller of the 2th step of the treatment.
 * @author Daniel
 */
public class BuildingsIsletStep2Controller extends
        AbstractBuildingsIsletStepController
{

    /**
     * Constructor.
     * @param biStepIn
     *            the 6th step of the treatment
     * @param parentControllerIn
     *            the parent controller
     */
    public BuildingsIsletStep2Controller(final BuildingsIsletStep2 biStepIn,
            final BuildingsIsletController parentControllerIn)
    {
        super(parentControllerIn);
        this.biStep2 = biStepIn;
    }

    /**
     * The 2th step of the treatment.
     */
    private BuildingsIsletStep2 biStep2;

    @Override
    public final void viewStep()
    {
        this.getUniverse3DView().addMesh(
                new MeshView(this.biStep2.getInitialBuildings()));
        this.getUniverse3DView().addMesh(
                new MeshView(this.biStep2.getInitialGrounds()));
    }

}
