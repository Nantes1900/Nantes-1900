package fr.nantes1900.models.islets.buildings;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.islets.AbstractIslet;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep0;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep1;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep2;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep3;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep4;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep5;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep6;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep7;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep8;
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
     * The seventh building islet step.
     */
    private BuildingsIsletStep7 biStep7;
    /**
     * The eighth building islet step.
     */
    private BuildingsIsletStep8 biStep8;

    /**
     * The number of the step 0.
     */
    public static final int     ZERO_STEP    = 0;
    /**
     * The number of the step 1.
     */
    public static final int     FIRST_STEP   = 1;

    /**
     * The number of the step 2.
     */
    public static final int     SECOND_STEP  = 2;
    /**
     * The number of the step 3.
     */
    public static final int     THIRD_STEP   = 3;
    /**
     * The number of the step 4.
     */
    public static final int     FOURTH_STEP  = 4;
    /**
     * The number of the step 5.
     */
    public static final int     FIFTH_STEP   = 5;
    /**
     * The number of the step 6.
     */
    public static final int     SIXTH_STEP   = 6;
    /**
     * The number of the step 7.
     */
    public static final int     SEVENTH_STEP = 7;
    /**
     * The number of the step 8.
     */
    public static final int     EIGHTH_STEP  = 8;
    /**
     * The number of the current step.
     */
    private int                 progression  = 0;

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
     * @return the seventh step
     */
    public final BuildingsIsletStep7 getBiStep7()
    {
        return this.biStep7;
    }

    /**
     * Getter.
     * @return the eighth step
     */
    public final BuildingsIsletStep8 getBiStep8()
    {
        return this.biStep8;
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
     * Return a node containing the tree depending of the progression of the
     * treatment.
     * @return the node
     */
    public final DefaultMutableTreeNode returnNode()
    {
        switch (this.getProgression())
        {
            case AbstractBuildingsIslet.ZERO_STEP:
                // TODO : error
                return null;
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
            case AbstractBuildingsIslet.SEVENTH_STEP:
                return this.biStep7.returnNode();
            case AbstractBuildingsIslet.EIGHTH_STEP:
                return this.biStep8.returnNode();
            default:
                return null;
        }
    }

    /**
     * Setter.
     * @param biStepIn
     *            the zero step
     */
    public final void setBiStep0(final BuildingsIsletStep0 biStepIn)
    {
        this.biStep0 = biStepIn;
    }

    /**
     * Setter.
     * @param biStep1In
     *            the first step
     */
    public final void setBiStep1(final BuildingsIsletStep1 biStep1In)
    {
        this.biStep1 = biStep1In;
    }

    /**
     * Setter.
     * @param biStep2In
     *            the second step
     */
    public final void setBiStep2(final BuildingsIsletStep2 biStep2In)
    {
        this.biStep2 = biStep2In;
    }

    /**
     * Setter.
     * @param biStep3In
     *            the third step
     */
    public final void setBiStep3(final BuildingsIsletStep3 biStep3In)
    {
        this.biStep3 = biStep3In;
    }

    /**
     * Setter.
     * @param biStep4In
     *            the fourth step
     */
    public final void setBiStep4(final BuildingsIsletStep4 biStep4In)
    {
        this.biStep4 = biStep4In;
    }

    /**
     * Setter.
     * @param biStep5In
     *            the fifth step
     */
    public final void setBiStep5(final BuildingsIsletStep5 biStep5In)
    {
        this.biStep5 = biStep5In;
    }

    /**
     * Setter.
     * @param biStep6In
     *            the sixth step
     */
    public final void setBiStep6(final BuildingsIsletStep6 biStep6In)
    {
        this.biStep6 = biStep6In;
    }

    /**
     * Setter.
     * @param biStep7In
     *            the seventh step
     */
    public final void setBiStep7(final BuildingsIsletStep7 biStep7In)
    {
        this.biStep7 = biStep7In;
    }

    /**
     * Setter.
     * @param biStep8In
     *            the eighth step
     */
    public final void setBiStep8(final BuildingsIsletStep8 biStep8In)
    {
        this.biStep8 = biStep8In;
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

    public Vector3d getGravityNormal()
    {
        return gravityNormal;
    }

    public void setGravityNormal(Vector3d gravityNormal)
    {
        this.gravityNormal = gravityNormal;
    }

    /**
     * Exception class used when an attribute has not been defined whereas the
     * algorithm has been launched.
     * @author Daniel Lefevre
     */
    public final class VoidParameterException extends Exception
    {

        /**
         * Version ID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        public VoidParameterException()
        {
        }
    }
}
