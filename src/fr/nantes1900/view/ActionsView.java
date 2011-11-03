/**
 * 
 */
package fr.nantes1900.view;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Camille
 *
 */
public class ActionsView extends JPanel
{
    /**
     * Button used to open a mockup part.
     */
    private JButton bOpen;
    
    /**
     * Button used to launch the treatment of an islet.
     */
    private JButton bLaunch;
    
    /**
     * Creates a new panel containing the open and launch buttons.
     */
    public ActionsView()
    {
        bOpen = new JButton("Ouvrir");
        bLaunch = new JButton("Lancer");
        
        this.add(bOpen);
        this.add(bLaunch);
    }
}
