/**
 * 
 */
package fr.nantes1900.view.isletselection;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Camille Bouquet, Luc Jallerat
 */
public class GlobalTreeView extends JPanel
{
    /**
     * Version ID.
     */
    private static final long serialVersionUID = 1L;

    private JTree             tree;
    private JScrollPane       spTree;

    /**
     * Empty constructor.
     */
    public GlobalTreeView()
    {
        this.setLayout(new BorderLayout());
        this.spTree = new JScrollPane();
        this.add(this.spTree, BorderLayout.CENTER);
    }

    public void displayDirectory(File newDirectory)
    {
        FileNode newDirectoryNode = new FileNode(newDirectory);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
                newDirectoryNode);
        this.fillMyTree(root, newDirectoryNode);

        this.tree = new JTree(root);
        this.spTree.setViewportView(this.tree);
    }

    public void fillMyTree(DefaultMutableTreeNode root,
            FileNode newDirectoryNode)
    {
        File[] childrenFiles = newDirectoryNode.listFiles();
        if (childrenFiles.length > 0)
        {
            for (File file : childrenFiles)
            {
                FileNode currentNode = new FileNode(file);
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(
                        currentNode);

                if (file.isDirectory())
                {
                    root.add(child);
                    this.fillMyTree(child, currentNode);
                } else if (currentNode.toString().endsWith("stl"))
                {
                    root.add(child);
                }
            }
        }
    }

    public JTree getTree()
    {
        return this.tree;
    }

    public class FileNode extends File
    {
        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public FileNode(File file)
        {
            super(file.getAbsolutePath());
        }

        public String getEntireName()
        {
            return super.toString();
        }

        @Override
        public String toString()
        {
            return super.getName();
        }
    }
}
