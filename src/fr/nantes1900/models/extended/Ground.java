package fr.nantes1900.models.extended;

import fr.nantes1900.models.basis.Mesh;

/**
 * Implements a ground as a mesh representing the ground surface.
 * @author Daniel
 */
public final class Ground extends Mesh
{

    /**
     * Version attribute.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor from a Mesh.
     * @param m
     *            the mesh representing this ground
     */
    public Ground(final Mesh m)
    {
        super(m);
    }
}
