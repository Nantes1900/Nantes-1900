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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import fr.nantes1900.constants.Icones;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.view.components.HelpButton;

/**
 * Basic view of a characteristics panel with no elements inside and a disabled
 * validate button.
 * @author Camille Bouquet
 * @author Luc Jallerat
 */
public class CharacteristicsView extends JPanel {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Validation button.
     */
    protected JButton bValidate;
    /**
     * Panels which contains characteristics elements such as labels, combo
     * boxes, help buttons, check boxes and so on.
     */
    protected JPanel pContent;

    /**
     * Creates a new empty characteristics panel without any elements in it
     * except a title and a disabled validate button.
     */
    public CharacteristicsView() {
        this.setBorder(new TitledBorder(BorderFactory
                .createRaisedSoftBevelBorder(), TextsKeys.KEY_CHARACTERISTICS));
        this.bValidate = new JButton(new ImageIcon(Icones.validate));
        this.bValidate.setToolTipText(TextsKeys.KEY_TOVALID);

        // Disables the validate button for this empty characteristic panel
        this.bValidate.setEnabled(false);

        this.setMinimumSize(new Dimension(0, 100));
        this.setPreferredSize(new Dimension(0, 100));

        this.pContent = new JPanel();
        FlowLayout contentLayout = new FlowLayout();
        contentLayout.setAlignment(FlowLayout.LEFT);
        this.pContent.setLayout(contentLayout);

        this.setLayout(new BorderLayout());
        this.add(this.bValidate, BorderLayout.EAST);
        this.add(this.pContent, BorderLayout.CENTER);
    }

    /**
     * Creates easily a new set of characteristic elements composed of a title,
     * the characteristic element and an help button.
     * @param caracteristic
     *            the characteristic element to add. It can be a JPanel for
     *            complex ones.
     * @param title
     *            the title of this characteristic element.
     * @param helpButton
     *            the help button to indicates what the characteristic means.
     * @return a panel containing the elements well placed.
     */
    protected static JPanel createSimpleCaracteristic(
            final JComponent caracteristic, final JLabel title,
            final HelpButton helpButton) {
        JPanel caracteristicPanel = new JPanel();

        caracteristicPanel.setLayout(new GridBagLayout());
        caracteristicPanel.add(title, new GridBagConstraints(0, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 10, 5, 10), 0, 0));
        caracteristicPanel.add(caracteristic, new GridBagConstraints(1, 0, 1,
                1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 10, 5, 10), 0, 0));
        caracteristicPanel.add(helpButton, new GridBagConstraints(2, 0, 1, 1,
                0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 10), 0, 0));

        return caracteristicPanel;
    }

    /**
     * Adds a new panel to the content pane which corresponds to a
     * characteristic set.
     * @param caracteristicPanel
     *            the characteristic panel to add.
     */
    protected final void addCaracteristic(final JPanel caracteristicPanel) {
        this.pContent.add(caracteristicPanel);
    }

    /**
     * Gets the validate button
     * @return the validate button
     */
    public final JButton getValidateButton() {
        return this.bValidate;
    }

    /**
     * Gets the content panel of this characteristics panel.
     * @return the content panel.
     */
    protected JPanel getPanelContent() {
        return this.pContent;
    }
}
