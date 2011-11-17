package fr.nantes1900.control.steps;

import java.util.List;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.basis.Triangle;
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
     * The 2th step of the treatment.
     */
    private BuildingsIsletStep2 biStep2;

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
     * Changes the type of a list of triangles.
     * @param trianglesSelected
     *            the list of triangles
     * @param type
     *            the new type of these triangles
     */
    public final void action(final List<Triangle> trianglesSelected,
            final int type)
    {
        if (type == ActionTypes.TURN_TO_BUILDING)
        {
            // The user wants these triangles to turn building.
            this.biStep2.getInitialGrounds().removeAll(trianglesSelected);
            this.biStep2.getInitialBuildings().addAll(trianglesSelected);
        } else if (type == ActionTypes.TURN_TO_GROUND)
        {
            // The user wants these triangles to turn ground.
            this.biStep2.getInitialBuildings().removeAll(trianglesSelected);
            this.biStep2.getInitialGrounds().addAll(trianglesSelected);
        }
    }

    @Override
    public final void viewStep()
    {
        this.getUniverse3DView().addMesh(
                new MeshView(this.biStep2.getInitialBuildings()));
        this.getUniverse3DView().addMesh(
                new MeshView(this.biStep2.getInitialGrounds()));

        // Also display the noise.
    }

}
