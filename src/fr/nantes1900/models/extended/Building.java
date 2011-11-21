package fr.nantes1900.models.extended;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.extended.steps.AbstractBuildingStep;
import fr.nantes1900.models.extended.steps.BuildingStep3;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.islets.buildings.exception.InvalidCaseException;

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
        // TODO : simplify this...
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
                    this.buildingStep = this.buildingStep.launchTreatment();
                break;
                case AbstractBuildingsIslet.FOURTH_STEP:
                    this.buildingStep = this.buildingStep.launchTreatment();
                break;
                case AbstractBuildingsIslet.FIFTH_STEP:
                    this.buildingStep = this.buildingStep.launchTreatment();
                break;
                case AbstractBuildingsIslet.SIXTH_STEP:
                    this.buildingStep = this.buildingStep.launchTreatment();
                break;
                case AbstractBuildingsIslet.SEVENTH_STEP:
                    this.buildingStep = this.buildingStep.launchTreatment();
                break;
                default:
                    throw new InvalidCaseException();
            }
        } catch (InvalidCaseException e)
        {
            System.out.println("Big problem");
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
}
