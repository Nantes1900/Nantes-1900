package fr.nantes1900.models.extended.steps;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.exceptions.NullArgumentException;

/**
 * Abstract class describing each building step. Each of these steps must be
 * able to launche the next process and to return a tree node of their content.
 * @author Daniel
 */
public abstract class AbstractBuildingStep {

    /**
     * Launches the next process.
     * @return the next step
     * @throws NullArgumentException
     *             if an argument needed in the process has not been initialized
     */
    public abstract AbstractBuildingStep launchProcess()
            throws NullArgumentException;

    /**
     * Creates a tree node.
     * @param counter
     *            the counter of the building
     * @return the tree node
     */
    public abstract DefaultMutableTreeNode returnNode(int counter);
}
