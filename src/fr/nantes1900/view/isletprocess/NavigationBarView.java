/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;

/**
 * @author Camille
 */
public class NavigationBarView extends JPanel
{
    protected JButton bAbort = new JButton(FileTools.readElementText(TextsKeys.KEY_LAUNCHBUTTON));
    protected JButton bLaunch = new JButton(FileTools.readElementText(TextsKeys.KEY_LAUNCHBUTTON));
    protected JButton bBack = new JButton(FileTools.readElementText(TextsKeys.KEY_LAUNCHBUTTON));
    protected JLabel title = new JLabel(FileTools.readElementText(TextsKeys.KEY_LAUNCHBUTTON));
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
