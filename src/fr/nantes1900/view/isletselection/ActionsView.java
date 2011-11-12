/**
 * 
 */
package fr.nantes1900.view.isletselection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * @author Camille
 */
public class ActionsView extends JPanel
{

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
     * Checkbox to select gravity normal as ground normal.
     */
    private JCheckBox cbGravityGround;

    /**
     * Creates a new panel containing the open and launch buttons.
     */
    public ActionsView()
    {
        this.bOpen = new JButton("Ouvrir");
        this.bLaunch = new JButton("Lancer");
        this.bLaunch.setEnabled(false);
        this.bHelp = new HelpButton(FileTools.readHelpMessage(
                FileTools.KEY_IS_OPENDIRECTORY, FileTools.MESSAGETYPE_TOOLTIP),
                FileTools.readHelpMessage(FileTools.KEY_IS_OPENDIRECTORY,
                        FileTools.MESSAGETYPE_MESSAGE),
                FileTools.readHelpMessage(FileTools.KEY_IS_OPENDIRECTORY,
                        FileTools.MESSAGETYPE_TITLE));
        this.cbGravityGround = new JCheckBox(
                "Utiliser la normale orientée selon la gravite");

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
        this.add(this.cbGravityGround, new GridBagConstraints(0, 1,
                GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER,
                0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
    }

    /**
     * Returns the help button.
     * @return The help button.
     */
    public final HelpButton getHelpButton()
    {
        return this.bHelp;
    }

    /**
     * Returns the launch islet treatment button.
     * @return The launch button.
     */
    public final JButton getLaunchButton()
    {
        return this.bLaunch;
    }

    /**
     * Returns the open folder button.
     * @return The open button.
     */
    public final JButton getOpenButton()
    {
        return this.bOpen;
    }

    /**
     * TODO.
     * @return TODO.
     */
    public final boolean isGravityGroundCheckBoxSelected()
    {
        return this.cbGravityGround.isSelected();
    }
}
