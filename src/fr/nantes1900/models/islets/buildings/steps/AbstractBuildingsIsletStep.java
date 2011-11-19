package fr.nantes1900.models.islets.buildings.steps;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Abstract the steps of the islet treatment. Each step must be able to launch
 * the next treatment and to return a TreeNode for the JTree.
 * @author Daniel Lefèvre
 */
public abstract class AbstractBuildingsIsletStep
{

    /**
     * Launches the treatment.
     * @return the next step
     */
    public abstract AbstractBuildingsIsletStep launchTreatment();

    /**
     * Builds a tree node for the JTree.
     * @return a default mutable tree node
     */
    public abstract DefaultMutableTreeNode returnNode();
}
