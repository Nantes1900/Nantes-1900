package fr.nantes1900.control.isletprocess;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.InputEvent.SHIFT_DOWN_MASK;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.models.islets.buildings.exceptions.WeirdResultException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.IsletTreeView;

/**
 * @author Luc Jallerat, Camille Bouquet
 */
/**
 * @author Daniel
 */
/**
 * @author Daniel
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

    /**
     * The listener that will listen to the item "hide" from the contextual
     * menu.
     */
    private ActionListener hideActionListener;

    /**
     * The listener that will listen to the item "show" from the contextual
     * menu.
     */
    private ActionListener showActionListener;

    /**
     * Constructor. Sets up the listeners.
     * @param parentControllerIn
     *            the parent controller
     */
    public IsletTreeController(final IsletProcessController parentControllerIn) {

        this.parentController = parentControllerIn;

        this.itView = new IsletTreeView();
        this.buildTreeView();

        this.addTreeController();

        this.hideActionListener = new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                DefaultMutableTreeNode node;

                for (TreePath tp : itView.getTree().getSelectionPaths())
                {
                    node = (DefaultMutableTreeNode) tp.getLastPathComponent();

                    if (node.getUserObject() instanceof Surface)
                    {
                        parentController.getU3DController().hideSurface(
                                (Surface) node.getUserObject());
                    }
                }

                itView.getTree().removeSelectionInterval(0,
                        itView.getTree().getMaxSelectionRow());

                ImprovedTreeListener itL = (ImprovedTreeListener) (IsletTreeController.this.itView
                        .getTree().getMouseListeners()[0]);
                itL.refresh3DSelection();
            }
        };
        this.showActionListener = new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                DefaultMutableTreeNode node;

                for (TreePath tp : itView.getTree().getSelectionPaths())
                {

                    node = (DefaultMutableTreeNode) tp.getLastPathComponent();

                    if (node.getUserObject() instanceof Surface)
                    {
                        parentController.getU3DController().showSurface(
                                (Surface) node.getUserObject());
                    }
                }

                IsletTreeController.this.itView.getTree()
                        .removeSelectionInterval(
                                0,
                                IsletTreeController.this.itView.getTree()
                                        .getMaxSelectionRow());

                ImprovedTreeListener itL = (ImprovedTreeListener) (IsletTreeController.this.itView
                        .getTree().getMouseListeners()[0]);
                itL.refresh3DSelection();
            }
        };
    }

    /**
     * Adds a tree controller.
     */
    public final void addTreeController() {
        MouseListener[] mliste = this.itView.getTree().getMouseListeners();
        this.itView.getTree().removeMouseListener(mliste[0]);
        this.itView.getTree().addMouseListener(
                new ImprovedTreeListener(this.itView.getTree()));
    }

    /**
     * Building the tree by calling the return node method of the
     * BuildingIsletController.
     */
    private void buildTreeView() {
        try
        {
            this.itView.buildTree(this.parentController.getBiController()
                    .returnNode());
        } catch (InvalidCaseException e)
        {
            System.out.println(FileTools.readInformationMessage(
                    TextsKeys.KEY_RETURNNODE, TextsKeys.MESSAGETYPE_MESSAGE));
        }
    }

    /**
     * Getter.
     * @return the parent controller
     */
    public final IsletProcessController getParentController() {
        return this.parentController;
    }

    /**
     * Getter.
     * @return the view associated
     */
    public final IsletTreeView getView() {
        return this.itView;
    }

    /**
     * Refreshes the tree.
     */
    public final void refreshView() {
        this.buildTreeView();
        this.itView.repaint();
        this.addTreeController();
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.listener.ElementsSelectedListener#surfaceDeselected(fr.
     * nantes1900.models.extended.Surface)
     */
    @Override
    public void surfaceDeselected(final Surface surfaceSelected) {
        // Not used.
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.listener.ElementsSelectedListener#surfaceSelected(fr.nantes1900
     * .models.extended.Surface)
     */
    @Override
    public void surfaceSelected(final Surface surfaceSelected) {
        // Not used.
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.listener.ElementsSelectedListener#newTrianglesSelection
     * (java.util.List)
     */
    @Override
    public void newTrianglesSelection(final List<Triangle> trianglesSelected) {
        // Not used.
    }

    /**
     * Implements an improved tree listener : each clicks are listened to
     * manipulate the surfaces correctly.
     * @author Luc Jallerat
     */
    private class ImprovedTreeListener extends MouseAdapter {

        /**
         * The Jtree.
         */
        private JTree tree;

        /**
         * Constructor.
         * @param treeIn
         *            the tree to copy
         */
        public ImprovedTreeListener(final JTree treeIn) {
            this.tree = treeIn;
        }

        /*
         * (non-Javadoc)
         * @see
         * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseClicked(final MouseEvent event) {

            TreePath path = this.tree.getPathForLocation(event.getX(),
                    event.getY());

            if (path != null)
            {
                if (this.contains(path)
                        && event.getButton() == MouseEvent.BUTTON1)
                {
                    // If the path has already been selected.
                    this.tree.removeSelectionPath(path);
                } else
                {
                    if (!this.contains(path))
                    {
                        if ((event.getModifiersEx() & CTRL_DOWN_MASK) == CTRL_DOWN_MASK)
                        {
                            // If CTRL is down, adds the path selected.
                            this.tree.addSelectionPath(path);
                        } else
                        {
                            if (((event.getModifiersEx() & SHIFT_DOWN_MASK) == SHIFT_DOWN_MASK)
                                    & event.getButton() == MouseEvent.BUTTON1)
                            {
                                int leadRow = this.tree.getLeadSelectionRow();
                                if (leadRow != -1)
                                {
                                    this.tree.addSelectionInterval(leadRow,
                                            this.tree.getRowForPath(path));
                                } else
                                {
                                    // If not, deselected every other paths, and
                                    // selects
                                    // only the new one.
                                    this.tree.removeSelectionInterval(0,
                                            this.tree.getMaxSelectionRow());
                                    this.tree.addSelectionPath(path);
                                }
                            } else
                            {
                                // If not, deselected every other paths, and
                                // selects
                                // only the new one.
                                this.tree.removeSelectionInterval(0,
                                        this.tree.getMaxSelectionRow());
                                this.tree.addSelectionPath(path);
                            }
                        }
                    }
                    // If the right button of the mouse have been pressed.
                    if (event.getButton() == MouseEvent.BUTTON3)
                    {
                        this.manageCMenu(event);
                    }
                }
            } else
            {
                // If nothing has been selected, deselected everything.
                this.tree.removeSelectionInterval(0,
                        this.tree.getMaxSelectionRow());
            }

            // If the node has children, expands it or collapses it
            // depending on its state.
            TreePath treepath = this.tree.getClosestPathForLocation(
                    event.getX(), event.getY());
            if (treepath != null)
            {
                if (this.tree.isCollapsed(treepath))
                {
                    this.tree.expandPath(treepath);
                } else
                {
                    this.tree.collapsePath(treepath);
                }
            }

            // Refresh the tree.
            this.refresh3DSelection();
        }

        /**
         * Checks if the tree contains the path.
         * @param path
         *            the path to check
         * @return true if one of the tree selectionPaths has the same reference
         *         as path, false otherwise
         */
        private boolean contains(final TreePath path) {
            TreePath[] liste = this.tree.getSelectionPaths();

            for (TreePath tp : liste)
            {
                if (tp == path)
                {
                    return true;
                }
            }

            return false;
        }

        /**
         * Displays the contextual menu.
         * @param event
         *            The mouseEvent used to give the mouse location to the
         *            contextual menu.
         */
        private void manageCMenu(final MouseEvent event) {
            if (this.tree.isSelectionEmpty())
            {
                IsletTreeController.this.itView.disableHide();
            } else
            {
                IsletTreeController.this.itView
                        .setHideListener(IsletTreeController.this.hideActionListener);
                IsletTreeController.this.itView
                        .setShowListener(IsletTreeController.this.showActionListener);
                IsletTreeController.this.itView.getJpm().show(
                        IsletTreeController.this.itView,
                        event.getX()
                                - IsletTreeController.this.itView
                                        .getScrollPosition().x,
                        event.getY()
                                - IsletTreeController.this.itView
                                        .getScrollPosition().y);
            }
        }

        /**
         * Refreshes the list of the selected objects in the 3D view.
         */
        public void refresh3DSelection() {
            IsletTreeController.this.getParentController().getU3DController()
                    .deselectEverySurfaces();
            IsletTreeController.this.getParentController().getU3DController()
                    .unHighlightEverySurfaces();

            for (TreePath tp : IsletTreeController.this.itView.getTree()
                    .getSelectionPaths())
            {

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp
                        .getLastPathComponent();

                if (node.getUserObject() instanceof Surface)
                {

                    if (IsletTreeController.this.getParentController()
                            .getProgression() == AbstractBuildingsIslet.SIXTH_STEP
                            && IsletTreeController.this.getParentController()
                                    .getU3DController().isLocked())
                    {
                        IsletTreeController.this
                                .getParentController()
                                .getU3DController()
                                .highlightSurface(
                                        (Surface) node.getUserObject());

                    } else
                    {
                        IsletTreeController.this
                                .getParentController()
                                .getU3DController()
                                .selectOrUnselectSurfaceFromTree(
                                        (Surface) node.getUserObject());
                    }
                }
            }
        }

    }
}
