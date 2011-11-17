package fr.nantes1900.models.extended.steps;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.constants.SeparationWallsSeparationRoofs;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.extended.Roof;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.extended.Wall;
import fr.nantes1900.utils.Algos;

public class BuildingStep4 extends AbstractBuildingStep
{

    private Mesh initialWall;
    private Mesh initialRoof;
    private Mesh noise;
    private Vector3d groundNormal;
    private Surface groundForAlgorithm;
    private List<Wall> walls;
    private List<Roof> roofs;

    public BuildingStep4(Mesh initialWallIn, Mesh initialRoofIn)
    {
        this.initialWall = initialWallIn;
        this.initialRoof = initialRoofIn;
    }

    /**
     * TODO.
     * @param groundNormal
     *            TODO.
     */
    public final void cutRoofs()
    {
        // Cut the mesh in parts, considering their orientation.
        final List<Mesh> thingsList = Algos.blockOrientedExtract(
                this.initialRoof,
                SeparationWallsSeparationRoofs.getRoofAngleError());

        // Considering their size and their orientation, sort the blocks in
        // roofs or noise. If a wall is oriented in direction of the ground,
        // it is not keeped.
        for (final Mesh e : thingsList)
        {
            if ((e.size() >= SeparationWallsSeparationRoofs.getRoofSizeError())
                    && (e.averageNormal().dot(this.groundNormal) > 0))
            {
                this.roofs.add(new Roof(e));
            } else
            {
                this.noise.addAll(e);
            }
        }
    }

    public Mesh getInitialWall()
    {
        return initialWall;
    }

    public Mesh getInitialRoof()
    {
        return initialRoof;
    }

    /**
     * TODO.
     */
    public final void cutWalls()
    {
        // Cut the mesh in parts, considering their orientation.
        final List<Mesh> thingsList = Algos.blockOrientedExtract(
                this.initialWall,
                SeparationWallsSeparationRoofs.getWallAngleError());

        // Considering their size, sort the blocks in walls or noise.
        for (final Mesh e : thingsList)
        {
            if (e.size() >= SeparationWallsSeparationRoofs.getWallSizeError())
            {
                this.walls.add(new Wall(e));
            } else
            {
                this.noise.addAll(e);
            }
        }
    }

    @Override
    public final BuildingStep5 launchTreatment()
    {
        this.cutWalls();
        this.cutRoofs();
        this.treatNoise();
        this.treatNewNeighbours(this.groundForAlgorithm);

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
        return new BuildingStep5(wallsCopy, roofsCopy);
    }

    @Override
    public final DefaultMutableTreeNode returnNode()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();

        root.add(new DefaultMutableTreeNode(this.initialWall));
        root.add(new DefaultMutableTreeNode(this.initialRoof));

        return root;
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

    public void serArguments(Vector3d groundNormalIn,
            Surface groundForAlgorithmIn)
    {
        // TODO : check if this method has been called before lauching
        // treatment.
        this.groundNormal = groundNormalIn;
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

    /**
     * TODO.
     * @param grounds
     *            TODO.
     */
    public final void treatNewNeighbours(final Surface grounds)
    {
        this.searchForNeighbours(grounds);

        // After the noise addition, if some of the walls or some of the
        // roofs are now neighbours (they share an edge) and have the same
        // orientation, then they are added to form only one wall or roof.

        // Wall is prioritary : it means that if a roof touch a wall, this
        // roof is added to the wall, and not the inverse.

        final List<Surface> wholeList = new ArrayList<>();
        wholeList.addAll(this.walls);
        wholeList.addAll(this.roofs);

        for (int i = 0; i < wholeList.size(); i = i + 1)
        {
            final Surface surface = wholeList.get(i);

            final List<Surface> oriented = new ArrayList<>();
            final List<Surface> ret = new ArrayList<>();

            for (final Surface m : wholeList)
            {
                if (m.getMesh().isOrientedAs(surface.getMesh(),
                        SeparationWallsSeparationRoofs.getMiddleAngleError()))
                {
                    oriented.add(m);
                }
            }

            surface.returnNeighbours(ret, oriented);

            for (final Surface m : ret)
            {
                if (m != surface)
                {
                    surface.getMesh().addAll(m.getMesh());
                    wholeList.remove(m);
                    this.walls.remove(m);
                    this.roofs.remove(m);
                }
            }
        }
    }

    /**
     * TODO.
     */
    public final void treatNoise()
    {
        List<Surface> wallsOut = new ArrayList<>();
        for (Wall w : this.walls)
        {
            wallsOut.add(w);
        }
        List<Surface> roofsOut = new ArrayList<>();
        for (Roof r : this.roofs)
        {
            roofsOut.add(r);
        }
        // Adds the oriented and neighbour noise to the walls.
        Algos.blockTreatOrientedNoise(wallsOut, this.noise,
                SeparationWallsSeparationRoofs.getLargeAngleError());

        // Adds the oriented and neighbour noise to the roofs.
        Algos.blockTreatOrientedNoise(roofsOut, this.noise,
                SeparationWallsSeparationRoofs.getLargeAngleError());
    }

}
