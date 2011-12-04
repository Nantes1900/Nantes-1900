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
        this.setMinimumSize(new Dimension(100, 50));
        //Tooltips
        this.bLaunch.setToolTipText(
                FileTools.readElementText(TextsKeys.KEY_LAUNCHBUTTON));
        this.bBack.setToolTipText(FileTools.readElementText(TextsKeys.KEY_BACKBUTTON));
        this.bAbort.setToolTipText(FileTools.readElementText(TextsKeys.KEY_ABORTBUTTON));
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

    public void refreshStepTitle(int i) {
        this.title.setText(FileTools.readElementText(TextsKeys.KEY_PROCESSTITLE
                + i));
        this.title.repaint();
        if (i==1){
            this.bBack.setEnabled(false);
        }
        else {
            this.bBack.setEnabled(true);
        }
        if (i==7){
            this.bLaunch.setEnabled(false);
        }
        else {
            this.bLaunch.setEnabled(true);
        }
    }
}
