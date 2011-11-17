package fr.nantes1900.models.islets.buildings.steps;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class AbstractBuildingsIsletStep
{

    public abstract AbstractBuildingsIsletStep launchTreatment();

    public abstract DefaultMutableTreeNode returnNode();
}
