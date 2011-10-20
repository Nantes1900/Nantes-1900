package fr.nantes1900.models.islets;

import fr.nantes1900.models.middle.Mesh;

/**
 * Implements a ground as a mesh with an associated String attribute.
 * 
 * @author Daniel Lefevre
 */
public class Ground {

    /**
     * The attribute describing the type of the ground.
     */
    private String attribute;
    /**
     * The mesh describing the surface.
     */
    private Mesh mesh = new Mesh();

    /**
     * Constructor. Builds the Ground with an attribute as a String.
     * 
     * @param type
     *            the attribute
     */
    public Ground(final String type) {
        super();
        this.attribute = type;
    }

    /**
     * Builds a ground from a mesh, by computing the algorithms.
     * 
     * @param m
     *            the mesh to convert
     */
    public final void buildFromMesh(final Mesh m) {
        this.mesh = new Mesh(m);

        this.decimate(this.mesh);
    }

    /**
     * Decimates the ground. Not implemented.
     * 
     * @param ground
     *            the ground to decimate as a mesh
     */
    public void decimate(final Mesh ground) {
        // TODO : implement this method.
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
     * @return the ground as a mesh
     */
    public final Mesh getMesh() {
        return this.mesh;
    }

    /**
     * Setter.
     * 
     * @param type
     *            the attribute of the Ground
     */
    public final void setAttribute(final String type) {
        this.attribute = type;
    }

    /**
     * Writes the ground as a mesh in a STL file.
     * 
     * @param fileName
     *            the name of the file to write in
     */
    public final void writeSTL(final String fileName) {
        this.mesh.writeSTL(fileName);
    }
}
