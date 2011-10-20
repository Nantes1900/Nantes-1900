package fr.nantes1900.models.islets;

import fr.nantes1900.models.middle.TriangleMesh;

public class WateryIslet extends AbstractIslet {
    public WateryIslet(TriangleMesh m) {
	super(m);
    }

    /**
     * Treats the files of wateries which are in the directory. Creates Ground
     * objects for each files, puts an attribute : Water, and calls the
     * buildFromMesh method of Ground. Then adds it to the list of wateries.
     * 
     * @param directoryName
     *            the directory name to find the wateries.
     */
    public void treatWateries(final String directoryName) {
	// TODO
    }
}
