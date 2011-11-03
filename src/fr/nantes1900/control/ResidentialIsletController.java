package fr.nantes1900.control;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.islets.AbstractTreatableIslet.UnimplementedException;
import fr.nantes1900.models.islets.ResidentialIslet;
import fr.nantes1900.view.ResidentialIsletView;

public class ResidentialIsletController {

    private ResidentialIsletView residentialIsletView;
    private ResidentialIslet residentialIslet;

    public ResidentialIsletController(ResidentialIslet residentialIsletIn,
	    ResidentialIsletView residentialIsletViewIn) {
	this.residentialIsletView = residentialIsletViewIn;
	this.residentialIslet = residentialIsletIn;
    }

    public ResidentialIsletController() {
    }

    public ResidentialIslet getResidentialIslet() {
	return this.residentialIslet;
    }

    public ResidentialIsletView getResidentialIsletView() {
	return this.residentialIsletView;
    }

    private void incProgression() {
	this.residentialIslet.progression++;
    }

    public void launchNextStep() {
	switch (this.residentialIslet.progression) {
	case 0:
	    try {
		this.residentialIslet.launchStep0();
	    } catch (UnimplementedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    break;
	case 1:
	    try {
		this.residentialIslet.launchStep1();
	    } catch (UnimplementedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    break;
	case 2:
	    try {
		this.residentialIslet.launchStep2();
	    } catch (UnimplementedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    break;
	case 3:
	    this.residentialIslet.launchStep3();
	    break;
	case 4:
	    this.residentialIslet.launchStep4();
	    break;
	case 5:
	    this.residentialIslet.launchStep5();
	    break;
	case 6:
	    this.residentialIslet.launchStep6();
	    break;
	case 7:
	    this.residentialIslet.launchStep7();
	    break;
	case 8:
	    this.residentialIslet.launchStep8();
	    break;
	}
	this.incProgression();
	this.residentialIsletView.refresh();
    }

    // FIXME
    public JTree returnTree() {
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Islet");
	final JTree tree = new JTree(top);

	int buildingNumber = 0;
	for (Building building : this.residentialIslet.getBuildings()) {
	    MutableTreeNode currentBuildingNode = new DefaultMutableTreeNode(
		    "Building" + buildingNumber);
	    top.add(currentBuildingNode);
	    top.add(building.returnTreeNode());
	}

	return tree;
    }
}