package fr.nantes1900.view.isletprocess;

import javax.swing.JComboBox;

import fr.nantes1900.view.components.HelpButton;

public class CaracteristicsStep2View extends CaracteristicsView
{
    public CaracteristicsStep2View()
    {
        super();
        String[] types = {"Bâtiment", "Sol"};
        
        JComboBox cbType = new JComboBox(types);
        this.addCaracteristic(createSimpleCaracteristic(cbType, "Type", new HelpButton()));
    }
}
