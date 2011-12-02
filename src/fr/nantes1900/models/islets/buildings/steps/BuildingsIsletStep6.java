package fr.nantes1900.models.islets.buildings.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;

/**
 * Implements a step of the process. This step is after the determination of the
 * neighbours and before the sort of the neighbours.
 * @author Daniel Lefèvre
 */
public class BuildingsIsletStep6 extends AbstractBuildingsIsletStep {

    /**
     * The list of buildings.
     */
    private List<Building> buildings;
    /**
     * The grounds.
     */
    private Ground grounds;

    /**
     * Constructor.
     * @param buildingsIn
     *            the list of buildings
     * @param groundsIn
     *            te grounds
     */
    public BuildingsIsletStep6(final List<Building> buildingsIn,
            final Ground groundsIn) {
        this.buildings = buildingsIn;
        this.grounds = groundsIn;
    }

    /**
     * Getter.
     * @return the list of buildings
     */
    public final List<Building> getBuildings() {
        return this.buildings;
    }

    /**
     * Getter.
     * @return the grounds
     */
    public final Ground getGrounds() {
        return this.grounds;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.islets.buildings.steps.AbstractBuildingsIsletStep
     * #launchProcess()
     */
    @Override
    public final AbstractBuildingsIsletStep launchProcess() {
        // There is no more process for now.
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.islets.buildings.steps.AbstractBuildingsIsletStep
     * #returnNode()
     */
    @Override
    public final DefaultMutableTreeNode returnNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this);
        for (Building b : this.buildings) {
            root.add(b.returnNode6());
        }
        root.add(new DefaultMutableTreeNode(this.grounds));

        return root;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.islets.buildings.steps.AbstractBuildingsIsletStep
     * #toString()
     */
    @Override
    public final String toString() {
        return super.toString() + AbstractBuildingsIslet.SIXTH_STEP;
    }
}
