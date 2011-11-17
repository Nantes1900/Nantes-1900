package fr.nantes1900.control.steps;

import java.util.List;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.basis.Triangle;
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

    public final void action(List<Triangle> trianglesSelected, int actionType)
    {
        Building building = this.searchForBuildingContaining(trianglesSelected);
        if (building != null)
        {
            if (actionType == ActionTypes.TURN_TO_WALL)
            {
                building.getbStep4().getInitialWall().addAll(trianglesSelected);
                building.getbStep4().getInitialRoof().remove(trianglesSelected);
            } else if (actionType == ActionTypes.TURN_TO_ROOF)
            {
                building.getbStep4().getInitialRoof().addAll(trianglesSelected);
                building.getbStep4().getInitialWall().remove(trianglesSelected);
            }
        } else
        {
            // TODO : error.
        }

    }

    private Building searchForBuildingContaining(
            List<Triangle> trianglesSelected)
    {
        for (Building building : this.biStep4.getBuildings())
        {
            if (building.getbStep4().getInitialWall()
                    .containsAll(trianglesSelected)
                    || building.getbStep4().getInitialRoof()
                            .containsAll(trianglesSelected))
            {
                return building;
            }
        }
        return null;
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
