package fr.nantes1900.control;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet.UnimplementedException;
import fr.nantes1900.view.BuildingsIsletView;

public class BuildingsIsletController {

    private BuildingsIsletView isletView;
    private AbstractBuildingsIslet islet;

    public BuildingsIsletController(AbstractBuildingsIslet isletIn,
        BuildingsIsletView isletViewIn) {
        this.isletView = isletViewIn;
        this.islet = isletIn;
    }

    public BuildingsIsletView getIsletView() {
        return this.isletView;
    }

    public AbstractBuildingsIslet getIslet() {
        return this.islet;
    }

    private void incProgression() {
        this.islet.progression++;
    }

    public void launchNextStep() {
        switch (this.islet.progression) {
        case 0:
            try {
                this.islet.launchStep0();
            } catch (UnimplementedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        case 1:
            try {
                this.islet.launchStep1();
            } catch (UnimplementedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        case 2:
            try {
                this.islet.launchStep2();
            } catch (UnimplementedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        case 3:
            this.islet.launchStep3();
            break;
        case 4:
            this.islet.launchStep4();
            break;
        case 5:
            this.islet.launchStep5();
            break;
        case 6:
            this.islet.launchStep6();
            break;
        case 7:
            this.islet.launchStep7();
            break;
        case 8:
            this.islet.launchStep8();
            break;
        }
        this.incProgression();
        this.isletView.refresh();
    }

    public JTree returnTree() {
        DefaultMutableTreeNode topTree = new DefaultMutableTreeNode("Islet");
        final JTree tree = new JTree(topTree);

        int buildingNumber = 0;
        for (Building building : this.islet.getBuildings()) {
            MutableTreeNode currentBuildingNode =
                new DefaultMutableTreeNode("Building" + buildingNumber);
            topTree.add(currentBuildingNode);
            topTree.add(building.returnTree());
        }

        return tree;
    }
}
