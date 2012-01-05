package fr.nantes1900.models.islets.steps;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.exceptions.NullArgumentException;
import fr.nantes1900.models.exceptions.WeirdResultException;
import fr.nantes1900.utils.FileTools;

/**
 * Abstract the steps of the islet process. Each step must be able to LAUNCH the
 * next process and to return a TreeNode for the JTree.
 * @author Daniel Lefèvre
 */
public abstract class AbstractBuildingsIsletStep {

    /**
     * Launches the process.
     * @return the next step
     * @throws NullArgumentException
     *             when an argument needed in the process have not been
     *             initialized
     * @throws WeirdResultException
     *             if the result is not coherent
     */
    public abstract AbstractBuildingsIsletStep launchProcess()
            throws NullArgumentException, WeirdResultException;

    /**
     * Builds a tree node for the JTree.
     * @return a default mutable tree node
     */
    public abstract DefaultMutableTreeNode returnNode();

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return FileTools.readElementText(TextsKeys.KEY_ISLET);
    }
}
