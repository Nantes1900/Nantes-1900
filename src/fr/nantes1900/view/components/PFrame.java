package fr.nantes1900.view.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Personnalized JFrame with a status bar on the bottom and a different look.
 * @author Camille
 * @author Daniel
 */
public class PFrame extends JFrame {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The status bar placed at the bottom which displays information for the
     * user.
     */
    protected JPanel statusBar;

    /**
     * Panel containing the other components.
     */
    protected JPanel pComponents;

    /**
     * Progress bar
     */
    protected JProgressBar progressBar;

    /**
     * Status text.
     */
    protected JLabel statusBarText;

    /**
     * Creates a new frame with an empty status bar.
     */
    public PFrame() {
        super();
        this.statusBar = new JPanel();
        this.statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        this.statusBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.statusBar.setMinimumSize(new Dimension(0, 40));
        this.statusBarText = new JLabel();
        this.statusBar.add(this.statusBarText);
        this.pComponents = new JPanel();

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(this.statusBar, BorderLayout.SOUTH);
        this.getContentPane().add(this.pComponents, BorderLayout.CENTER);
    }

    /**
     * Returns the main container of this panel.
     * @return the components panel.
     */
    public final JPanel getComponentsPanel() {
        return this.pComponents;
    }

    /**
     * Sets the text to display in the status bar.
     * @param text
     *            The text to display in the status bar.
     */
    public final void setStatusBarText(final String text) {
        this.statusBarText.setText(text);
        this.validate();
        this.repaint();
    }

    /**
     * Shows or hides the progress bar in the status bar.
     * @param show
     *            true - shows the progress bar\nfalse - hides the progress bar
     */
    public final void showProgressBar(final boolean show) {
        if (show)
        {
            this.progressBar = new JProgressBar(0, 100);
            this.statusBar.add(this.progressBar);
        } else
        {
            this.statusBar.remove(this.progressBar);
        }
        this.validate();
        this.repaint();
    }

    /**
     * Updates the value of the progress bar.
     * @param progression
     *            the new value of the progress bar
     */
    public final void updatesProgressBar(final double progression) {
        if (this.progressBar != null)
        {
            this.progressBar.setValue((int) Math.round(progression * 100));
        }
    }
}
