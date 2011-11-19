package fr.nantes1900.models.islets.buildings.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;

public class BuildingsIsletStep8 extends AbstractBuildingsIsletStep
{

    private List<Building> buildings;

    private Ground         grounds;

    public BuildingsIsletStep8(List<Building> buildingsIn, Ground groundsIn)
    {
        this.buildings = buildingsIn;
        this.grounds = groundsIn;
    }

    public List<Building> getBuildings()
    {
        return buildings;
    }

    public Ground getGrounds()
    {
        return grounds;
    }

    @Override
    public final AbstractBuildingsIsletStep launchTreatment()
    {
        // TODO : generate an error : not implemented.
        return null;
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
