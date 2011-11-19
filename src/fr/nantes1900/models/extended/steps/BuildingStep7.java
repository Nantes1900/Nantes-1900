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

public class BuildingStep7 extends AbstractBuildingStep
{

    /**
     * TODO.
     */
    private List<Wall> walls = new ArrayList<>();

    /**
     * TODO.
     */
    private List<Roof> roofs = new ArrayList<>();

    private Vector3d   groundNormal;

    public BuildingStep7(List<Wall> wallsIn, List<Roof> roofsIn)
    {
        this.walls = wallsIn;
        this.roofs = roofsIn;
    }

    /**
     * TODO.
     * @param normalGround
     *            TODO.
     */
    public final void determinateContours(final Vector3d normalGround)
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
                        normalGround);

                surface.setPolygone(p);
            } catch (final InvalidSurfaceException e)
            {
                // If there is a problem, we cannot continue the treatment.
            }
        }
    }

    public List<Roof> getRoofs()
    {
        return roofs;
    }

    public List<Wall> getWalls()
    {
        return walls;
    }

    public BuildingStep8 launchTreatment()
    {
        this.determinateContours(this.groundNormal);

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
    public DefaultMutableTreeNode returnNode()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setArguments(Vector3d groundNormalIn)
    {
        this.groundNormal = groundNormalIn;
    }

}
