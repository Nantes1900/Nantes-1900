/**
 * 
 */
package fr.nantes1900.view.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Personnalized JFrame with a status bar on the bottom and a different look.
 * @author Camille
 */
/**
 * @author Daniel
 */
public class PFrame extends JFrame
{

    /**
     * Version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The status bar placed at the bottom which displays information for the
     * user.
     */
    protected JPanel          statusBar;

    /**
     * Panel containing the other components.
     */
    protected JPanel          pComponents;

    /**
     * Creates a new frame with an empty status bar.
     */
    public PFrame()
    {
        super();
        this.statusBar = new JPanel();
        this.statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        this.statusBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.statusBar.setMinimumSize(new Dimension(0, 40));

        this.pComponents = new JPanel();

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(this.statusBar, BorderLayout.SOUTH);
        this.getContentPane().add(this.pComponents, BorderLayout.CENTER);
    }

    /**
     * Returns the main container of this panel.
     * @return the components panel.
     */
    public final JPanel getComponentsPanel()
    {
        return this.pComponents;
    }

    /**
     * Sets the text to display in the status bar.
     * @param text
     *            The text to display in the status bar.
     */
    public final void setStatusBarText(final String text)
    {
        this.statusBar.removeAll();
        this.statusBar.add(new JLabel(text));
        this.validate();
        this.repaint();
    }
}
