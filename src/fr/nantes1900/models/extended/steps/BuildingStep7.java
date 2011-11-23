package fr.nantes1900.models.extended.steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.extended.Roof;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.extended.Surface.InvalidSurfaceException;
import fr.nantes1900.models.extended.Wall;
import fr.nantes1900.models.islets.buildings.exceptions.NullArgumentException;

/**
 * Implements a building step : a state of the building. This step is after the
 * sort of each surfaces and before the determination of the contour.
 * @author Daniel Lefèvre
 */
public class BuildingStep7 extends AbstractBuildingStep
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
     * The normal to the ground.
     */
    private Vector3d   groundNormal;

    /**
     * Constructor.
     * @param wallsIn
     *            the list of walls
     * @param roofsIn
     *            the list of roofs
     */
    public BuildingStep7(final List<Wall> wallsIn, final List<Roof> roofsIn)
    {
        this.walls = wallsIn;
        this.roofs = roofsIn;
    }

    /**
     * Computes the contour of the surface, using the sorted neighbours.
     */
    public final void determinateContours()
    {
        // Creates the map where the points and edges will be put : if one
        // point is created a second time, it will be given the same
        // reference as the other one having the same values.
        final Map<Point, Point> pointMap = new HashMap<>();

        // Adds all the surfaces
        final List<Surface> wholeList = new ArrayList<>();
        wholeList.addAll(this.walls);
        wholeList.addAll(this.roofs);

        for (final Surface surface : wholeList)
        {
            try
            {
                // When the neighbours are sorted, finds the intersection of
                // them to find the edges of this surface.
                final Polygon p = surface.findEdges(this.walls,
                        pointMap,
                        this.groundNormal);

                surface.setPolygone(p);
            } catch (final InvalidSurfaceException e)
            {
                // If there is a problem, we cannot continue the treatment.
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

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.extended.steps.AbstractBuildingStep#launchTreatment
     * ()
     */
    @Override
    public final BuildingStep8 launchTreatment() throws NullArgumentException
    {
        if (this.groundNormal == null)
        {
            throw new NullArgumentException();
        }
        this.determinateContours();

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
        return new BuildingStep8(wallsCopy, roofsCopy);
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
     * @param groundNormalIn
     *            the normal to the ground
     */
    public final void setArguments(final Vector3d groundNormalIn)
    {
        this.groundNormal = groundNormalIn;
    }

}
