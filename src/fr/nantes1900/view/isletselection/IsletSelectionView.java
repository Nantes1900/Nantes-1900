/**
 * 
 */
package fr.nantes1900.view.isletselection;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;

import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.PFrame;
import fr.nantes1900.view.display3d.Universe3DView;

/**
 * @author Camille
 */
public class IsletSelectionView extends PFrame
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The panel containing the buttons to perform the different actions.
     */
    private ActionsView aView;

    /**
     * The tree allowing to select an islet.
     */
    private GlobalTreeView gtView;

    /**
     * The 3D view of the islet.
     */
    private Universe3DView u3DView;

    /**
     * Creates a new frame to select an islet and launch the treatment.
     * @param actionsView
     *            The panel containing the buttons to perform the different
     *            actions.
     * @param globalTreeView
     *            The tree allowing to select an islet.
     * @param buildingsIsletView
     *            TODO.
     * @todo Handle the size issues.
     */
    public IsletSelectionView(final ActionsView actionsView,
            final GlobalTreeView globalTreeView,
            final Universe3DView buildingsIsletView)
    {
        super();
        // initializes the frame
        this.setTitle("Nantes 1900");
        this.setMinimumSize(new Dimension(600, 600));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // gets the view to add
        this.aView = actionsView;
        this.gtView = globalTreeView;
        this.u3DView = buildingsIsletView;

        // adds the different views
        this.getComponentsPanel().setLayout(new GridBagLayout());
        this.getComponentsPanel().add(
                this.aView,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL,
                        new Insets(5, 10, 5, 10), 0, 0));
        this.getComponentsPanel().add(
                this.gtView,
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(5, 10, 5, 10), 0, 0));
        this.getComponentsPanel().add(
                this.u3DView,
                new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(5, 10, 5, 10), 0, 0));
        this.setStatusBarText(FileTools
                .readHelpMessage(FileTools.KEY_IS_OPENDIRECTORY,
                        FileTools.MESSAGETYPE_STATUSBAR));
        pack();
    }
}
