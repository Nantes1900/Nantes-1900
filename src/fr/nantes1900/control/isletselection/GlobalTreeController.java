/**
 * 
 */
package fr.nantes1900.control.isletselection;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.exceptions.WeirdResultException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletselection.GlobalTreeView;

/**
 * @author Camille Bouquet, Luc Jallerat
 */
public class GlobalTreeController {

    /**
     * View of the tree.
     */
    private GlobalTreeView gtView;

    /**
     * The parent controller to give feedback to.
     */
    private IsletSelectionController parentController;

    /**
     * Creates a new controller to handle the tree used to select and view an
     * islet.
     * @param isletSelectionController
     *            the parent controller
     */
    public GlobalTreeController(
            final IsletSelectionController isletSelectionController) {
        this.parentController = isletSelectionController;
        this.gtView = new GlobalTreeView();
    }

    /**
     * Returns the view of the tree associated with this controller.
     * @return the view of the tree.
     */
    public final GlobalTreeView getGlobalTreeView() {
        return this.gtView;
    }

    /**
     * Getter.
     * @return the parent controller
     */
    protected final IsletSelectionController getParentController() {
        return this.parentController;
    }

    /**
     * Updates the root directory.
     * @param newDirectory
     *            The new root directory.
     */
    public final void updateDirectory(final File newDirectory) {
        this.gtView.displayDirectory(newDirectory);
        this.gtView.getTree().addTreeSelectionListener(
                new TreeSelectionListener() {

                    @Override
                    public void valueChanged(final TreeSelectionEvent e) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
                                .getPath().getLastPathComponent();
                        try {
                            GlobalTreeController.this.getParentController()
                                    .displayFile(node);
                        } catch (WeirdResultException e1) {
                            JOptionPane.showMessageDialog(parentController
                                    .getWindow(), e1.getMessage(), FileTools
                                    .readInformationMessage(
                                            TextsKeys.KEY_ERROR_WEIRDRESULT,
                                            TextsKeys.MESSAGETYPE_TITLE),
                                    JOptionPane.ERROR_MESSAGE);
                        } catch (IOException e2) {
                            // TODO : pop-up error
                            e2.printStackTrace();
                        }
                    }
                });
    }
}
