package fr.nantes1900.models.extended;

import fr.nantes1900.models.Mesh;

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
     * Build a special building from a mesh.
     * 
     * @param m
     *            the special building as a mesh
     */
    public final void buildFromMesh(final Mesh m) {
        this.mesh = m;
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
