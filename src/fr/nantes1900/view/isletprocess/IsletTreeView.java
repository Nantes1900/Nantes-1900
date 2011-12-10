package fr.nantes1900.view.isletprocess;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;

/**
 * Implements a tree displayer.
 * @author Luc Jallerat
 */
public class IsletTreeView extends JPanel {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The popup menu.
     */
    private JPopupMenu jpm = new JPopupMenu();
    /**
     * The item hide in the popup menu.
     */
    private JMenuItem mHide = new JMenuItem(FileTools.readElementText(TextsKeys.KEY_HIDEITEM));
    /**
     * The item show in the popup menu.
     */
    private JMenuItem mShow = new JMenuItem(FileTools.readElementText(TextsKeys.KEY_SHOWITEM));

    /**
     * The Jtree to display.
     */
    private JTree tree;
    /**
     * The scroll pane containing the JTree.
     */
    private JScrollPane spTree;

    /**
     * Constructor.
     */
    public IsletTreeView() {
        this.setLayout(new BorderLayout());
        this.spTree = new JScrollPane();
        this.add(this.spTree, BorderLayout.CENTER);
        this.jpm.add(this.mHide);
        this.jpm.add(this.mShow);
    }

    /**
     * Sets up the JTree.
     * @param root
     *            the treeNode to build the JTree
     */
    public final void buildTree(final DefaultMutableTreeNode root) {
        this.tree = new JTree(root);
        this.spTree.setViewportView(this.tree);
    }

    /**
     * Getter.
     * @return the JTree
     */
    public final JTree getTree() {
        return this.tree;
    }

    /**
     * Sets a new ActionListener as a Hide Listener.
     * @param al
     *            the new ActionListener
     */
    public final void setHideListener(final ActionListener al) {
        if (this.mHide.getActionListeners().length > 0) {
            this.mHide.removeActionListener(this.mHide.getActionListeners()[0]);
        }
        this.mHide.addActionListener(al);
    }

    /**
     * Sets a new ActionListener as a Show Listener.
     * @param al
     *            the new ActionListener
     */
    public final void setShowListener(final ActionListener al) {
        if (this.mShow.getActionListeners().length > 0) {
            this.mShow.removeActionListener(this.mShow.getActionListeners()[0]);
        }
        this.mShow.addActionListener(al);
    }

    /**
     * Getter.
     * @return the jPopupMenu
     */
    public final JPopupMenu getJpm() {
        return this.jpm;
    }

    /**
     * Enable the hide button.
     */
    public final void enableHide() {
        this.mHide.setEnabled(true);
    }

    /**
     * Disable the hide button.
     */
    public final void disableHide() {
        this.mHide.setEnabled(false);
    }

    /**
     * Enable the show button.
     */
    public final void enableShow() {
        this.mShow.setEnabled(true);
    }

    /**
     * Disable the show button.
     */
    public final void disableShow() {
        this.mShow.setEnabled(false);
    }
}
