package fr.nantes1900.models.islets.buildings.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.extended.steps.BuildingStep5;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.islets.buildings.exceptions.NullArgumentException;

/**
 * Implements a step of the process. This step is after the separation between
 * walls and the separation between roofs and before the determination of the
 * neighbours.
 * @author Daniel Lefèvre
 */
public class BuildingsIsletStep5 extends AbstractBuildingsIsletStep {

    /**
     * The list of buildings.
     */
    private List<Building> buildings;
    /**
     * The grounds.
     */
    private Ground grounds;
    /**
     * The noise used in the algorithms.
     */
    private Surface noise;

    /**
     * The normal to the ground.
     */
    private Vector3d groundNormal;

    /**
     * Constructor.
     * @param buildingsIn
     *            the list of buildings
     * @param groundsIn
     *            the grounds
     */
    public BuildingsIsletStep5(final List<Building> buildingsIn,
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
     * @see
     * fr.nantes1900.models.islets.buildings.steps.AbstractBuildingsIsletStep
     * #launchProcess()
     */
    @Override
    public final BuildingsIsletStep6 launchProcess()
            throws NullArgumentException {
        for (Building b : this.buildings) {
            BuildingStep5 buildingStep = b.getbStep5();
            buildingStep.setArguments(this.noise, this.grounds,
                    this.groundNormal);
            b.launchProcess5();
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
    public final DefaultMutableTreeNode returnNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this);

        int counter = 0;
        for (Building b : this.buildings) {
            root.add(b.returnNode5(counter));
            counter++;
        }

        if (this.getNoise().getMesh() != null
                && !this.getNoise().getMesh().isEmpty()) {
            this.getNoise().setNodeString("Noise");
            root.add(new DefaultMutableTreeNode(this.getNoise()));
        } else {
            // TODO : pop-up
            System.out.println("Noise empty : error !");
        }

        return root;
    }

    /**
     * Setter.
     * @param groundsIn
     *            the grounds
     * @param groundNormalIn
     *            the normal to the ground
     */
    public final void setArguments(final Ground groundsIn,
            final Vector3d groundNormalIn) {
        this.grounds = groundsIn;
        this.groundNormal = groundNormalIn;
    }

    /**
     * Setter.
     * @param noiseIn
     *            the noise
     */
    public final void setArguments(final Surface noiseIn) {
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
        return super.toString() + AbstractBuildingsIslet.FIFTH_STEP;
    }
}
