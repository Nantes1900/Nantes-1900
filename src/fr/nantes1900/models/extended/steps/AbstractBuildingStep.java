package fr.nantes1900.models.extended.steps;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.islets.buildings.exceptions.NullArgumentException;

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
     * @throws NullArgumentException
     *             if an argument needed in the treatment has not been
     *             initialized
     */
    public abstract AbstractBuildingStep
            launchTreatment() throws NullArgumentException;

    /**
     * Creates a tree node.
     * @return the tree node
     */
    public abstract DefaultMutableTreeNode returnNode();
}
