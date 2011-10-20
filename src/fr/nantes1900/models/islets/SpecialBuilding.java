package fr.nantes1900.models.islets;

import fr.nantes1900.models.middle.Mesh;

/**
 * Implements a special building object as a container of a mesh.
 * 
 * @author Daniel Lefevre
 */
public class SpecialBuilding {

    /**
     * The mesh describing the surface of the special building.
     */
    private Mesh mesh = new Mesh();

    /**
     * Constructor.
     */
    public SpecialBuilding() {
    }

    /**
     * Builds a special building from a mesh. Not implemented.
     * 
     * @param m
     *            the special building as a mesh
     */
    public final void buildFromMesh(final Mesh m) {
        this.mesh = m;
        // TODO : implement this method.
    }

    /**
     * Getter.
     * 
     * @return the mesh
     */
    public final Mesh getMesh() {
        return this.mesh;
    }
}
