/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Camille
 */
public class NavigationBarView extends JPanel
{
    protected JButton bAbort = new JButton("Abort");
    protected JButton bLaunch = new JButton("Launch");
    protected JButton bBack = new JButton("Back");
    protected JLabel title = new JLabel("Etape 1 : Désintégration plasma de l'univers mutant");
    protected JPanel pCentral = new JPanel();
    
    public NavigationBarView() {
        this.pCentral.setLayout(new FlowLayout(FlowLayout.CENTER));
        pCentral.add(title);
        pCentral.add(bLaunch);
        this.setLayout(new BorderLayout());
        this.add(bAbort, BorderLayout.WEST);
        this.add(pCentral, BorderLayout.CENTER); 
        this.add(bBack, BorderLayout.EAST);
    }
    
    public JButton getAbortButton(){return this.bAbort;}
    public JButton getLaunchButton(){return this.bLaunch;}
    public JButton getBackButton(){return this.bBack;}
}
