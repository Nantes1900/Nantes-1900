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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.nantes1900.constants.Icones;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.utils.FileTools;

/**
 * @author Camille
 */

public class NavigationBarView extends JPanel {

    protected JButton bAbort = new JButton(
            new ImageIcon(Icones.abort));
    protected JButton bLaunch = new JButton(
            new ImageIcon(Icones.launch));
    protected JButton bBack = new JButton(
            new ImageIcon(Icones.back));
    protected JButton bSave = new JButton(
            new ImageIcon(Icones.save));
    protected JLabel title = new JLabel();
    protected JPanel pCentral = new JPanel();

    public NavigationBarView() {
        //Layout
        this.pCentral.setLayout(new GridBagLayout());
        pCentral.add(this.bBack, new GridBagConstraints(0, 0, 1, 1, 0, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.NONE,
                new Insets(8, 8, 8, 8), 0, 5));
        pCentral.add(this.title, new GridBagConstraints(1, 0, 1, 1, 0, 1,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(8, 8, 8, 8), 0, 5));
        pCentral.add(this.bLaunch, new GridBagConstraints(2, 0, 1, 1, 0, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.NONE,
                new Insets(8, 8, 8, 8), 0, 5));
        this.setLayout(new BorderLayout());
        this.add(bAbort, BorderLayout.WEST);
        this.add(pCentral, BorderLayout.CENTER);
        this.add(bSave, BorderLayout.EAST);
        this.setMinimumSize(new Dimension(100, 50));
        //Tooltips
        this.bLaunch.setToolTipText(
                FileTools.readElementText(TextsKeys.KEY_LAUNCHBUTTON));
        this.bBack.setToolTipText(FileTools.readElementText(TextsKeys.KEY_BACKBUTTON));
        this.bAbort.setToolTipText(FileTools.readElementText(TextsKeys.KEY_ABORTBUTTON));
        this.bSave.setToolTipText(FileTools.readElementText(TextsKeys.KEY_SAVERESULTBUTTON));
        
        //refresh the title and enable the buttons launch
        //and previous process or not
        this.refreshStepTitle(AbstractBuildingsIslet.FIRST_STEP);
    }

    public JButton getAbortButton() {
        return this.bAbort;
    }

    public JButton getBackButton() {
        return this.bBack;
    }

    public JButton getLaunchButton() {
        return this.bLaunch;
    }
    
    public JButton getSaveButton() {
        return this.bSave;
    }

    public void refreshStepTitle(int i) {
        this.title.setText(FileTools.readElementText(TextsKeys.KEY_PROCESSTITLE
                + i));
        this.title.repaint();
        if (i==AbstractBuildingsIslet.FIRST_STEP){
            this.bBack.setEnabled(false);
        }
        else {
            this.bBack.setEnabled(true);
        }
        if (i==AbstractBuildingsIslet.SIXTH_STEP){
            this.bLaunch.setEnabled(false);
            this.bSave.setEnabled(true);
        }
        else {
            this.bLaunch.setEnabled(true);
            this.bSave.setEnabled(false);
        }
    }
}
