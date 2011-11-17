package fr.nantes1900.models.islets.buildings.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.Surface;

public class BuildingsIsletStep6 extends AbstractBuildingsIsletStep
{

    private List<Building> buildings;
    private Ground grounds;
    private Surface groundForAlgorithm;

    public List<Building> getBuildings()
    {
        return buildings;
    }

    public Ground getGrounds()
    {
        return grounds;
    }

    public BuildingsIsletStep6(List<Building> buildingsIn, Ground groundsIn)
    {
        this.buildings = buildingsIn;
        this.grounds = groundsIn;
    }

    /**
     * TODO. SortNeighbours
     * @return TODO.
     */
    @Override
    public final BuildingsIsletStep7 launchTreatment()
    {
        for (Building b : this.buildings)
        {
            b.launchTreatment();
        }

        return new BuildingsIsletStep7(this.buildings, this.grounds);
    }

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
}
