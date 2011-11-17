package fr.nantes1900.control.steps;

import java.util.List;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Triangle;
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

    public final void action(Mesh mesh, int actionType)
    {
        if (actionType == ActionTypes.TURN_TO_NOISE)
        {
            this.biStep3.getBuildings().remove(
                    this.returnBuildingContaining(mesh));
            this.biStep3.getNoise().addAll(mesh);
        } else if (actionType == ActionTypes.TURN_TO_BUILDING)
        {
            this.biStep3.getBuildings().add(new Building(mesh));
            this.biStep3.getNoise().removeAll(mesh);
        } else
        {
            // LOOK : error.
        }
    }

    public Building returnBuildingContaining(Mesh mesh)
    {
        for (Building building : this.biStep3.getBuildings())
        {
            if (building.getbStep3().getInitialTotalMesh() == mesh)
            {
                return null;
            }
        }
        return null;
    }

    public void action(List<Triangle> trianglesSelected, int actionType)
    {
        if (actionType == ActionTypes.REMOVE)
        {
            for (Building building : this.biStep3.getBuildings())
            {
                building.getbStep3().getInitialTotalMesh()
                        .removeAll(trianglesSelected);
            }
            this.biStep3.getGrounds().removeAll(trianglesSelected);
        } else if (actionType == ActionTypes.TURN_TO_BUILDING)
        {
            this.biStep3.getBuildings().add(
                    new Building(new Mesh(trianglesSelected)));
            this.biStep3.getNoise().removeAll(trianglesSelected);
        } else
        {
            // LOOK : error.
        }
    }

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
