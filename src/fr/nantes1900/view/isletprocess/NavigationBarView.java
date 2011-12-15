package fr.nantes1900.view.isletprocess;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.nantes1900.constants.Icons;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * * Implements a navigation bar with buttons to launch a new process, to come
 * back to the previous one, to abort the process, or to save the results. Some
 * informations are also displayed concerning the current step and process.
 * @author Camille Bouquet, Luc Jallerat
 */

public class NavigationBarView extends JPanel {

    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Button abort (the entire process).
     */
    private JButton bAbort = new JButton(new ImageIcon(Icons.abort));
    /**
     * Button launch (the next process).
     */
    private JButton bLaunch = new JButton(new ImageIcon(Icons.launch));
    /**
     * Button to go back (to the previous process).
     */
    private JButton bBack = new JButton(new ImageIcon(Icons.back));
    /**
     * Button save (the final results).
     */
    private JButton bSave = new JButton(new ImageIcon(Icons.save));
    /**
     * The title of the bar, describing the name of the step.
     */
    private JLabel titleStep = new JLabel();
    /**
     * The panel.
     */
    private JPanel pCentral = new JPanel();
    
    /**
     * Help button to explain the current step.
     */
    private HelpButton bHelp = new HelpButton();
    /**
     * Constructor.
     */
    public NavigationBarView() {
        // Layout
        this.pCentral.setLayout(new GridBagLayout());

        this.pCentral.add(this.bBack, new GridBagConstraints(0, 0, 1, 1, 0, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.NONE,
                new Insets(8, 8, 8, 8), 0, 5));

        this.pCentral.add(this.titleStep, new GridBagConstraints(1, 0, 1, 1, 0,
                1, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(8, 8, 8, 8), 0, 5));

        this.pCentral.add(bHelp, new GridBagConstraints(2, 0, 1, 1, 0,
                1, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(8, 8, 8, 8), 0, 2));

        this.pCentral.add(this.bLaunch, new GridBagConstraints(3, 0, 1, 1, 0,
                1, GridBagConstraints.PAGE_START, GridBagConstraints.NONE,
                new Insets(8, 8, 8, 8), 0, 5));

        this.setLayout(new BorderLayout());
        this.add(this.bAbort, BorderLayout.WEST);
        this.add(this.pCentral, BorderLayout.CENTER);
        this.add(this.bSave, BorderLayout.EAST);
        this.setMinimumSize(new Dimension(100, 50));
        
        

        // Tooltips
        this.bLaunch.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_LAUNCHBUTTON));
        this.bBack.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_BACKBUTTON));
        this.bAbort.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_ABORTBUTTON));
        this.bSave.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_SAVERESULTBUTTON));

        // Refreshs the title and the HelpButton and enable the buttons launch
        // and previous process or not
        this.refreshStepTitle(AbstractBuildingsIslet.FIRST_STEP);
    }

    /**
     * Getter.
     * @return the abort button
     */
    public final JButton getAbortButton() {
        return this.bAbort;
    }

    /**
     * Getter.
     * @return the back button
     */
    public final JButton getBackButton() {
        return this.bBack;
    }

    /**
     * Getter.
     * @return the launch button
     */
    public final JButton getLaunchButton() {
        return this.bLaunch;
    }

    /**
     * Getter.
     * @return the save button
     */
    public final JButton getSaveButton() {
        return this.bSave;
    }

    /**
     * Getter.
     * @return the help button
     */
    public final HelpButton getHelpButton() {
        return this.bHelp;
    }

    /**
     * Displays the current step title, depending on the current step.
     * @param i
     *            the number of the step
     */
    public final void refreshStepTitle(final int i) {
        //HelpButton
        this.bHelp.setHelpMessage(
                FileTools.readHelpMessage(
                        (TextsKeys.KEY_HELP_STEP+i),
                        TextsKeys.MESSAGETYPE_MESSAGE),
                FileTools.readHelpMessage(
                        (TextsKeys.KEY_HELP_STEP+i),
                        TextsKeys.MESSAGETYPE_TITLE));
        
        //Tooltips
        this.bHelp.setTooltip(FileTools.readHelpMessage(
                TextsKeys.KEY_HELP_STEP+i, TextsKeys.MESSAGETYPE_TOOLTIP));
        this.bLaunch.setToolTipText(FileTools
                .readElementText(TextsKeys.KEY_PROCESSTITLE + i));
        
        //Title
        this.titleStep.setText(FileTools
                .readElementText(TextsKeys.KEY_STEPTITLE + i));
        this.titleStep.repaint();
        
        //Enable or disable some buttons depending on the step
        if (i == AbstractBuildingsIslet.FIRST_STEP) {
            this.bBack.setEnabled(false);
        } else {
            this.bBack.setEnabled(true);
        }

        if (i == AbstractBuildingsIslet.SIXTH_STEP) {
            this.bLaunch.setEnabled(false);
            this.bSave.setEnabled(true);
        } else {
            this.bLaunch.setEnabled(true);
            this.bSave.setEnabled(false);
        } 
    }
}
