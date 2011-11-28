/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
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
    private JCheckBox cbDelete;
    
    public CharacteristicsStep3TrianglesView()
    {
        super();
        
        this.cbDelete = new JCheckBox();
        cbDelete.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0)
            {
                if (((JCheckBox) arg0.getSource()).isSelected())
                {
                    bValidate.setEnabled(true);
                } else
                {
                    bValidate.setEnabled(false);
                }
            }
            
        });
        this.addCaracteristic(createSimpleCaracteristic(cbDelete, FileTools.readElementText(TextsKeys.KEY_DELETETEXT), new HelpButton()));
        
        this.bValidate.setEnabled(true);
    }
    
    public boolean isDeleteSelected()
    {
        return this.cbDelete.isSelected();
    }
}
