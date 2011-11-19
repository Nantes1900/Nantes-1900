package fr.nantes1900.models.extended.steps;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.extended.Roof;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.extended.Surface.ImpossibleNeighboursOrderException;
import fr.nantes1900.models.extended.Wall;

/**
 * Implements a building step : a state of the building. This step is after
 * TODO.
 * @author Daniel
 */
public class BuildingStep6 extends AbstractBuildingStep
{

    /**
     * The list of walls.
     */
    private List<Wall>       walls                    = new ArrayList<>();

    /**
     * The list of roofs.
     */
    private List<Roof>       roofs                    = new ArrayList<>();

    /**
     * The ground as a surface used in treatments.
     */
    private Surface          groundForAlgorithm;

    /**
     * Number minimal of neighbours to be considered as a real surface.
     */
    private static final int NUMBER_MIN_OF_NEIGHBOURS = 3;

    /**
     * Constructor.
     * @param wallsIn
     *            the list of walls
     * @param roofsIn
     *            the list of roofs
     */
    public BuildingStep6(final List<Wall> wallsIn, final List<Roof> roofsIn)
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
     * Getter.
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
    public final BuildingStep7 launchTreatment()
    {
        this.sortSurfaces();
        // TODO : if null ?
        this.orderNeighbours(this.groundForAlgorithm);

        List<Wall> wallsCopy = new ArrayList<>();
        for (Wall w : this.walls)
        {
            wallsCopy.add(new Wall(w));
        }
        List<Roof> roofsCopy = new ArrayList<>();
        for (Roof r : this.roofs)
        {
            roofsCopy.add(new Roof(r));
        }
        return new BuildingStep7(wallsCopy, roofsCopy);
    }

    /**
     * TODO.
     * @param grounds
     *            TODO.
     */
    public final void orderNeighbours(final Surface grounds)
    {
        // Adds all the surfaces
        final List<Surface> wholeList = new ArrayList<>();
        wholeList.addAll(this.walls);
        wholeList.addAll(this.roofs);

        for (final Surface surface : wholeList)
        {
            try
            {
                // Orders its neighbours in order to treat them.
                // If the neighbours of one surface are not 2 per 2 neighbours
                // each other, then it tries to correct it.
                surface.orderNeighbours(wholeList, grounds);

            } catch (final ImpossibleNeighboursOrderException e)
            {
                // If there is a problem, the treatment cannot continue.
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.extended.steps.AbstractBuildingStep#returnNode()
     */
    @Override
    public final DefaultMutableTreeNode returnNode()
    {
        // FIXME
        return null;
    }

    /**
     * Setter.
     * @param groundForAlgorithmIn
     *            the ground as a surface used in algorithms
     */
    public final void setArguments(final Surface groundForAlgorithmIn)
    {
        this.groundForAlgorithm = groundForAlgorithmIn;
    }

    /**
     * Removes every surfaces which have less than or equal 2 neighbours : it is
     * considered they are not really real surfaces.
     */
    private void sortSurfaces()
    {
        for (int i = 0; i < this.walls.size(); i++)
        {
            final Surface s = this.walls.get(i);
            if (s.getNeighbours().size() < NUMBER_MIN_OF_NEIGHBOURS)
            {
                this.walls.remove(s);
                for (final Surface neighbour : s.getNeighbours())
                {
                    neighbour.getNeighbours().remove(s);
                }
            }
        }
        for (int i = 0; i < this.roofs.size(); i++)
        {
            final Surface s = this.roofs.get(i);
            if (s.getNeighbours().size() < NUMBER_MIN_OF_NEIGHBOURS)
            {
                this.roofs.remove(s);
                for (final Surface neighbour : s.getNeighbours())
                {
                    neighbour.getNeighbours().remove(s);
                }
            }
        }
    }

}
