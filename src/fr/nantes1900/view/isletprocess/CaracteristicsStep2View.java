package fr.nantes1900.view.isletprocess;

import javax.swing.JComboBox;

import fr.nantes1900.view.components.HelpButton;

public class CaracteristicsStep2View extends CaracteristicsView
{
    /**
     * default serial UID
     */
    private static final long serialVersionUID = 1L;
    public static final String TYPE_BUILDING = "Bâtiment";
    public static final String TYPE_GROUND = "Sol";
    private JComboBox<String> cbType;
    
    public CaracteristicsStep2View()
    {
        super();
        String[] types = {TYPE_BUILDING, TYPE_GROUND};
        
        cbType = new JComboBox<String>(types);
        this.addCaracteristic(createSimpleCaracteristic(cbType, "Type", new HelpButton()));
    }

    public String getTypeSelected()
    {
        return (String) cbType.getSelectedItem();
    }
}
