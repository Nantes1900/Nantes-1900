/**
 * 
 */
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

/**
 * @author Camille
 */
public class IsletTreeView extends JPanel {

    private JPopupMenu jpm = new JPopupMenu();
    private JMenuItem mHide = new JMenuItem(TextsKeys.KEY_HIDEITEM);
    
    private JTree tree;
    private JScrollPane spTree;

    public IsletTreeView() {
        this.setLayout(new BorderLayout());
        this.spTree = new JScrollPane();
        this.add(this.spTree, BorderLayout.CENTER);
    }

    public void buildTree(DefaultMutableTreeNode root) {
        this.tree = new JTree(root);
        this.spTree.setViewportView(this.tree);
    }

    public JTree getTree() {
        return this.tree;
    }
    
    public void addItemsToJpm(){
        this.jpm.add(this.mHide);
    }
    
    public void setHideListener(ActionListener al){
        this.mHide.addActionListener(al);
    }
    
    public JPopupMenu getJpm(){
        return this.jpm;
    }
    
    public void enableHide(){
        this.mHide.setEnabled(true);
    }
    
    public void disableHide(){
        this.mHide.setEnabled(false);
    }
}
