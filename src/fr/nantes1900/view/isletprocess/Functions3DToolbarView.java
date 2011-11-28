/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JToolBar;


/**
 * @author Camille
 */
public class Functions3DToolbarView extends JToolBar
{
    /**
     * Default generated serial UID.
     */
    private static final long serialVersionUID = 1L;

    public Functions3DToolbarView()
    {
        super(JToolBar.VERTICAL);
        
        add(new JButton("Test"));
        setMinimumSize(new Dimension(30, 0));
    }
}
