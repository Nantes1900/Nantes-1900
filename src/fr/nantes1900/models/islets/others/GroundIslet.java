package fr.nantes1900.models.islets.others;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.islets.AbstractIslet;

/**
 * Implements a ground as a mesh with an associated String attribute.
 * @author Daniel Lefevre
 */
public class GroundIslet extends AbstractIslet
{

    /**
     * Constructor. Builds the Ground with an attribute as a String.
     * @param m
     *            TODO.
     */
    public GroundIslet(final Mesh m)
    {
        super(m);
    }

    /**
     * Builds a ground from a mesh, by computing the algorithms.
     * @param m
     *            the mesh to convert
     */
    public final void buildFromMesh(final Mesh m)
    {
        this.setInitialTotalMesh(new Mesh(m));

        this.decimate(this.getInitialTotalMesh());
    }

    /**
     * Decimates the ground. Not implemented.
     * @param ground
     *            the ground to decimate as a mesh
     */
    public void decimate(final Mesh ground)
    {
        // TODO : implement this method.
    }

    /**
     * Getter.
     * @return the ground as a mesh
     */
    public final Mesh getMesh()
    {
        return this.getInitialTotalMesh();
    }

    /**
     * Treats the files of grounds which are in the directory. Creates Ground
     * objects for each files, puts an attribute, and calls the buildFromMesh
     * method of Ground. Then adds it to the list of grounds.
     * @param directoryName
     *            the directory name to find the grounds.
     */
    public void treatGrounds(final String directoryName)
    {
        // TODO
    }

    /**
     * Writes the ground as a mesh in a STL file.
     * @param fileName
     *            the name of the file to write in
     */
    public final void writeSTL(final String fileName)
    {
        this.getInitialTotalMesh().writeSTL(fileName);
    }
}
