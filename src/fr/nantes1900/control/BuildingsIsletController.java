package fr.nantes1900.control;

import java.io.IOException;

import javax.vecmath.Vector3d;

import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.control.isletselection.IsletSelectionController;
import fr.nantes1900.control.steps.BuildingsIsletStep1Controller;
import fr.nantes1900.control.steps.BuildingsIsletStep2Controller;
import fr.nantes1900.control.steps.BuildingsIsletStep3Controller;
import fr.nantes1900.control.steps.BuildingsIsletStep4Controller;
import fr.nantes1900.control.steps.BuildingsIsletStep5Controller;
import fr.nantes1900.control.steps.BuildingsIsletStep6Controller;
import fr.nantes1900.control.steps.BuildingsIsletStep7Controller;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.utils.ParserSTL;

public class BuildingsIsletController
{

    private AbstractBuildingsIslet islet;
    private IsletSelectionController parentController;
    private Universe3DController u3DController;
    private Vector3d gravityNormal;

    private BuildingsIsletStep1Controller biStep1Controller;
    private BuildingsIsletStep2Controller biStep2Controller;
    private BuildingsIsletStep3Controller biStep3Controller;
    private BuildingsIsletStep4Controller biStep4Controller;
    private BuildingsIsletStep5Controller biStep5Controller;
    private BuildingsIsletStep6Controller biStep6Controller;
    private BuildingsIsletStep7Controller biStep7Controller;
    private BuildingsIsletStep7Controller biStep8Controller;

    public BuildingsIsletController(
            final IsletSelectionController isletSelectionController,
            final Universe3DController universe3DControllerIn)
    {
        this.parentController = isletSelectionController;
        this.u3DController = universe3DControllerIn;
    }

    public final Vector3d computeNormalWithTrianglesSelected()
    {
        Mesh mesh = new Mesh(this.u3DController.getTrianglesSelected());
        return mesh.averageNormal();
    }

    // FIXME
    public void display()
    {
        this.u3DController.getUniverse3DView().clearAllMeshes();

        switch (this.islet.getProgression()) {
        case 0:
            // TODO : error
            break;
        case 1:
            this.biStep1Controller.viewStep();
            break;
        case 2:
            this.biStep2Controller.viewStep();
            break;
        case 3:
            this.biStep3Controller.viewStep();
            break;
        case 4:
            this.biStep4Controller.viewStep();
            break;
        case 5:
            this.biStep5Controller.viewStep();
            break;
        case 6:
            this.biStep6Controller.viewStep();
            break;
        case 7:
            this.biStep7Controller.viewStep();
            break;
        case 8:
            this.biStep8Controller.viewStep();
            break;
        }
    }

    public Vector3d getGravityNormal()
    {
        return this.gravityNormal;
    }

    public final AbstractBuildingsIslet getIslet()
    {
        return this.islet;
    }

    public final IsletSelectionController getIsletSelectionController()
    {
        return this.parentController;
    }

    public final IsletSelectionController getParentController()
    {
        return this.parentController;
    }

    public final Universe3DController getU3DController()
    {
        return this.u3DController;
    }

    private final void incProgression()
    {
        this.islet.incProgression();
    }

    public final void launchNextTreatment()
    {
        this.islet.launchTreatment();

        this.incProgression();
        this.display();
    }

    public final void putGravityNormal()
    {
        this.islet.setGravityNormal(this.gravityNormal);
    }

    public final void readGravityNormal(final String fileName)
            throws IOException
    {
        ParserSTL parser = new ParserSTL(fileName);
        Mesh mesh = parser.read();
        this.setGravityNormal(mesh.averageNormal());
    }

    public final void setGravityNormal(final Vector3d gravityNormalIn)
    {
        this.gravityNormal = gravityNormalIn;
    }

    public final void setGroundNormal(final Vector3d groundNormal)
    {
        this.islet.setGroundNormal(groundNormal);
    }

    public final void setIslet(final AbstractBuildingsIslet isletIn)
    {
        this.islet = isletIn;
    }

    public final void setIsletSelectionController(
            final IsletSelectionController isletSelectionControllerIn)
    {
        this.parentController = isletSelectionControllerIn;
    }

    public final void setParentController(
            final IsletSelectionController parentControllerIn)
    {
        this.parentController = parentControllerIn;
    }

    public final void setUniverse3DController(
            Universe3DController u3dcontrollerIn)
    {
        this.u3DController = u3dcontrollerIn;
    }

    public final void useGravityNormalAsGroundNormal()
    {
        this.islet.setGroundNormal(this.islet.getGravityNormal());
    }

}
