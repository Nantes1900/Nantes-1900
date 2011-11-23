package fr.nantes1900.models.extended.steps;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Abstract class describing each building step. Each of these steps must be
 * able to launche the next treatment and to return a tree node of their
 * content.
 * @author Daniel
 */
public abstract class AbstractBuildingStep
{

    /**
     * Launches the next treatment.
     * @return the next step
     */
    public abstract AbstractBuildingStep launchTreatment();

    /**
     * Creates a tree node.
     * @return the tree node
     */
    public abstract DefaultMutableTreeNode returnNode();
}
