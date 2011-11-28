package fr.nantes1900.models.islets.buildings.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.extended.steps.BuildingStep5;
import fr.nantes1900.models.islets.buildings.exceptions.NullArgumentException;

/**
 * Implements a step of the treatment. This step is after the separation between
 * walls and the separation between roofs and before the determination of the
 * neighbours.
 * @author Daniel Lefèvre
 */
public class BuildingsIsletStep5 extends AbstractBuildingsIsletStep
{

    /**
     * The list of buildings.
     */
    private List<Building> buildings;
    /**
     * The grounds.
     */
    private Ground         grounds;
    /**
     * The noise used in the algorithms.
     */
    private Surface        noise;

    /**
     * Getter.
     * @return the noise
     */
    public final Surface getNoise()
    {
        return this.noise;
    }

    /**
     * Constructor.
     * @param buildingsIn
     *            the list of buildings
     * @param groundsIn
     *            the grounds
     */
    public BuildingsIsletStep5(final List<Building> buildingsIn,
            final Ground groundsIn)
    {
        this.buildings = buildingsIn;
        this.grounds = groundsIn;
    }

    /**
     * Getter.
     * @return the list of buildings
     */
    public final List<Building> getBuildings()
    {
        return this.buildings;
    }

    /**
     * Getter.
     * @return the grounds
     */
    public final Ground getGrounds()
    {
        return this.grounds;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.islets.buildings.steps.AbstractBuildingsIsletStep
     * #launchTreatment()
     */
    @Override
    public final BuildingsIsletStep6 launchTreatment()
            throws NullArgumentException
    {
        for (Building b : this.buildings)
        {
            BuildingStep5 buildingStep = b.getbStep5();
            buildingStep.setArguments(this.noise, this.grounds);
            b.launchTreatment5();
        }

        return new BuildingsIsletStep6(this.buildings, this.grounds);
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.islets.buildings.steps.AbstractBuildingsIsletStep
     * #returnNode()
     */
    @Override
    public final DefaultMutableTreeNode returnNode()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this);
        for (Building b : this.buildings)
        {
            root.add(b.returnNode5());
        }
        root.add(new DefaultMutableTreeNode(this.grounds));

        return root;
    }

    /**
     * Setter.
     * @param noiseIn
     *            the noise
     * @param groundsIn
     *            the grounds
     */
    public final void
            setArguments(final Surface noiseIn, final Ground groundsIn)
    {
        this.noise = noiseIn;
        this.grounds = groundsIn;
    }

    @Override
    public String toString()
    {
        return super.toString() + "5";
    }
}
