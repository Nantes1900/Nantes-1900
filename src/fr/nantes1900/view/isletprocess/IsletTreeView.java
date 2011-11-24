/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Camille
 */
public class IsletTreeView extends JPanel
{
    private JTree             tree;
    private JScrollPane       spTree;
    
    public IsletTreeView(){
        this.setLayout(new BorderLayout());
        this.spTree = new JScrollPane();
        this.add(this.spTree, BorderLayout.CENTER);
    }
    
    public void buildTree(DefaultMutableTreeNode root){
        this.tree = new JTree(root);
        this.spTree.setViewportView(this.tree);
    }
}
