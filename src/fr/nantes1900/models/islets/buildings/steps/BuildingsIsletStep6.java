package fr.nantes1900.models.islets.buildings.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.steps.BuildingStep6;

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
    public final BuildingsIsletStep7 launchTreatment()
    {
        for (Building b : this.buildings)
        {
            BuildingStep6 buildingStep = b.getbStep6();
            buildingStep.setArguments(this.grounds);
            b.launchTreatment();
        }

        return new BuildingsIsletStep7(this.buildings, this.grounds);
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
        root.add(new DefaultMutableTreeNode(this.grounds));

        return root;
    }

    /**
     * Setter.
     * @param groundsIn
     *            the grounds
     */
    public final void setArguments(final Ground groundsIn)
    {
        this.grounds = groundsIn;
    }
}
