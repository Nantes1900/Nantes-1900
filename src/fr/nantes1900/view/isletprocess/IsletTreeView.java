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
@SuppressWarnings("serial")
public class IsletTreeView extends JPanel {

    private JPopupMenu jpm = new JPopupMenu();
    private JMenuItem mHide = new JMenuItem(TextsKeys.KEY_HIDEITEM);
    private JMenuItem mShow = new JMenuItem(TextsKeys.KEY_SHOWITEM);
    
    private JTree tree;
    private JScrollPane spTree;

    public IsletTreeView() {
        this.setLayout(new BorderLayout());
        this.spTree = new JScrollPane();
        this.add(this.spTree, BorderLayout.CENTER);
        this.addItemsToJpm();
    }

    public void buildTree(DefaultMutableTreeNode root) {
        this.tree = new JTree(root);
        this.spTree.setViewportView(this.tree);
    }

    public JTree getTree() {
        return this.tree;
    }
    
    private void addItemsToJpm(){
        this.jpm.add(this.mHide);
        this.jpm.add(this.mShow);
    }
    
    public void setHideListener(ActionListener al){
        if (this.mHide.getActionListeners().length>0){
            this.mHide.removeActionListener(this.mHide.getActionListeners()[0]);
        }
        this.mHide.addActionListener(al);
    }
    
    public void setShowListener(ActionListener al){
        if (this.mShow.getActionListeners().length>0){
            this.mShow.removeActionListener(this.mShow.getActionListeners()[0]);
        }
        this.mShow.addActionListener(al);
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
    
    public void enableShow(){
        this.mShow.setEnabled(true);
    }
    
    public void disableShow(){
        this.mShow.setEnabled(false);
    }
}
