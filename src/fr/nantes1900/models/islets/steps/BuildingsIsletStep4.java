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
 * walls and roofs and before the separation between walls and the separation
 * between roofs.
 * @author Daniel Lefèvre
 */
public class BuildingsIsletStep4 extends AbstractBuildingsIsletStep implements Writable {

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
    public final BuildingsIsletStep5 launchProcess()
            throws NullArgumentException {

        ProgressComputer.initBuildingsCounter();
        ProgressComputer.setBuildingsNumber(this.buildings.size());

        for (Building b : this.buildings) {
            b.getbStep4().setArguments(this.groundNormal, this.grounds,
                    this.noise);
            b.launchProcess4();

            ProgressComputer.incBuildingsCounter();
        }

        BuildingsIsletStep5 biStep = new BuildingsIsletStep5(this.buildings,
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
            root.add(b.returnNode4(counter));
            counter++;
        }

        if (this.getNoise().getMesh() != null
                && !this.getNoise().getMesh().isEmpty()) {
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
     * @param groundNormalIn
     *            the normal to the ground.
     */
    public final void setArguments(final Vector3d groundNormalIn) {
        this.groundNormal = groundNormalIn;
    }

    /*
     * (non-Javadoc)
     * @see fr.nantes1900.models.islets.steps.AbstractBuildingsIsletStep
     * #toString()
     */
    @Override
    public final String toString() {
        return super.toString() + AbstractBuildingsIslet.FOURTH_STEP;
    }
}
