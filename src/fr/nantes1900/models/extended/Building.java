package fr.nantes1900.models.extended;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.extended.steps.BuildingStep3;
import fr.nantes1900.models.extended.steps.BuildingStep4;
import fr.nantes1900.models.extended.steps.BuildingStep5;
import fr.nantes1900.models.extended.steps.BuildingStep6;
import fr.nantes1900.models.extended.steps.BuildingStep7;
import fr.nantes1900.models.extended.steps.BuildingStep8;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.islets.buildings.InvalidCaseException;

/**
 * Implements a building as containing 6 steps representing the state of the
 * building during the treatments 3 to 8.
 * @author Daniel Lefevre
 */
public class Building
{

    /**
     * The third building step.
     */
    private BuildingStep3          bStep3;
    /**
     * The fourth building step.
     */
    private BuildingStep4          bStep4;
    /**
     * The fifth building step.
     */
    private BuildingStep5          bStep5;
    /**
     * The sixth building step.
     */
    private BuildingStep6          bStep6;
    /**
     * The seventh building step.
     */
    private BuildingStep7          bStep7;
    /**
     * The eighth building step.
     */
    private BuildingStep8          bStep8;

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
        this.bStep3 = new BuildingStep3(mesh);
    }

    /**
     * Getter.
     * @return the third step
     */
    public final BuildingStep3 getbStep3()
    {
        return this.bStep3;
    }

    /**
     * Getter.
     * @return the fourth step
     */
    public final BuildingStep4 getbStep4()
    {
        return this.bStep4;
    }

    /**
     * Getter.
     * @return the fifth step
     */
    public final BuildingStep5 getbStep5()
    {
        return this.bStep5;
    }

    /**
     * Getter.
     * @return the sixth step
     */
    public final BuildingStep6 getbStep6()
    {
        return this.bStep6;
    }

    /**
     * Getter.
     * @return the seventh step
     */
    public final BuildingStep7 getbStep7()
    {
        return this.bStep7;
    }

    /**
     * Getter.
     * @return the eighth step
     */
    public final BuildingStep8 getbStep8()
    {
        return this.bStep8;
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
                    this.bStep4 = this.bStep3.launchTreatment();
                break;
                case AbstractBuildingsIslet.FOURTH_STEP:
                    this.bStep5 = this.bStep4.launchTreatment();
                break;
                case AbstractBuildingsIslet.FIFTH_STEP:
                    this.bStep6 = this.bStep5.launchTreatment();
                break;
                case AbstractBuildingsIslet.SIXTH_STEP:
                    this.bStep7 = this.bStep6.launchTreatment();
                break;
                case AbstractBuildingsIslet.SEVENTH_STEP:
                    this.bStep8 = this.bStep7.launchTreatment();
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
                    return this.bStep3.returnNode();
                case AbstractBuildingsIslet.FOURTH_STEP:
                    return this.bStep4.returnNode();
                case AbstractBuildingsIslet.FIFTH_STEP:
                    return this.bStep5.returnNode();
                case AbstractBuildingsIslet.SIXTH_STEP:
                    return this.bStep6.returnNode();
                case AbstractBuildingsIslet.SEVENTH_STEP:
                    return this.bStep7.returnNode();
                case AbstractBuildingsIslet.EIGHTH_STEP:
                    return this.bStep8.returnNode();
                default:
                    throw new InvalidCaseException();
            }
        } catch (InvalidCaseException e)
        {
            System.out.println("Big problem");
            return null;
        }
    }
}
