package fr.nantes1900.view.isletselection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import fr.nantes1900.constants.Icones;
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
     * Button used to open a mockup part.
     */
    private JButton bOpen;

    /**
     * Button used to launch the process of an islet.
     */
    private JButton bLaunch;

    /**
     * Button used to display some help.
     */
    private HelpButton bHelp;

    /**
     * Creates a new panel containing the open and launch buttons.
     */
    public ActionsView() {
        // definition of the buttons
        this.bOpen = new JButton(new ImageIcon(Icones.open));
        this.bOpen.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_OPENBUTTON));
        this.bLaunch = new JButton(new ImageIcon(Icones.launch));
        this.bLaunch.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_LAUNCHBUTTON));
        this.bLaunch.setEnabled(false);
        this.bHelp = new HelpButton(FileTools.readHelpMessage(
                TextsKeys.KEY_IS_OPENDIRECTORY, TextsKeys.MESSAGETYPE_TOOLTIP),
                FileTools.readHelpMessage(TextsKeys.KEY_IS_OPENDIRECTORY,
                        TextsKeys.MESSAGETYPE_MESSAGE),
                FileTools.readHelpMessage(TextsKeys.KEY_IS_OPENDIRECTORY,
                        TextsKeys.MESSAGETYPE_TITLE));

        // layout
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
     * Returns the launch islet process button.
     * @return The launch button.
     */
    public final JButton getLaunchButton() {
        return this.bLaunch;
    }

    /**
     * Returns the open folder button.
     * @return The open button.
     */
    public final JButton getOpenButton() {
        return this.bOpen;
    }
}
