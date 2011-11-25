/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

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
    private JCheckBox cbDelete;
    
    public CharacteristicsStep3TrianglesView()
    {
        super();
        String[] types = {""};
        
        cbType = new JComboBox<String>(types);
        this.addCaracteristic(createSimpleCaracteristic(cbType, "Appartient à", new HelpButton()));
        
        this.cbDelete = new JCheckBox();
        cbDelete.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0)
            {
                if (((JCheckBox) arg0.getSource()).isSelected())
                {
                    cbType.setEnabled(false);
                } else
                {
                    cbType.setEnabled(true);
                }
            }
            
        });
        this.addCaracteristic(createSimpleCaracteristic(cbDelete, "Supprimer", new HelpButton()));
        
        this.bValidate.setEnabled(true);
    }
    
    public boolean isDeleteSelected()
    {
        return this.cbDelete.isSelected();
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
