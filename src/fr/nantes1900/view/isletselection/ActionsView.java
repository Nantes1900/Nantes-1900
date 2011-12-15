package fr.nantes1900.view.isletselection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import fr.nantes1900.constants.Icons;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * Panel containing elements to manage the window.
 * @author Camille Bouquet
 * @author Luc Jallerat
 */
public class ActionsView extends JPanel {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Button used to OPEN a mockup part.
     */
    private JButton bOpen;

    /**
     * Button used to LAUNCH the process of an islet.
     */
    private JButton bLaunch;

    /**
     * Button used to display some help.
     */
    private HelpButton bHelp;

    /**
     * Creates a new panel containing the OPEN and LAUNCH buttons.
     */
    public ActionsView() {

        // Definition of the buttons
        this.bOpen = new JButton(new ImageIcon(Icons.OPEN));
        this.bOpen.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_OPENBUTTON));

        this.bLaunch = new JButton(new ImageIcon(Icons.LAUNCH));
        this.bLaunch.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_LAUNCHBUTTON));
        this.bLaunch.setEnabled(false);

        this.bHelp = new HelpButton(FileTools.readHelpMessage(
                TextsKeys.KEY_IS_OPENDIRECTORY, TextsKeys.MESSAGETYPE_TOOLTIP),
                FileTools.readHelpMessage(TextsKeys.KEY_IS_OPENDIRECTORY,
                        TextsKeys.MESSAGETYPE_MESSAGE),
                FileTools.readHelpMessage(TextsKeys.KEY_IS_OPENDIRECTORY,
                        TextsKeys.MESSAGETYPE_TITLE));

        // Layout
        this.setLayout(new GridBagLayout());
        this.add(this.bOpen, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 10, 5, 10), 0, 0));
        this.add(this.bLaunch, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 10, 5, 10), 0, 0));
        this.add(bHelp, new GridBagConstraints(2, 0,
                GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 10, 5, 10), 0, 0));
    }

    /**
     * Returns the help button.
     * @return The help button.
     */
    public final HelpButton getHelpButton() {
        return this.bHelp;
    }

    /**
     * Returns the LAUNCH islet process button.
     * @return The LAUNCH button.
     */
    public final JButton getLaunchButton() {
        return this.bLaunch;
    }

    /**
     * Returns the OPEN folder button.
     * @return The OPEN button.
     */
    public final JButton getOpenButton() {
        return this.bOpen;
    }
}
