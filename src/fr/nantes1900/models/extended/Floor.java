package fr.nantes1900.models.extended;

import fr.nantes1900.models.Mesh;

public class Floor {

    private String attribute;
    private Mesh mesh = new Mesh();

    /**
     * Constructor. Build the Floor with an attribute as a String.
     * @param type
     *            the attribute
     */
    public Floor(String type) {
        super();
        this.attribute = type;
    }

    /**
     * Build a floor from a mesh, by computing the algorithms.
     * @param m
     *            the mesh to convert
     */
    public void buildFromMesh(Mesh m) {
        this.mesh = new Mesh(m);

        this.decimate(this.mesh);
    }

    /**
     * Decimate the floor.
     * @param floor
     *            the floor to decimate as a mesh
     */
    public void decimate(Mesh floor) {
        // FIXME
    }

    /**
     * Getter.
     * @return the attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Getter.
     * @return the floor as a mesh
     */
    public Mesh getMesh() {
        return mesh;
    }

    /**
     * Setter
     * @param attribute
     *            the attribute of the Floor
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void writeSTL(String fileName) {
        this.mesh.writeSTL(fileName);
    }
}
