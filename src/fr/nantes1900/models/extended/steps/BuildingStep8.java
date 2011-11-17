package fr.nantes1900.models.extended.steps;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.extended.Roof;
import fr.nantes1900.models.extended.Wall;

public class BuildingStep8 extends AbstractBuildingStep
{

    /**
     * TODO.
     */
    private List<Wall> walls = new ArrayList<>();

    /**
     * TODO.
     */
    private List<Roof> roofs = new ArrayList<>();

    public BuildingStep8(List<Wall> wallsIn, List<Roof> roofsIn)
    {
        this.walls = wallsIn;
        this.roofs = roofsIn;
    }

    public List<Roof> getRoofs()
    {
        return roofs;
    }

    public List<Wall> getWalls()
    {
        return walls;
    }

    @Override
    public AbstractBuildingStep launchTreatment()
    {
        // TODO : return an error : not implemented.
        return null;
    }

    @Override
    public DefaultMutableTreeNode returnNode()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
