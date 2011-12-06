/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.models.islets.buildings.exceptions.WeirdResultException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.IsletTreeView;

/**
 * @author Luc Jallerat, Camille Bouquet
 */
public class IsletTreeController implements ElementsSelectedListener {

    /**
     * The view displaying the JTree.
     */
    private IsletTreeView itView;

    /**
     * The parent controller.
     */
    private IsletProcessController parentController;

    private ActionListener hideActionListener;

    public IsletTreeController(IsletProcessController parentControllerIn) {
        this.parentController = parentControllerIn;
        this.itView = new IsletTreeView();
        this.buildTreeView();
        this.addTreeController();
        this.hideActionListener = new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                System.out.println("hide !");
                System.out.println(
                        IsletTreeController.this.itView.getTree().getSelectionPaths().length);
            }
        };
    }

    public void addTreeController() {
        MouseListener[] mliste = this.itView.getTree().getMouseListeners();
        //System.out.println(mliste.length);
        this.itView.getTree().removeMouseListener(mliste[0]);
        this.itView.getTree().addMouseListener(new HomeMadeTreeListener(
                this.itView.getTree()));
    }

    private void buildTreeView() {
        try {
            this.itView.buildTree(this.parentController.getBiController()
                    .returnNode());
        } catch (InvalidCaseException e) {
            // TODO Auto-generated catch block
            System.out.println(FileTools.readInformationMessage(
                    TextsKeys.KEY_RETURNNODE, TextsKeys.MESSAGETYPE_MESSAGE));
        } catch (WeirdResultException e) {
            // TODO by Luc
            e.printStackTrace();
        }
    }

    public IsletProcessController getParentController() {
        return this.parentController;
    }

    public IsletTreeView getView() {
        return this.itView;
    }

    public void refreshView() {
        this.buildTreeView();
        this.itView.repaint();
        this.addTreeController();
    }
    
    public void surfaceDeselected(Surface surfaceSelected){
        
    }

    public void surfaceSelected(Surface surfaceSelected){
        
    }

    public void newTrianglesSelection(List<Triangle> trianglesSelected){
        
    }
    
    private class HomeMadeTreeListener extends MouseAdapter{
        JTree tree;
        public HomeMadeTreeListener(JTree tree){
            this.tree = tree;
        }
        @Override
        public void mouseClicked(MouseEvent event) {
            boolean b = false;
            TreePath path = this.tree.getPathForLocation(event.getX(), event.getY());
            if (path != null)
            {
                TreePath[] liste = this.tree.getSelectionPaths();
                for (TreePath tp:liste){
                    b = (b || (tp.equals(path)));
                }
                if (b){
                    this.tree.removeSelectionPath(path);
                    System.out.println("removed");
                }
                else{
                    this.tree.addSelectionPath(path);
                    System.out.println("added");
                    if (event.getButton() == MouseEvent.BUTTON3){
                        this.manageCMenu(event);
                    }
                        
                }
            }
            else{
                this.tree.removeSelectionInterval(0, this.tree.getMaxSelectionRow());
                System.out.println("all removed");
            }
        }
        
        private void manageCMenu(MouseEvent event){
            System.out.println("cmenu");
            if (this.tree.isSelectionEmpty()){
                IsletTreeController.this.itView.disableHide();
            }
            else{
                IsletTreeController.this.itView.setHideListener(
                        IsletTreeController.this.hideActionListener);
                IsletTreeController.this.itView.enableHide();
                IsletTreeController.this.itView.getJpm().show(
                    IsletTreeController.this.itView, event.getX(), event.getY());
            }
        }
    }
}
