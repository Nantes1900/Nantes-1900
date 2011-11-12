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

    /**
     * TODO.
     */
    private JTree tree;

    /**
     * TODO.
     */
    private JScrollPane spTree;

    /**
     * Empty constructor.
     */
    public GlobalTreeView()
    {
        this.setLayout(new BorderLayout());
        this.spTree = new JScrollPane();
        this.add(this.spTree, BorderLayout.CENTER);
    }

    /**
     * TODO.
     * @param newDirectory
     *            TODO.
     */
    public final void displayDirectory(final File newDirectory)
    {
        FileNode newDirectoryNode = new FileNode(newDirectory);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
                newDirectoryNode);
        this.fillMyTree(root, newDirectoryNode);

        this.tree = new JTree(root);
        this.spTree.setViewportView(this.tree);
    }

    /**
     * TODO.
     * @param root
     *            TODO.
     * @param newDirectoryNode
     *            TODO.
     */
    public final void fillMyTree(final DefaultMutableTreeNode root,
            final FileNode newDirectoryNode)
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

    /**
     * TODO.
     * @return TODO.
     */
    public final JTree getTree()
    {
        return this.tree;
    }

    /**
     * TODO.
     * @author TODO.
     */
    public class FileNode extends File
    {

        /**
         * TODO.
         */
        private static final long serialVersionUID = 1L;

        /**
         * TODO.
         * @param file
         *            TODO.
         */
        public FileNode(final File file)
        {
            super(file.getAbsolutePath());
        }

        /**
         * TODO.
         * @return TODO.
         */
        public final String getEntireName()
        {
            return super.toString();
        }

        @Override
        public final String toString()
        {
            return super.getName();
        }
    }
}
