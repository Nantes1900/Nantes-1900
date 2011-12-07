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
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;

import javax.swing.JTree;
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
    
    private ActionListener showActionListener;

    public IsletTreeController(IsletProcessController parentControllerIn) {
        this.parentController = parentControllerIn;
        this.itView = new IsletTreeView();
        this.buildTreeView();
        this.addTreeController();
        this.hideActionListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                DefaultMutableTreeNode node;
                System.out.println("hide !");
                for (TreePath tp:IsletTreeController.this.itView.getTree().getSelectionPaths())
                {
                   node = (DefaultMutableTreeNode)tp.getLastPathComponent();
                   if (node.getUserObject() instanceof Surface) {
                       IsletTreeController.this.parentController.getU3DController()
                        .hideSurface((Surface)node.getUserObject());
                    }
                }
                IsletTreeController.this.itView.getTree().removeSelectionInterval(
                        0, IsletTreeController.this.itView.getTree().getMaxSelectionRow());
                HomeMadeTreeListener hmtl = (HomeMadeTreeListener)(IsletTreeController.this.itView
                        .getTree().getMouseListeners()[0]);
                hmtl.refresh3DSelection();
            }
        };
        this.showActionListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                DefaultMutableTreeNode node;
                System.out.println("show !");
                for (TreePath tp:IsletTreeController.this.itView.getTree().getSelectionPaths())
                {
                   node = (DefaultMutableTreeNode)tp.getLastPathComponent();
                   if (node.getUserObject() instanceof Surface) {
                       IsletTreeController.this.parentController.getU3DController()
                        .showSurface((Surface)node.getUserObject());
                    }
                }
                IsletTreeController.this.itView.getTree().removeSelectionInterval(
                        0, IsletTreeController.this.itView.getTree().getMaxSelectionRow());
                HomeMadeTreeListener hmtl = (HomeMadeTreeListener)(IsletTreeController.this.itView
                        .getTree().getMouseListeners()[0]);
                hmtl.refresh3DSelection();
            }
        };
    }

    public void addTreeController() {
        MouseListener[] mliste = this.itView.getTree().getMouseListeners();
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
                    if (event.getModifiersEx() == CTRL_DOWN_MASK){
                        this.tree.addSelectionPath(path);
                        System.out.println("added");
                    }
                    else{
                        this.tree.removeSelectionInterval(0, this.tree.getMaxSelectionRow());
                        this.tree.addSelectionPath(path);
                        System.out.println("all removed and one added");
                    }
                    if (event.getButton() == MouseEvent.BUTTON3){
                        this.manageCMenu(event);
                    }
                }
                }
            else{
                this.tree.removeSelectionInterval(0, this.tree.getMaxSelectionRow());
                System.out.println("all removed");
            }
            this.refresh3DSelection();
        }
        
        private void manageCMenu(MouseEvent event){
            System.out.println("cmenu");
            if (this.tree.isSelectionEmpty()){
                IsletTreeController.this.itView.disableHide();
            }
            else{
                IsletTreeController.this.itView.setHideListener(
                        IsletTreeController.this.hideActionListener);
                IsletTreeController.this.itView.setShowListener(
                        IsletTreeController.this.showActionListener);
                //IsletTreeController.this.itView.enableHide();
                IsletTreeController.this.itView.getJpm().show(
                    IsletTreeController.this.itView, event.getX(), event.getY());
            }
        }

        
        private void refresh3DSelection(){
            IsletTreeController.this.getParentController()
            .getU3DController().deselectEverySurfaces();

            for (TreePath tp:IsletTreeController.this.itView.getTree().getSelectionPaths()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
                if (node.getUserObject() instanceof Surface) {
                    IsletTreeController.this
                            .getParentController()
                            .getU3DController()
                            .selectOrUnselectSurfaceFromTree(
                                    (Surface) node.getUserObject());
                } else {
                    // TODO : throw exception if it is not a mesh.
                }
            }
        }
        
    }
}
