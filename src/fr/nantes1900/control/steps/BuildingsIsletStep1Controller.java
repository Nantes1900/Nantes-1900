package fr.nantes1900.control.steps;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.models.islets.buildings.steps.AbstractBuildingsIsletStep;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep1;
import fr.nantes1900.view.display3d.MeshView;

public class BuildingsIsletStep1Controller extends
        AbstractBuildingsIsletStepController
{

    public BuildingsIsletStep1Controller(BuildingsIsletStep1 biStepIn,
            BuildingsIsletController parentControllerIn)
    {
        super(parentControllerIn);
        this.biStep1 = biStepIn;
    }

    private BuildingsIsletStep1 biStep1;

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
