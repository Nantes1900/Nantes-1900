package fr.nantes1900.models.islets.buildings.steps;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.constants.SeparationBuildings;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.utils.Algos;

/**
 * Implements a step of the treatment. This step is after the separation between
 * grounds and buildings and before the separation between buildings.
 * @author Daniel Lefèvre
 */
public class BuildingsIsletStep2 extends AbstractBuildingsIsletStep
{

    /**
     * The mesh containing every buildings.
     */
    private Mesh   initialBuildings;
    /**
     * The mesh containing every grounds.
     */
    private Ground initialGrounds;

    /**
     * Getter.
     * @return the noise
     */
    public final Mesh getNoise()
    {
        return this.noise;
    }

    /**
     * The mesh containing the noise during the treatments.
     */
    private Mesh noise;

    /**
     * Constructor.
     * @param buildings
     *            the mesh containing all the buildings
     * @param grounds
     *            the mesh containing all the grounds
     */
    public BuildingsIsletStep2(final Mesh buildings, final Ground grounds)
    {
        this.initialBuildings = buildings;
        this.initialGrounds = grounds;
    }

    /**
     * Extracts buildings by separating the blocks after the ground extraction.
     * @return the list of the different buildings
     */
    private List<Building> buildingsExtraction()
    {
        final List<Mesh> buildingList = new ArrayList<>();

        List<Mesh> thingsList;
        // Extraction of the buildings.
        thingsList = Algos.blockExtract(this.initialBuildings);

        // Steprithm : detection of buildings considering their size.
        for (final Mesh m : thingsList)
        {
            if (m.size() >= SeparationBuildings.getBlockBuildingSize())
            {
                buildingList.add(m);
            } else
            {
                this.noise.addAll(m);
            }
        }

        if (buildingList.size() == 0)
        {
            System.out.println("Error : no building found !");
        }

        List<Building> buildings = new ArrayList<>();

        for (Mesh m : buildingList)
        {
            buildings.add(new Building(m));
        }

        return buildings;
    }

    /**
     * Getter.
     * @return the set of every buildings
     */
    public final Mesh getInitialBuildings()
    {
        return this.initialBuildings;
    }

    /**
     * Getter.
     * @return the set of every grounds
     */
    public final Ground getInitialGrounds()
    {
        return this.initialGrounds;
    }

    @Override
    public final BuildingsIsletStep3 launchTreatment()
    {
        this.noise = new Mesh();

        List<Building> buildings = this.buildingsExtraction();

        this.initialGrounds = this.noiseTreatment();

        return new BuildingsIsletStep3(buildings, this.initialGrounds);
    }

    /**
     * Treats the noise by calling the method Algos.blockTreatNoise.
     * @return the ground of this islet
     */
    private Ground noiseTreatment()
    {
        List<Mesh> list = Algos.blockExtract(this.initialGrounds.getMesh());
        return new Ground(Algos.blockTreatNoise(list, this.noise));
    }

    @Override
    public final DefaultMutableTreeNode returnNode()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this);
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(this.initialBuildings);
        DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(this.initialGrounds);

        root.add(node1);
        root.add(node2);

        return root;
    }
}