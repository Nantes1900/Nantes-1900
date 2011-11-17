/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * @author Camille
 */
public class CaracteristicsView extends JPanel
{
    public CaracteristicsView()
    {

//        JLabel title = new JLabel("Caractéristiques");

        this.setBorder(new TitledBorder(BorderFactory.createRaisedBevelBorder(),
                "Caractéristiques"));
    }
}
