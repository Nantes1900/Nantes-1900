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

/**
 * Implements the controller of a building islet. Used to visualize the islets,
 * to launch the treatments.
 * @author Daniel
 */
public class BuildingsIsletController
{

    /**
     * The buildings islet containing the model.
     */
    private AbstractBuildingsIslet islet;
    /**
     * The islet selection controller, which is the parent of this.
     */
    private IsletSelectionController parentController;
    /**
     * The universe 3D controller to interact with the universe 3D.
     */
    private Universe3DController u3DController;
    /**
     * The normal of the gravity.
     */
    private Vector3d gravityNormal;

    /**
     * The controller related to the first step of the treatment.
     */
    private BuildingsIsletStep1Controller biStep1Controller;

    /**
     * The controller related to the second step of the treatment.
     */
    private BuildingsIsletStep2Controller biStep2Controller;

    /**
     * The controller related to the third step of the treatment.
     */
    private BuildingsIsletStep3Controller biStep3Controller;

    /**
     * The controller related to the 4th step of the treatment.
     */
    private BuildingsIsletStep4Controller biStep4Controller;

    /**
     * The controller related to the 5th step of the treatment.
     */
    private BuildingsIsletStep5Controller biStep5Controller;

    /**
     * The controller related to the 6th step of the treatment.
     */
    private BuildingsIsletStep6Controller biStep6Controller;

    /**
     * The controller related to the 7th step of the treatment.
     */
    private BuildingsIsletStep7Controller biStep7Controller;

    /**
     * The controller related to the 8th step of the treatment.
     */
    private BuildingsIsletStep7Controller biStep8Controller;

    /**
     * Constructor.
     * @param isletSelectionController
     *            the controller of the islet selection
     * @param universe3DControllerIn
     *            the universe 3D controller
     */
    public BuildingsIsletController(
            final IsletSelectionController isletSelectionController,
            final Universe3DController universe3DControllerIn)
    {
        this.parentController = isletSelectionController;
        this.u3DController = universe3DControllerIn;
    }

    /**
     * TODO.
     * @return TODO.
     */
    public final Vector3d computeNormalWithTrianglesSelected()
    {
        Mesh mesh = new Mesh(this.u3DController.getTrianglesSelected());
        return mesh.averageNormal();
    }

    /**
     * TODO.
     */
    public final void display()
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
        default:
            // TODO : error
            break;
        }
    }

    /**
     * Getter.
     * @return the gravity normal
     */
    public final Vector3d getGravityNormal()
    {
        return this.gravityNormal;
    }

    /**
     * Getter.
     * @return the buildings islet
     */
    public final AbstractBuildingsIslet getIslet()
    {
        return this.islet;
    }

    /**
     * Getter.
     * @return the controller of the islet selection
     */
    public final IsletSelectionController getIsletSelectionController()
    {
        return this.parentController;
    }

    /**
     * Getter.
     * @return the parent controller
     */
    public final IsletSelectionController getParentController()
    {
        return this.parentController;
    }

    /**
     * Getter.
     * @return the universe 3D controller
     */
    public final Universe3DController getU3DController()
    {
        return this.u3DController;
    }

    /**
     * Progression incrementation.
     */
    private void incProgression()
    {
        this.islet.incProgression();
    }

    /**
     * Launch the next treatment. Calls the launchTreatment method from the
     * BuildingsIslet.
     */
    public final void launchNextTreatment()
    {
        this.islet.launchTreatment();

        this.incProgression();
        this.display();
    }

    /**
     * TODO.
     */
    public final void putGravityNormal()
    {
        this.islet.setGravityNormal(this.gravityNormal);
    }

    /**
     * TODO.
     * @param fileName
     *            TODO.
     * @throws IOException
     *             TODO.
     */
    public final void readGravityNormal(final String fileName)
            throws IOException
    {
        ParserSTL parser = new ParserSTL(fileName);
        Mesh mesh = parser.read();
        this.setGravityNormal(mesh.averageNormal());
    }

    /**
     * Setter.
     * @param gravityNormalIn
     *            the gravity normal
     */
    public final void setGravityNormal(final Vector3d gravityNormalIn)
    {
        this.gravityNormal = gravityNormalIn;
    }

    /**
     * Setter.
     * @param groundNormal
     *            the ground normal
     */
    public final void setGroundNormal(final Vector3d groundNormal)
    {
        this.islet.setGroundNormal(groundNormal);
    }

    /**
     * Setter.
     * @param isletIn
     *            the new buildings islet
     */
    public final void setIslet(final AbstractBuildingsIslet isletIn)
    {
        this.islet = isletIn;
    }

    /**
     * Setter.
     * @param isletSelectionControllerIn
     *            the controller of the islet selection
     */
    public final void setIsletSelectionController(
            final IsletSelectionController isletSelectionControllerIn)
    {
        this.parentController = isletSelectionControllerIn;
    }

    /**
     * Setter.
     * @param parentControllerIn
     *            the parent controller
     */
    public final void setParentController(
            final IsletSelectionController parentControllerIn)
    {
        this.parentController = parentControllerIn;
    }

    /**
     * Setter.
     * @param u3dcontrollerIn
     *            the universe 3D controller
     */
    public final void setUniverse3DController(
            final Universe3DController u3dcontrollerIn)
    {
        this.u3DController = u3dcontrollerIn;
    }

    /**
     * TODO.
     */
    public final void useGravityNormalAsGroundNormal()
    {
        this.islet.setGroundNormal(this.islet.getGravityNormal());
    }
}
