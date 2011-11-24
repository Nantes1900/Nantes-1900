package fr.nantes1900.models.islets.buildings.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.steps.BuildingStep4;
import fr.nantes1900.models.islets.buildings.exceptions.NullArgumentException;

/**
 * Implements a step of the treatment. This step is after the separation between
 * walls and roofs and before the separation between walls and the separation
 * between roofs.
 * @author Daniel Lefèvre
 */
public class BuildingsIsletStep4 extends AbstractBuildingsIsletStep
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
     * The normal to the ground used in treatments.
     */
    private Vector3d       groundNormal;

    /**
     * Constructor.
     * @param cutBuildings
     *            the list of buildings
     * @param groundsIn
     *            the grounds
     */
    public BuildingsIsletStep4(final List<Building> cutBuildings,
            final Ground groundsIn)
    {
        this.buildings = cutBuildings;
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
    public final BuildingsIsletStep5
            launchTreatment() throws NullArgumentException
    {
        for (Building b : this.buildings)
        {
            BuildingStep4 buildingStep = b.getbStep4();
            buildingStep.setArguments(this.groundNormal, this.grounds);
            b.launchTreatment4();
        }

        return new BuildingsIsletStep5(this.buildings, this.grounds);
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
            root.add(b.returnNode3());
        }
        root.add(new DefaultMutableTreeNode(this.grounds));

        return root;
    }

    /**
     * Setter.
     * @param groundNormalIn
     *            the normal to the ground.
     */
    public final void setArguments(final Vector3d groundNormalIn)
    {
        this.groundNormal = groundNormalIn;
    }
}
