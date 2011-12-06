package fr.nantes1900.models.extended;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Polygon;

/**
 * Implements a wall as a surface.
 * @author Daniel
 */
/**
 * @author Daniel
 */
public class Wall extends Surface {

    /**
     * Void constructor.
     */
    public Wall() {
    }

    /**
     * Constructor from a mesh.
     * @param m
     *            the mesh representing the wall
     */
    public Wall(final Mesh m) {
        super(m);
    }

    /**
     * Constructor from a polygon.
     * @param p
     *            the polygon representing the wall
     */
    public Wall(final Polygon p) {
        super(p);
    }

    /**
     * Copy constructor.
     * @param w
     *            the wall to copy
     */
    public Wall(final Wall w) {
        super(w);
    }

    /**
     * Constructor.
     * @param s
     *            the surface to copy
     */
    public Wall(final Surface s) {
        super(s);
    }
}
