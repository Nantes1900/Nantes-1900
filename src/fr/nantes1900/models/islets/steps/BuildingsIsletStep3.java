package fr.nantes1900.models.islets.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.exceptions.NullArgumentException;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.AbstractBuildingsIslet;

/**
 * Implements a step of the process. This step is after the separation between
 * buildings and before the separation between walls and roofs.
 * @author Daniel Lefèvre
 */
public class BuildingsIsletStep3 extends AbstractBuildingsIsletStep {

    /**
     * The list of buildings contained in the islet.
     */
    private List<Building> buildings;

    /**
     * The grounds.
     */
    private Ground grounds;

    /**
     * The gravity normal used in algorithms.
     */
    private Vector3d gravityNormal;

    /**
     * The noise used in algorithms.
     */
    private Surface noise;

    /**
     * Constructor.
     * @param buildingsIn
     *            the list of buildings
     * @param groundsIn
     *            the ground
     */
    public BuildingsIsletStep3(final List<Building> buildingsIn,
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

    /**
     * Getter.
     * @return the noise
     */
    public final Surface getNoise() {
        return this.noise;
    }

    /*
     * (non-Javadoc)
     * @see fr.nantes1900.models.islets.steps.AbstractBuildingsIsletStep
     * #launchProcess()
     */
    @Override
    public final BuildingsIsletStep4 launchProcess()
            throws NullArgumentException {
        for (Building b : this.buildings) {
            b.getbStep3().setArguments(this.gravityNormal);
            b.launchProcess3();
        }

        BuildingsIsletStep4 biStep = new BuildingsIsletStep4(this.buildings,
                this.grounds);
        biStep.setArguments(this.noise);
        return biStep;
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
            if (!b.getbStep3().getInitialTotalSurface().getMesh().isEmpty()) {
                root.add(b.returnNode3(counter));
                counter++;
            }
        }

        if (!this.grounds.getMesh().isEmpty()) {
            this.grounds.setNodeString("Sols");
            root.add(new DefaultMutableTreeNode(this.grounds));

        }

        if (!this.noise.getMesh().isEmpty()) {
            this.noise.setNodeString("Bruit");
            root.add(new DefaultMutableTreeNode(this.noise));
        }

        return root;
    }

    /**
     * Setter.
     * @param noiseIn
     *            the noise
     */
    public final void setArguments(final Surface noiseIn) {
        this.noise = noiseIn;
    }

    /**
     * Setter.
     * @param gravityNormalIn
     *            the gravity normal used in process
     */
    public final void setArguments(final Vector3d gravityNormalIn) {
        this.gravityNormal = gravityNormalIn;
    }

    /*
     * (non-Javadoc)
     * @see fr.nantes1900.models.islets.steps.AbstractBuildingsIsletStep
     * #toString()
     */
    @Override
    public final String toString() {
        return super.toString() + AbstractBuildingsIslet.THIRD_STEP;
    }
}
