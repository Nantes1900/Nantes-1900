package fr.nantes1900.models.islets.buildings;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.islets.AbstractIslet;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.models.islets.buildings.exceptions.NullArgumentException;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep0;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep1;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep2;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep3;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep4;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep5;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep6;
import fr.nantes1900.utils.MatrixMethod;

/**
 * Abstracts a building islet : residential or industrial. This class contains
 * all the methods to apply the treatments on the meshes.
 * @author Daniel Lefèvre
 */
public abstract class AbstractBuildingsIslet extends AbstractIslet
{
    /**
     * The zero building islet step.
     */
    private BuildingsIsletStep0 biStep0;
    /**
     * The first building islet step.
     */
    private BuildingsIsletStep1 biStep1;
    /**
     * The second building islet step.
     */
    private BuildingsIsletStep2 biStep2;
    /**
     * The third building islet step.
     */
    private BuildingsIsletStep3 biStep3;
    /**
     * The fourth building islet step.
     */
    private BuildingsIsletStep4 biStep4;
    /**
     * The fifth building islet step.
     */
    private BuildingsIsletStep5 biStep5;
    /**
     * The sixth building islet step.
     */
    private BuildingsIsletStep6 biStep6;

    /**
     * The number of the step 0.
     */
    public static final int     ZERO_STEP   = 0;
    /**
     * The number of the step 1.
     */
    public static final int     FIRST_STEP  = 1;
    /**
     * The number of the step 2.
     */
    public static final int     SECOND_STEP = 2;
    /**
     * The number of the step 3.
     */
    public static final int     THIRD_STEP  = 3;
    /**
     * The number of the step 4.
     */
    public static final int     FOURTH_STEP = 4;
    /**
     * The number of the step 5.
     */
    public static final int     FIFTH_STEP  = 5;
    /**
     * The number of the step 6.
     */
    public static final int     SIXTH_STEP  = 6;
    /**
     * The number of the current step.
     */
    private int                 progression = 0;

    /**
     * The normal to the ground. Used to extract the grounds.
     */
    private Vector3d            groundNormal;

    /**
     * The normal to the gravity.
     */
    private Vector3d            gravityNormal;

    /**
     * Constructor. Stocks the mesh in the initialTotalMesh variable.
     */
    public AbstractBuildingsIslet()
    {
    }

    public void decProgression()
    {
        this.progression--;
    }

    /**
     * Getter.
     * @return the zero step
     */
    public final BuildingsIsletStep0 getBiStep0()
    {
        return this.biStep0;
    }

    /**
     * Getter.
     * @return the first step
     */
    public final BuildingsIsletStep1 getBiStep1()
    {
        return this.biStep1;
    }

    /**
     * Getter.
     * @return the second step
     */
    public final BuildingsIsletStep2 getBiStep2()
    {
        return this.biStep2;
    }

    /**
     * Getter.
     * @return the third step
     */
    public final BuildingsIsletStep3 getBiStep3()
    {
        return this.biStep3;
    }

    /**
     * Getter.
     * @return the fourth step
     */
    public final BuildingsIsletStep4 getBiStep4()
    {
        return this.biStep4;
    }

    /**
     * Getter.
     * @return the fifth step
     */
    public final BuildingsIsletStep5 getBiStep5()
    {
        return this.biStep5;
    }

    /**
     * Getter.
     * @return the sixth step
     */
    public final BuildingsIsletStep6 getBiStep6()
    {
        return this.biStep6;
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
     * @return the normal to the ground
     */
    public final Vector3d getGroundNormal()
    {
        return this.groundNormal;
    }

    /**
     * Getter.
     * @return the progression of the treatment
     */
    public final int getProgression()
    {
        return this.progression;
    }

    /**
     * Incrementer. Used to increment the progression of the treamtent.
     */
    public final void incProgression()
    {
        this.progression++;
    }

    /**
     * Launches the first treatment.
     * @throws NullArgumentException
     *             if the gravity normal has not been initiliazed
     */
    public void launchTreatment0() throws NullArgumentException
    {
        if (this.getGravityNormal() == null)
        {
            throw new NullArgumentException();
        }
        // FIXME : remove the setArguments : allow each step to access to its
        // islet parent and get everything it wants.
        this.getBiStep0().setArguments(this.getGravityNormal());
        this.biStep1 = this.getBiStep0().launchTreatment();
        MatrixMethod.changeBase(this.getGroundNormal(), this.getBiStep0()
                .getMatrix());
    }

    /**
     * Launches the first treatment.
     * @throws NullArgumentException
     */
    public void launchTreatment1() throws NullArgumentException
    {
        this.getBiStep1().setArguments(this.getGravityNormal());
        this.biStep2 = this.getBiStep1().launchTreatment();
    }

    /**
     * Launches the second treatment.
     */
    public void launchTreatment2()
    {
        this.biStep3 = this.getBiStep2().launchTreatment();
    }

    /**
     * Launches the third treatment.
     * @throws NullArgumentException
     */
    public void launchTreatment3() throws NullArgumentException
    {
        System.out.println("Beginning tr 3");
        if (this.getGravityNormal() == null || this.getGroundNormal() == null
                || this.getBiStep2().getNoise() == null)
        {
            throw new NullArgumentException();
        }
        for (Building b : this.getBiStep3().getBuildings())
        {
            b.setArguments(this.getGroundNormal(), this.getGravityNormal(),
                    this.getBiStep2().getInitialGrounds(), this.getBiStep2()
                            .getNoise());
        }
        this.biStep4 = this.getBiStep3().launchTreatment();
        System.out.println("End tr 3");
    }

    /**
     * Launches the fourth treatment.
     * @throws NullArgumentException
     */
    public void launchTreatment4() throws NullArgumentException
    {
        System.out.println("Beginning tr 4");
        this.biStep5 = this.getBiStep4().launchTreatment();
        System.out.println("End tr 4");
    }

    /**
     * Launches the fifth treatment.
     * @throws NullArgumentException
     */
    public void launchTreatment5() throws NullArgumentException
    {
        System.out.println("Beginning tr 5");
        this.biStep6 = this.getBiStep5().launchTreatment();
        System.out.println("End tr 5");
    }

    /**
     * Return a node containing the tree depending of the progression of the
     * treatment.
     * @return the node
     * @throws InvalidCaseException
     *             if the case in not valid (more than 8 or less than 0)
     */
    public final DefaultMutableTreeNode returnNode()
            throws InvalidCaseException
    {
        switch (this.getProgression())
        {
            case AbstractBuildingsIslet.ZERO_STEP:
                throw new InvalidCaseException();
            case AbstractBuildingsIslet.FIRST_STEP:
                return this.biStep1.returnNode();
            case AbstractBuildingsIslet.SECOND_STEP:
                return this.biStep2.returnNode();
            case AbstractBuildingsIslet.THIRD_STEP:
                return this.biStep3.returnNode();
            case AbstractBuildingsIslet.FOURTH_STEP:
                return this.biStep4.returnNode();
            case AbstractBuildingsIslet.FIFTH_STEP:
                return this.biStep5.returnNode();
            case AbstractBuildingsIslet.SIXTH_STEP:
                return this.biStep6.returnNode();
            default:
                return null;
        }
    }

    public void setBiStep0(BuildingsIsletStep0 buildingsIsletStepIn)
    {
        this.biStep0 = buildingsIsletStepIn;
    }

    /**
     * Setter.
     * @param gravityNormalIn
     *            the new gravity normal
     */
    public final void setGravityNormal(final Vector3d gravityNormalIn)
    {
        this.gravityNormal = gravityNormalIn;
    }

    /**
     * Setter.
     * @param groundNormalIn
     *            the normal to set as ground normal
     */
    public final void setGroundNormal(final Vector3d groundNormalIn)
    {
        this.groundNormal = groundNormalIn;
    }

    /**
     * Setter.
     * @param progressionIn
     *            the progression
     */
    public final void setProgression(final int progressionIn)
    {
        this.progression = progressionIn;
    }
}
