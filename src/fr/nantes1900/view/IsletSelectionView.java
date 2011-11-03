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
     * 
     */
    private ActionsView aView;
    /**
     * Creates a new frame to select an islet and launch the treatment.
     * @param actionsView 
     * @todo Handle the size issues.
     */
    public IsletSelectionView(ActionsView actionsView)
    {
        this.setTitle("Nantes 1900");
        this.setMinimumSize(new Dimension(600, 600));
        
        this.aView = actionsView;
        this.getContentPane().setLayout(new GridBagLayout());
        this.getContentPane().add(aView, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 10, 5, 10), 0, 0));
    }
}
