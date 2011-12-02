/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import fr.nantes1900.view.components.HelpButton;

/**
 * @author Camille
 */
public class CharacteristicsView extends JPanel {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * TODO .
     */
    protected JButton bValidate;
    /**
     * TODO .
     */
    protected JPanel pContent;

    /**
     * TODO .
     */
    public CharacteristicsView() {
        this.setBorder(new TitledBorder(BorderFactory
                .createRaisedSoftBevelBorder(), "Caractéristiques"));
        this.bValidate = new JButton("Valider");

        // Disables the validate button for this empty characteristic panel
        this.bValidate.setEnabled(false);

        this.setMinimumSize(new Dimension(100, 100));

        this.pContent = new JPanel();
        FlowLayout contentLayout = new FlowLayout();
        contentLayout.setAlignment(FlowLayout.LEFT);
        this.pContent.setLayout(contentLayout);

        this.setLayout(new BorderLayout());
        this.add(this.bValidate, BorderLayout.EAST);
        this.add(this.pContent, BorderLayout.CENTER);
    }

    /**
     * TODO .
     * @param caracteristic
     *            TODO .
     * @param title
     *            TODO .
     * @param helpButton
     *            TODO .
     * @return TODO .
     */
    protected static JPanel createSimpleCaracteristic(
            final JComponent caracteristic, final String title,
            final HelpButton helpButton) {
        JPanel caracteristicPanel = new JPanel();

        caracteristicPanel.setLayout(new GridBagLayout());
        caracteristicPanel.add(new JLabel(title), new GridBagConstraints(0, 0,
                1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 10), 0, 0));
        caracteristicPanel.add(caracteristic, new GridBagConstraints(1, 0, 1,
                1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 10, 5, 10), 0, 0));
        caracteristicPanel.add(helpButton, new GridBagConstraints(2, 0, 1, 1,
                0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 10), 0, 0));

        return caracteristicPanel;
    }

    /**
     * TODO .
     * @param caracteristicPanel
     *            TODO .
     */
    protected final void addCaracteristic(final JPanel caracteristicPanel) {
        this.pContent.add(caracteristicPanel);
    }

    /**
     * TODO .
     * @return TODO .
     */
    public final JButton getValidateButton() {
        return this.bValidate;
    }
}
