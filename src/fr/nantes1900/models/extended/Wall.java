package fr.nantes1900.models.extended;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Polygon;

/**
 * Implements a wall as a surface.
 * @author Daniel
 */
public class Wall extends Surface
{

    /**
     * Constructor from a mesh.
     * @param m
     *            the mesh representing the wall
     */
    public Wall(final Mesh m)
    {
        super(m);
    }

    /**
     * Constructor from a polygon.
     * @param p
     *            the polygon representing the wall
     */
    public Wall(final Polygon p)
    {
        super(p);
    }

    public Wall(Wall w)
    {
        super(w);
    }

    @Override
    public final String toString()
    {
        return "Wall";
    }
}
