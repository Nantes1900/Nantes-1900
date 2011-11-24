/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import javax.swing.JComboBox;

import fr.nantes1900.constants.Characteristics;
import fr.nantes1900.view.components.HelpButton;


/**
 * @author Camille
 *
 */
public class CharacteristicsStep3TrianglesView extends CharacteristicsView
{
    /**
     * default serial UID
     */
    private static final long serialVersionUID = 1L;
    private JComboBox<String> cbType;
    
    public CharacteristicsStep3TrianglesView()
    {
        super();
        String[] types = {""};
        
        cbType = new JComboBox<String>(types);
        this.addCaracteristic(createSimpleCaracteristic(cbType, "Appartient à", new HelpButton()));
        this.bValidate.setEnabled(true);
    }

    public String getElementSelected()
    {
        return (String) cbType.getSelectedItem();
    }
    

    public void setElement(String string)
    {
        this.cbType.setSelectedItem(string);
    }
}
