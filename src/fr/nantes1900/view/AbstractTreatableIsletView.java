package fr.nantes1900.view;

import fr.nantes1900.models.islets.AbstractTreatableIslet;

public class AbstractTreatableIsletView {
    protected Universe3D universe3D;
    protected AbstractTreatableIslet abstractTreatableIslet;

    public AbstractTreatableIsletView(Universe3D universe3d) {
	this.universe3D = universe3d;
    }

    public Universe3D getUniverse3D() {
	return this.universe3D;
    }

    public void setUniverse3D(Universe3D universe3DIn) {
	this.universe3D = universe3DIn;
    }

    public void refresh() {
	this.universe3D.clearAllMeshes();

	switch (this.abstractTreatableIslet.progression) {
	case 0:
	    TriangleMeshViewer meshViewer = new TriangleMeshViewer(
		    this.abstractTreatableIslet.getInitialTotalMesh());
	    break;
	case 1:
	    break;
	case 2:
	    break;
	case 3:
	    break;
	case 4:
	    break;
	case 5:
	    break;
	case 6:
	    break;
	case 7:
	    break;
	case 8:
	    break;
	}
	// TODO
    }
}
