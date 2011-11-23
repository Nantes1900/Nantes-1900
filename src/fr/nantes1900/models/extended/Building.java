package fr.nantes1900.models.extended;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.extended.steps.AbstractBuildingStep;
import fr.nantes1900.models.extended.steps.BuildingStep3;
import fr.nantes1900.models.extended.steps.BuildingStep4;
import fr.nantes1900.models.extended.steps.BuildingStep5;
import fr.nantes1900.models.extended.steps.BuildingStep6;
import fr.nantes1900.models.extended.steps.BuildingStep7;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.models.islets.buildings.exceptions.NullArgumentException;

/**
 * Implements a building as containing 6 steps representing the state of the
 * building during the treatments 3 to 8.
 * @author Daniel Lefevre
 */
public class Building
{

    /**
     * The building step.
     */
    private AbstractBuildingStep   buildingStep;
    /**
     * The parent islet.
     */
    private AbstractBuildingsIslet parentIslet;
    /**
     * The normal to the ground.
     */
    private Vector3d               groundNormal;
    /**
     * The gravity normal.
     */
    private Vector3d               gravityNormal;
    /**
     * The noise.
     */
    private Mesh                   noise;
    /**
     * The grounds.
     */
    private Ground                 grounds;

    /**
     * Constructor.
     * @param mesh
     *            the mesh representing the building
     */
    public Building(final Mesh mesh)
    {
        this.buildingStep = new BuildingStep3(mesh);
    }

    /**
     * Getter.
     * @return the current step
     */
    public final AbstractBuildingStep getbStep()
    {
        return this.buildingStep;
    }

    /**
     * Launches the treatment corresponding to the progression.
     */
    public final void launchTreatment()
    {
        try
        {
            switch (this.parentIslet.getProgression())
            {
                case AbstractBuildingsIslet.ZERO_STEP:
                    throw new InvalidCaseException();
                case AbstractBuildingsIslet.FIRST_STEP:
                    throw new InvalidCaseException();
                case AbstractBuildingsIslet.SECOND_STEP:
                    throw new InvalidCaseException();
                case AbstractBuildingsIslet.THIRD_STEP:
                    BuildingStep3 step3 = (BuildingStep3) this.buildingStep;
                    step3.setArguments(this.gravityNormal);
                    this.buildingStep = this.buildingStep.launchTreatment();
                break;
                case AbstractBuildingsIslet.FOURTH_STEP:
                    BuildingStep4 step4 = (BuildingStep4) this.buildingStep;
                    step4.setArguments(this.groundNormal, this.grounds);
                    this.buildingStep = this.buildingStep.launchTreatment();
                break;
                case AbstractBuildingsIslet.FIFTH_STEP:
                    BuildingStep5 step5 = (BuildingStep5) this.buildingStep;
                    step5.setArguments(this.noise, this.grounds);
                    this.buildingStep = this.buildingStep.launchTreatment();
                break;
                case AbstractBuildingsIslet.SIXTH_STEP:
                    BuildingStep6 step6 = (BuildingStep6) this.buildingStep;
                    step6.setArguments(this.grounds);
                    this.buildingStep = this.buildingStep.launchTreatment();
                break;
                case AbstractBuildingsIslet.SEVENTH_STEP:
                    BuildingStep7 step7 = (BuildingStep7) this.buildingStep;
                    step7.setArguments(this.groundNormal);
                    this.buildingStep = this.buildingStep.launchTreatment();
                break;
                default:
                    throw new InvalidCaseException();
            }
        } catch (InvalidCaseException e)
        {
            System.out.println("Big problem");
        } catch (NullArgumentException e)
        {
            System.out.println("Big problem 2");
        }
    }

    /**
     * Creates a tree node for the JTree.
     * @return the mutable tree node
     */
    public final DefaultMutableTreeNode returnNode()
    {
        return this.buildingStep.returnNode();
    }

    /**
     * Setter.
     * @param groundNormalIn
     *            the normal to the ground
     * @param gravityNormalIn
     *            the gravity normal
     * @param groundsIn
     *            the grounds
     * @param noiseIn
     *            the noise
     */
    public final void setArguments(final Vector3d groundNormalIn,
            final Vector3d gravityNormalIn,
            final Ground groundsIn,
            final Mesh noiseIn)
    {
        this.groundNormal = groundNormalIn;
        this.gravityNormal = gravityNormalIn;
        this.grounds = groundsIn;
        this.noise = noiseIn;
    }
}
