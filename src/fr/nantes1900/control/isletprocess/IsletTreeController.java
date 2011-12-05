/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.IsletTreeView;

/**
 * @author Luc Jallerat, Camille Bouquet
 */
public class IsletTreeController {

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
        this.hideActionListener = new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                System.out.println("hide !");
            }
        };
        this.addContextuelMenu();
    }

    public void addTreeController() {
        this.itView.getTree().addTreeSelectionListener(
                new TreeSelectionListener() {

                    @Override
                    public void valueChanged(final TreeSelectionEvent e) {
                        IsletTreeController.this.getParentController()
                                .getU3DController().deselectEverySurfaces();

                        for (Object o : e.getPath().getPath()) {
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;

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
                });
        // FIXME : what if a object is deselected in the tree ? What if more
        // than ONE object is selected ?
    }
    
    public void addContextuelMenu(){
        this.itView.getTree().addMouseListener(new MouseAdapter(){
            public void mouseReleased(MouseEvent event){
                    if(event.getButton() == MouseEvent.BUTTON3)
                {   
                        if (IsletTreeController.this.itView.getTree().isSelectionEmpty()){
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
            });
    }

    private void buildTreeView() {
        try {
            this.itView.buildTree(this.parentController.getBiController()
                    .returnNode());
        } catch (InvalidCaseException e) {
            // TODO Auto-generated catch block
            System.out.println(FileTools.readInformationMessage(
                    TextsKeys.KEY_RETURNNODE, TextsKeys.MESSAGETYPE_MESSAGE));
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
        this.addContextuelMenu();
    }
}
