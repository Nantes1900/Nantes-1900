package fr.nantes1900.models.extended;

import fr.nantes1900.models.Mesh;

/**
 * Implements a floor as a mesh with an associated String attribute.
 * 
 * @author Daniel Lefevre
 */
public class Floor {

    /**
     * The attribute describing the type of the floor.
     */
    private String attribute;
    /**
     * The mesh describing the surface.
     */
    private Mesh mesh = new Mesh();

    /**
     * Constructor. Build the Floor with an attribute as a String.
     * 
     * @param type
     *            the attribute
     */
    public Floor(final String type) {
        super();
        this.attribute = type;
    }

    /**
     * Build a floor from a mesh, by computing the algorithms.
     * 
     * @param m
     *            the mesh to convert
     */
    public final void buildFromMesh(final Mesh m) {
        this.mesh = new Mesh(m);

        this.decimate(this.mesh);
    }

    /**
     * Decimate the floor.
     * 
     * @param floor
     *            the floor to decimate as a mesh
     */
    public void decimate(final Mesh floor) {
        // TODO : code this method.
    }

    /**
     * Getter.
     * 
     * @return the attribute
     */
    public final String getAttribute() {
        return this.attribute;
    }

    /**
     * Getter.
     * 
     * @return the floor as a mesh
     */
    public final Mesh getMesh() {
        return this.mesh;
    }

    /**
     * Setter.
     * 
     * @param type
     *            the attribute of the Floor
     */
    public final void setAttribute(final String type) {
        this.attribute = type;
    }

    /**
     * Write the floor as a mesh in a STL file.
     * 
     * @param fileName
     *            the name of the file to write in
     */
    public final void writeSTL(final String fileName) {
        this.mesh.writeSTL(fileName);
    }
}
