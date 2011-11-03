/**
 * 
 */
package fr.nantes1900.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;

/**
 * @author Camille
 */
public class IsletSelectionView extends JFrame
{
    /**
     * The panel containing the buttons to perform the different actions.
     */
    private ActionsView aView;

    /**
     * The tree allowing to select an islet.
     */
    private GlobalTreeView gtView;
    
    /**
     * Creates a new frame to select an islet and launch the treatment.
     * 
     * @param actionsView 
     *              The panel containing the buttons to perform the different actions.
     * @param globalTreeView 
     *              The tree allowing to select an islet.
     * @todo Handle the size issues.
     */
    public IsletSelectionView(ActionsView actionsView, GlobalTreeView globalTreeView)
    {
        // initializes the frame
        this.setTitle("Nantes 1900");
        this.setMinimumSize(new Dimension(600, 600));
        
        // gets the view to add
        this.aView = actionsView;
        this.gtView = globalTreeView;
        
        // adds the different views
        this.getContentPane().setLayout(new GridBagLayout());
        this.getContentPane().add(aView, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 10, 5, 10), 0, 0));
        this.getContentPane().add(gtView, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 10, 5, 10), 0, 0));
    }
}
