package fr.nantes1900.models.islets.buildings.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.islets.buildings.exceptions.NullArgumentException;

/**
 * Implements a step of the process. This step is after the separation between
 * walls and roofs and before the separation between walls and the separation
 * between roofs.
 * @author Daniel Lefèvre
 */
public class BuildingsIsletStep4 extends AbstractBuildingsIsletStep {

    /**
     * The list of buildings.
     */
    private List<Building> buildings;
    /**
     * The grounds.
     */
    private Ground grounds;
    /**
     * The normal to the ground used in processs.
     */
    private Vector3d groundNormal;

    /**
     * The Surface containing the noise.
     */
    private Surface noise;

    /**
     * Constructor.
     * @param cutBuildings
     *            the list of buildings
     * @param groundsIn
     *            the grounds
     */
    public BuildingsIsletStep4(final List<Building> cutBuildings,
            final Ground groundsIn) {
        this.buildings = cutBuildings;
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
    public final BuildingsIsletStep5 launchProcess()
            throws NullArgumentException {

        for (Building b : this.buildings) {
            b.getbStep4().setArguments(this.groundNormal, this.grounds,
                    this.noise);
            b.launchProcess4();
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
    public final DefaultMutableTreeNode returnNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this);

        int counter = 0;
        for (Building b : this.buildings) {
            root.add(b.returnNode4(counter));
            counter++;
        }

        return root;
    }

    /**
     * Setter.
     * @param groundNormalIn
     *            the normal to the ground.
     * @param noiseIn
     *            the noise
     */
    public final void setArguments(final Vector3d groundNormalIn,
            final Surface noiseIn) {
        this.groundNormal = groundNormalIn;
        this.noise = noiseIn;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.islets.buildings.steps.AbstractBuildingsIsletStep
     * #toString()
     */
    @Override
    public final String toString() {
        return super.toString() + AbstractBuildingsIslet.FOURTH_STEP;
    }
}
