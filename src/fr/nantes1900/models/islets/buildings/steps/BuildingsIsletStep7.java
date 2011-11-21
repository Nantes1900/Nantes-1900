package fr.nantes1900.models.islets.buildings.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.steps.BuildingStep7;

/**
 * Implements a step of the treatment. This step is after the sort of the
 * neighbours and before the determination of the contour.
 * @author Daniel Lefèvre
 */
/**
 * @author Daniel
 */
public class BuildingsIsletStep7 extends AbstractBuildingsIsletStep
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
     * The normal to the ground.
     */
    private Vector3d       groundNormal;

    /**
     * Constructor.
     * @param cutBuildings
     *            the list of buildings
     * @param groundsIn
     *            the grounds
     */
    public BuildingsIsletStep7(final List<Building> cutBuildings,
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
    public final BuildingsIsletStep8 launchTreatment()
    {
        for (Building b : this.buildings)
        {
            BuildingStep7 buildingStep = (BuildingStep7) b.getbStep();
            buildingStep.setArguments(this.groundNormal);
            b.launchTreatment();
        }

        return new BuildingsIsletStep8(this.buildings, this.grounds);
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
     * @param groundNormalIn
     *            the normal to the ground
     */
    public final void setArguments(final Vector3d groundNormalIn)
    {
        this.groundNormal = groundNormalIn;
    }
}
