package fr.nantes1900.models.islets.others;

import fr.nantes1900.models.islets.AbstractIslet;
import fr.nantes1900.models.middle.Mesh;

public class WateryIslet extends AbstractIslet {
    public WateryIslet(Mesh m) {
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
