package fr.nantes1900.models.extended.steps;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.extended.Roof;
import fr.nantes1900.models.extended.Wall;

/**
 * Implements a building step : a state of the building. This step is the last
 * step.
 * @author Daniel Lefèvre
 */
public class BuildingStep8 extends AbstractBuildingStep
{

    /**
     * The list of walls.
     */
    private List<Wall> walls = new ArrayList<>();

    /**
     * The list of roofs.
     */
    private List<Roof> roofs = new ArrayList<>();

    /**
     * Constructor.
     * @param wallsIn
     *            the list of walls
     * @param roofsIn
     *            the list of roofs
     */
    public BuildingStep8(final List<Wall> wallsIn, final List<Roof> roofsIn)
    {
        this.walls = wallsIn;
        this.roofs = roofsIn;
    }

    /**
     * Getter.
     * @return the list of roofs
     */
    public final List<Roof> getRoofs()
    {
        return this.roofs;
    }

    /**
     * @return the list of walls
     */
    public final List<Wall> getWalls()
    {
        return this.walls;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.extended.steps.AbstractBuildingStep#launchTreatment
     * ()
     */
    @Override
    public final AbstractBuildingStep launchTreatment()
    {
        // No more treatments for now : this method do nothing.
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.extended.steps.AbstractBuildingStep#returnNode()
     */
    @Override
    public final DefaultMutableTreeNode returnNode()
    {
        DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode();
        for (Wall w : this.walls)
        {
            currentNode.add(new DefaultMutableTreeNode(w.returnNode()));
        }
        for (Roof r : this.roofs)
        {
            currentNode.add(new DefaultMutableTreeNode(r.returnNode()));
        }

        return currentNode;
    }
}
