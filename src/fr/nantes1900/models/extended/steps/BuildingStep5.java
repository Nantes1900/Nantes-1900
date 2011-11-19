package fr.nantes1900.models.extended.steps;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.constants.SeparationWallsSeparationRoofs;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.extended.Roof;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.extended.Wall;
import fr.nantes1900.utils.Algos;

/**
 * TODO.
 * @author Daniel
 */
public class BuildingStep5 extends AbstractBuildingStep
{

    /**
     * The mesh representing the noise.
     */
    private Mesh       noise;

    /**
     * The list of walls.
     */
    private List<Wall> walls = new ArrayList<>();

    /**
     * The list of roofs.
     */
    private List<Roof> roofs = new ArrayList<>();

    /**
     * The ground as a surface used in treatments.
     */
    private Surface    groundForAlgorithm;

    /**
     * Constructor.
     * @param wallsIn
     *            the list of walls
     * @param roofsIn
     *            the list of roofs
     */
    public BuildingStep5(final List<Wall> wallsIn, final List<Roof> roofsIn)
    {
        this.walls = wallsIn;
        this.roofs = roofsIn;
    }

    /**
     * TODO.
     * @param grounds
     *            TODO.
     */
    public final void determinateNeighbours(final Surface grounds)
    {
        final Polygon groundsBounds = grounds.getMesh().returnUnsortedBounds();

        final List<Surface> wholeList = new ArrayList<>();
        wholeList.addAll(this.walls);
        wholeList.addAll(this.roofs);

        // To find every neighbours, we complete every holes between roofs
        // and walls by adding all the noise.
        final List<Mesh> wholeListFakes = new ArrayList<>();
        for (final Surface m : wholeList)
        {
            final Mesh fake = new Mesh(m.getMesh());
            wholeListFakes.add(fake);
        }
        Algos.blockTreatPlanedNoise(wholeListFakes,
                this.noise,
                SeparationWallsSeparationRoofs.getPlanesError());

        // First we clear the neighbours.
        for (final Surface s : wholeList)
        {
            s.getNeighbours().clear();
        }
        // And we clear the neighbours of the grounds.
        grounds.getNeighbours().clear();

        // We compute the bounds to check if they share a common edge.
        final List<Polygon> wholeBoundsList = new ArrayList<>();
        for (final Mesh m : wholeListFakes)
        {
            wholeBoundsList.add(m.returnUnsortedBounds());
        }

        // Then we check every edge of the bounds to see if some are shared by
        // two meshes. If they do, they are neighbours.
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

    @Override
    public final BuildingStep6 launchTreatment()
    {
        if (this.groundForAlgorithm == null)
        {
            throw new InvalidParameterException();
        }
        this.determinateNeighbours(this.groundForAlgorithm);

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
        return new BuildingStep6(wallsCopy, roofsCopy);
    }

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

    /**
     * Setter.
     * @param noiseIn
     *            the noise
     * @param groundForAlgorithmIn
     *            the ground as surface used in treatments
     */
    public final void setArguments(final Mesh noiseIn,
            final Surface groundForAlgorithmIn)
    {
        this.noise = noiseIn;
        this.groundForAlgorithm = groundForAlgorithmIn;
    }
}
