package fr.nantes1900.models.islets.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.islets.AbstractBuildingsIslet;

/**
 * Implements a step of the process. This step is after the determination of the
 * neighbours and the sort of the neighbours. This step simplifies the ground
 * and resticks it to the simplified buildings.
 * @author Daniel Lefèvre, Camille Bouquet
 */
public class BuildingsIsletStep7 extends AbstractBuildingsIsletStep implements
        Writable {

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
    public BuildingsIsletStep7(final List<Building> buildingsIn,
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
     * @see fr.nantes1900.models.islets.steps.AbstractBuildingsIsletStep
     * #launchProcess()
     */
    @Override
    public final AbstractBuildingsIsletStep launchProcess() {
        // There is no more process.
        return null;
    }

    /*
     * (non-Javadoc)
     * @see fr.nantes1900.models.islets.steps.AbstractBuildingsIsletStep
     * #returnNode()
     */
    @Override
    public final DefaultMutableTreeNode returnNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this);

        int counter = 0;
        for (Building b : this.buildings) {
            root.add(b.returnNode7(counter));
            counter++;
        }

        if (!this.getGrounds().getMesh().isEmpty()) {
            this.grounds.setNodeString("Sols");
            root.add(new DefaultMutableTreeNode(this.grounds));
        }

        return root;
    }

    /*
     * (non-Javadoc)
     * @see fr.nantes1900.models.islets.steps.AbstractBuildingsIsletStep
     * #toString()
     */
    @Override
    public final String toString() {
        return super.toString() + AbstractBuildingsIslet.SEVENTH_STEP;
    }

}
