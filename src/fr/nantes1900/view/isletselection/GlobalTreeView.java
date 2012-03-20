package fr.nantes1900.view.isletselection;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Camille Bouquet, Luc Jallerat
 */
public class GlobalTreeView extends JPanel {

	/**
	 * Version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The JTree displaying the tree.
	 */
	private JTree tree;

	/**
	 * The ScrollPane containing the tree.
	 */
	private JScrollPane spTree;

	/**
	 * Empty constructor.
	 */
	public GlobalTreeView() {
		this.setLayout(new BorderLayout());
		this.spTree = new JScrollPane();
		this.add(this.spTree, BorderLayout.CENTER);
	}

	/**
	 * From a directory, builds the JTree of the contained STL files, and
	 * displays it.
	 * 
	 * @param newDirectory
	 *            the name of the directory
	 */
	public final void displayDirectory(final File newDirectory) {
		FileNode newDirectoryNode = new FileNode(newDirectory);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(
				newDirectoryNode);
		this.fillMyTree(root, newDirectoryNode);

		this.tree = new JTree(root);
		this.spTree.setViewportView(this.tree);
	}

	/**
	 * Builds a tree from the content of the directory : if there is other
	 * directory, calls this method, and if there is STL files, adds it to the
	 * tree.
	 * 
	 * @param root
	 *            the treeNode where will be returned the tree
	 * @param newDirectoryNode
	 *            the directory to explore
	 */
	public final void fillMyTree(final DefaultMutableTreeNode root,
			final FileNode newDirectoryNode) {
		// To improve to show only folders containing .stl files
		// Maybe with a return node to add only if it is a file or is a valid folder
		if (newDirectoryNode.listFiles() != null) {
			List<File> childrenFiles = Arrays.asList(newDirectoryNode
					.listFiles());

			for (File file : childrenFiles) {
				FileNode currentNode = new FileNode(file);
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(
						currentNode);

				// If it is a directory, search for others STL files inside of
				// it.
				if (file.isDirectory()) {
					root.add(child);
					this.fillMyTree(child, currentNode);

					// Displays the file in the tree only if it is a STL file.
				} else if (currentNode.toString().endsWith("stl")) {
					root.add(child);
				}
			}
		}
	}

	/**
	 * Getter.
	 * 
	 * @return the JTree
	 */
	public final JTree getTree() {
		return this.tree;
	}

	/**
	 * Implements an extension of the File with an overriden method toString
	 * used to display correctly the name of the file in the JTree.
	 * 
	 * @author Luc Jallerat
	 */
	public class FileNode extends File {

		/**
		 * Serial version UID.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 * 
		 * @param file
		 *            the file object
		 */
		public FileNode(final File file) {
			super(file.getAbsolutePath());
		}

		/**
		 * Returns the complete name of the file.
		 * 
		 * @return the name of the file
		 */
		public final String getEntireName() {
			return super.toString();
		}

		/**
		 * Returns the short name of the file to display it in the JTree.
		 * 
		 * @return the short name of the file
		 */
		@Override
		public final String toString() {
			return super.getName();
		}
	}
}
