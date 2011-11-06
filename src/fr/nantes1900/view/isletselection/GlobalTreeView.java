/**
 * 
 */
package fr.nantes1900.view.isletselection;

import java.io.File;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Camille
 */
public class GlobalTreeView
{
    private JTree tree;

    /**
     * Empty constructor.
     */
    public GlobalTreeView()
    {
        this.displayDirectory(new File("C:\\"));
    }

    public void displayDirectory(File newDirectory)
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
                newDirectory.toString());
        for (File file : newDirectory.listFiles())
        {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(file);
            root.add(child);
        }
        this.tree = new JTree(root);
    }

    public JTree getTree()
    {
        return this.tree;
    }
}
