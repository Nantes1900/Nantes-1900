package fr.nantes1900.models.islets.buildings.steps;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;

public class BuildingsIsletStep3 extends AbstractBuildingsIsletStep
{

    private List<Building> buildings;

    private Ground         grounds;

    private Vector3d       groundNormal;
    private Vector3d       gravityNormal;

    private Mesh           noise;

    public BuildingsIsletStep3(List<Building> buildings, Ground groundsIn)
    {
        this.buildings = buildings;
        this.grounds = groundsIn;
    }

    public List<Building> getBuildings()
    {
        return this.buildings;
    }

    public Ground getGrounds()
    {
        return this.grounds;
    }

    public Mesh getNoise()
    {
        return this.noise;
    }

    /**
     * TODO. SeparationWallRoof
     */
    @Override
    public final BuildingsIsletStep4 launchTreatment()
    {
        for (Building b : this.buildings)
        {
            b.launchTreatment();
        }

        return new BuildingsIsletStep4(this.buildings, this.grounds);
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

    public void setArguments(Vector3d groundNormalIn, Vector3d gravityNormalIn)
    {
        this.groundNormal = groundNormalIn;
        this.gravityNormal = gravityNormalIn;
    }
}
