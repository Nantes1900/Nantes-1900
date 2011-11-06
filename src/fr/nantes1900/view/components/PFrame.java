/**
 * 
 */
package fr.nantes1900.view.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Personnalized JFrame with a status bar on the bottom and a different look.
 * @author Camille
 */
public class PFrame extends JFrame
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * The status bar placed at the bottom which displays information for the
     * user.
     */
    private JPanel            statusBar;

    /**
     * Panel containing the other components.
     */
    private JPanel            pComponents;

    /**
     * Creates a new frame with an empty status bar.
     */
    public PFrame()
    {
        super();
        statusBar = new JPanel();
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        statusBar.setLayout(new FlowLayout(FlowLayout.RIGHT));

        pComponents = new JPanel();
        
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.getContentPane().add(pComponents, BorderLayout.CENTER);
    }

    public void setStatusBarText(String text)
    {
        this.statusBar.removeAll();
        this.statusBar.add(new JLabel(text));
        this.validate();
        this.repaint();
    }
    
    public JPanel getComponentsPanel()
    {
        return this.pComponents;
    }
}
