package fr.nantes1900.models.extended.steps;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class AbstractBuildingStep
{

    public abstract AbstractBuildingStep launchTreatment();

    public abstract DefaultMutableTreeNode returnNode();
}
