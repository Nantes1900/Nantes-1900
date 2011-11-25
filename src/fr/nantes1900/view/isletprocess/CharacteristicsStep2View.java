package fr.nantes1900.view.isletprocess;

import javax.swing.JComboBox;

import fr.nantes1900.constants.Characteristics;
import fr.nantes1900.view.components.HelpButton;

public class CharacteristicsStep2View extends CharacteristicsView
{
    /**
     * default serial UID
     */
    private static final long serialVersionUID = 1L;
    private JComboBox<String> cbType;
    
    public CharacteristicsStep2View()
    {
        super();
        String[] types = {"", Characteristics.TYPE_BUILDING, Characteristics.TYPE_GROUND};
        
        cbType = new JComboBox<String>(types);
        this.addCaracteristic(createSimpleCaracteristic(cbType, "Type", new HelpButton()));
        this.bValidate.setEnabled(true);
    }

    public String getTypeSelected()
    {
        return (String) cbType.getSelectedItem();
    }
    

    public void setType(String string)
    {
        this.cbType.setSelectedItem(string);
    }
}
