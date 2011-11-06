package fr.nantes1900.models.islets.others;

import fr.nantes1900.models.islets.AbstractIslet;
import fr.nantes1900.models.middle.Mesh;

/**
 * Implements a special building object as a container of a mesh.
 * @author Daniel Lefevre
 */
public class SpecialBuildingIslet extends AbstractIslet
{

    /**
     * Constructor.
     */
    public SpecialBuildingIslet(Mesh m)
    {
        super(m);
    }

    /**
     * Getter.
     * @return the mesh
     */
    public final Mesh getMesh()
    {
        return this.initialTotalMesh;
    }

    /**
     * Treats the files of special buildings which are in the directory. Puts
     * them as meshes in the specialBuilding list.
     * @param directoryName
     *            the name of the directory where are the files
     */
    public void treatSpecialBuildings(final String directoryName)
    {
        // TODO
    }
}
