/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.BorderLayout;
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
public class CaracteristicsView extends JPanel
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected JButton bValidate;
    protected JPanel  pContent;

    public CaracteristicsView()
    {
        this.setBorder(new TitledBorder(BorderFactory
                .createRaisedSoftBevelBorder(), "Caractéristiques"));
        bValidate = new JButton("Valider");
        pContent = new JPanel();
        FlowLayout contentLayout = new FlowLayout();
        contentLayout.setAlignment(FlowLayout.LEFT);
        pContent.setLayout(contentLayout);

        this.setLayout(new BorderLayout());
        this.add(bValidate, BorderLayout.EAST);
        this.add(pContent, BorderLayout.CENTER);
    }

    protected JPanel createSimpleCaracteristic(JComponent caracteristic,
            String title, HelpButton helpButton)
    {
        JPanel caracteristicPanel = new JPanel();

        caracteristicPanel.setLayout(new GridBagLayout());
        caracteristicPanel.add(new JLabel(title), new GridBagConstraints(0, 0,
                1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
        caracteristicPanel.add(caracteristic, new GridBagConstraints(1, 0, 1,
                1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
        caracteristicPanel.add(helpButton, new GridBagConstraints(2, 0, 1, 1,
                0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));

        return caracteristicPanel;
    }

    protected void addCaracteristic(JPanel caracteristicPanel)
    {
        this.pContent.add(caracteristicPanel);
    }

    public JButton getValidateButton()
    {
        return this.bValidate;
    }
}
