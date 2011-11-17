package fr.nantes1900.models.extended;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Polygon;

/**
 * Implements a roof as a surface.
 * @author Daniel
 */
public class Roof extends Surface
{

    /**
     * Constructor from a mesh.
     * @param m
     *            the mesh representing the roof
     */
    public Roof(final Mesh m)
    {
        super(m);
    }

    /**
     * Constructor from a polygon.
     * @param p
     *            the polygon representing the roof
     */
    public Roof(final Polygon p)
    {
        super(p);
    }

    public Roof(final Roof r)
    {
        super(r);
    }

    @Override
    public final String toString()
    {
        return "Roof";
    }
}
