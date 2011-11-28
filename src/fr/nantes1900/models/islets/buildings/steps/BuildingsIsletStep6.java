package fr.nantes1900.models.islets.buildings.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;

/**
 * Implements a step of the treatment. This step is after the determination of
 * the neighbours and before the sort of the neighbours.
 * @author Daniel Lefèvre
 */
public class BuildingsIsletStep6 extends AbstractBuildingsIsletStep
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
     * Constructor.
     * @param buildingsIn
     *            the list of buildings
     * @param groundsIn
     *            te grounds
     */
    public BuildingsIsletStep6(final List<Building> buildingsIn,
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
    public final AbstractBuildingsIsletStep launchTreatment()
    {
        // There is no more treatment for now.
        return null;
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
            root.add(b.returnNode6());
        }
        root.add(new DefaultMutableTreeNode(this.grounds));

        return root;
    }

    @Override
    public String toString()
    {
        return super.toString() + "6";
    }
}
