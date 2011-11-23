package fr.nantes1900.models.islets.buildings.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.Surface;

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
     * The ground under a Surface type used in treatments.
     */
    private Surface        groundForAlgorithm;
    /**
     * The noise used in the algorithms.
     */
    private Mesh           noise;

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

    /**
     * TODO. DeterminateNeighbours
     * @return TODO.
     */
    @Override
    public final BuildingsIsletStep6 launchTreatment()
    {
        for (Building b : this.buildings)
        {
            b.getbStep5().setArguments(this.noise, this.groundForAlgorithm);
            b.launchTreatment();
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
            root.add(b.returnNode());
        }
        DefaultMutableTreeNode nodeG = new DefaultMutableTreeNode(this.grounds);

        root.add(nodeG);

        return root;
    }

    /**
     * Setter.
     * @param noiseIn
     *            the noise
     * @param groundForAlgorithmIn
     *            the grounds as a surface used in treatments
     */
    public final void setArguments(final Mesh noiseIn,
            final Surface groundForAlgorithmIn)
    {
        this.noise = noiseIn;
        this.groundForAlgorithm = groundForAlgorithmIn;
    }
}
