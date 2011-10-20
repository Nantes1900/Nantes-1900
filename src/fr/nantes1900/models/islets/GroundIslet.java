package fr.nantes1900.models.islets;

import fr.nantes1900.models.middle.TriangleMesh;

/**
 * Implements a ground as a mesh with an associated String attribute.
 * 
 * @author Daniel Lefevre
 */
public class GroundIslet extends AbstractIslet {

    /**
     * The mesh describing the surface.
     */
    private TriangleMesh triangleMesh = new TriangleMesh();

    /**
     * Constructor. Builds the Ground with an attribute as a String.
     * 
     * @param type
     *            the attribute
     */
    public GroundIslet(TriangleMesh m) {
	super(m);
    }

    /**
     * Builds a ground from a mesh, by computing the algorithms.
     * 
     * @param m
     *            the mesh to convert
     */
    public final void buildFromMesh(final TriangleMesh m) {
	this.triangleMesh = new TriangleMesh(m);

	this.decimate(this.triangleMesh);
    }

    /**
     * Decimates the ground. Not implemented.
     * 
     * @param ground
     *            the ground to decimate as a mesh
     */
    public void decimate(final TriangleMesh ground) {
	// TODO : implement this method.
    }

    /**
     * Getter.
     * 
     * @return the ground as a mesh
     */
    public final TriangleMesh getMesh() {
	return this.triangleMesh;
    }

    /**
     * Writes the ground as a mesh in a STL file.
     * 
     * @param fileName
     *            the name of the file to write in
     */
    public final void writeSTL(final String fileName) {
	this.triangleMesh.writeSTL(fileName);
    }

    /**
     * Treats the files of grounds which are in the directory. Creates Ground
     * objects for each files, puts an attribute, and calls the buildFromMesh
     * method of Ground. Then adds it to the list of grounds.
     * 
     * @param directoryName
     *            the directory name to find the grounds.
     */
    public void treatGrounds(final String directoryName) {
	// TODO
    }
}
