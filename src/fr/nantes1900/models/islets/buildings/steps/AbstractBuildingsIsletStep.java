package fr.nantes1900.models.islets.buildings.steps;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.islets.buildings.exceptions.NullArgumentException;
import fr.nantes1900.utils.FileTools;

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
     * @throws NullArgumentException
     *             when an argument needed in the treatment have not been
     *             initialized
     */
    public abstract AbstractBuildingsIsletStep
            launchTreatment() throws NullArgumentException;

    /**
     * Builds a tree node for the JTree.
     * @return a default mutable tree node
     */
    public abstract DefaultMutableTreeNode returnNode();
    
    @Override
    public String toString(){
        return FileTools.readElementText(TextsKeys.KEY_ISLET);
    }
}
