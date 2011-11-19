package fr.nantes1900.models.extended.steps;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.extended.Roof;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.extended.Surface.ImpossibleNeighboursOrderException;
import fr.nantes1900.models.extended.Wall;

public class BuildingStep6 extends AbstractBuildingStep
{

    /**
     * TODO.
     */
    private List<Wall> walls = new ArrayList<>();

    /**
     * TODO.
     */
    private List<Roof> roofs = new ArrayList<>();
    private Surface    groundForAlgorithm;

    public BuildingStep6(List<Wall> wallsIn, List<Roof> roofsIn)
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
    public BuildingStep7 launchTreatment()
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

    @Override
    public DefaultMutableTreeNode returnNode()
    {
        // FIXME
        return null;
    }

    // FIXME : what is the difference between searchForNeighbours and
    // determinateNeighbours.
    /**
     * TODO.
     * @param grounds
     *            TODO.
     */
    private void searchForNeighbours(final Surface grounds)
    {
        final Polygon groundsBounds = grounds.getMesh().returnUnsortedBounds();

        final List<Surface> wholeList = new ArrayList<>();
        wholeList.addAll(this.walls);
        wholeList.addAll(this.roofs);

        // First we clear the neighbours.
        for (final Surface m : wholeList)
        {
            m.getNeighbours().clear();
        }
        // And we clear the neighbours of the grounds.
        grounds.getNeighbours().clear();

        final List<Polygon> wholeBoundsList = new ArrayList<>();

        // We compute the bounds to check if they share a common edge.
        for (final Surface m : wholeList)
        {
            wholeBoundsList.add(m.getMesh().returnUnsortedBounds());
        }

        // Then we check every edge of the bounds to see if some are shared
        // by two meshes. If they do, they are neighbours.
        for (int i = 0; i < wholeBoundsList.size(); i = i + 1)
        {
            final Polygon polygone1 = wholeBoundsList.get(i);

            for (int j = i + 1; j < wholeBoundsList.size(); j = j + 1)
            {
                final Polygon polygone2 = wholeBoundsList.get(j);
                if (polygone1.isNeighbour(polygone2))
                {
                    wholeList.get(i).addNeighbour(wholeList.get(j));
                }
            }

            if (polygone1.isNeighbour(groundsBounds))
            {
                wholeList.get(i).addNeighbour(grounds);
            }
        }
    }

    public void setArguments(Surface groundForAlgorithmIn)
    {
        this.groundForAlgorithm = groundForAlgorithmIn;
    }

    /**
     * TODO.
     */
    public final void sortSurfaces()
    {
        int counter = 0;
        for (int i = 0; i < this.walls.size(); i++)
        {
            final Surface s = this.walls.get(i);
            if (s.getNeighbours().size() < 3)
            {
                this.walls.remove(s);
                for (final Surface neighbour : s.getNeighbours())
                {
                    neighbour.getNeighbours().remove(s);
                }
                counter++;
            }
        }
        for (int i = 0; i < this.roofs.size(); i++)
        {
            final Surface s = this.roofs.get(i);
            if (s.getNeighbours().size() < 3)
            {
                this.roofs.remove(s);
                for (final Surface neighbour : s.getNeighbours())
                {
                    neighbour.getNeighbours().remove(s);
                }
                counter++;
            }
        }
        System.out.println(" Isolated surfaces (not treated) : " + counter);
    }

}
