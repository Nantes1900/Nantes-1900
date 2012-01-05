package fr.nantes1900.models.islets.steps;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.coefficients.SeparationBuildings;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.AbstractBuildingsIslet;
import fr.nantes1900.utils.Algos;

/**
 * Implements a step of the process. This step is after the separation between
 * grounds and buildings and before the separation between buildings.
 * @author Daniel Lefèvre
 */
public class BuildingsIsletStep2 extends AbstractBuildingsIsletStep {

    /**
     * The mesh containing every buildings.
     */
    private Surface initialBuildings;
    /**
     * The mesh containing every grounds.
     */
    private Ground initialGrounds;

    /**
     * The mesh containing the noise during the processs.
     */
    private Surface noise;

    /**
     * Constructor.
     * @param initialBuildingsIn
     *            the mesh containing all the buildings
     * @param groundsIn
     *            the mesh containing all the grounds
     */
    public BuildingsIsletStep2(final Surface initialBuildingsIn,
            final Ground groundsIn) {
        this.initialBuildings = initialBuildingsIn;
        this.initialGrounds = groundsIn;
    }

    /**
     * Extracts buildings by separating the blocks after the ground extraction.
     * @return the list of the different buildings
     */
    private List<Building> buildingsExtraction() {
        final List<Mesh> buildingList = new ArrayList<>();

        List<Mesh> thingsList;
        // Extraction of the buildings.
        thingsList = Algos.blockExtract(this.initialBuildings.getMesh());

        // Steprithm : detection of buildings considering their size.
        for (final Mesh m : thingsList) {
            if (m.size() >= SeparationBuildings.getBlockBuildingSize()) {
                buildingList.add(m);
            } else {
                this.noise.getMesh().addAll(m);
            }
        }

        List<Building> buildings = new ArrayList<>();

        for (Mesh m : buildingList) {
            buildings.add(new Building(new Surface(m)));
        }

        return buildings;
    }

    /**
     * Getter.
     * @return the set of every buildings
     */
    public final Surface getInitialBuildings() {
        return this.initialBuildings;
    }

    /**
     * Getter.
     * @return the set of every grounds
     */
    public final Ground getInitialGrounds() {
        return this.initialGrounds;
    }

    /**
     * Getter.
     * @return the noise
     */
    public final Surface getNoise() {
        return this.noise;
    }

    @Override
    public final BuildingsIsletStep3 launchProcess() {
        this.noise = new Surface();

        List<Building> buildings = this.buildingsExtraction();

        this.initialGrounds = this.noiseProcess();

        BuildingsIsletStep3 biStep3 = new BuildingsIsletStep3(buildings,
                this.initialGrounds);
        biStep3.setArguments(this.noise);

        return biStep3;
    }

    /**
     * Treats the noise by calling the method Algos.blockTreatNoise.
     * @return the ground of this islet
     */
    private Ground noiseProcess() {
        List<Mesh> list = Algos.blockExtract(this.initialGrounds.getMesh());
        return new Ground(Algos.blockTreatNoise(list, this.noise.getMesh()));
    }

    /*
     * (non-Javadoc)
     * @see fr.nantes1900.models.islets.steps.AbstractBuildingsIsletStep
     * #returnNode()
     */
    @Override
    public final DefaultMutableTreeNode returnNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this);

        if (!this.getInitialBuildings().getMesh().isEmpty()) {
            this.initialBuildings.setNodeString("Bâtiments");
            DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(
                    this.initialBuildings);
            root.add(node1);

        }

        if (!this.getInitialGrounds().getMesh().isEmpty()) {
            this.initialGrounds.setNodeString("Sols");
            DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(
                    this.initialGrounds);
            root.add(node2);

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
        return super.toString() + AbstractBuildingsIslet.SECOND_STEP;
    }
}
