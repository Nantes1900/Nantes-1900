/**
 * 
 */
package fr.nantes1900.view;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * @author Camille
 */
public class IsletSelectionView extends JFrame
{
    /**
     * Creates a new frame to select an islet and launch the treatment.
     * @todo Handle the size issues.
     */
    public IsletSelectionView()
    {
        this.setTitle("Nantes 1900");
        this.setMinimumSize(new Dimension(600, 600));
    }
}
