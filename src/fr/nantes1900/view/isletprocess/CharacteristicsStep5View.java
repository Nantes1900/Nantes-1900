package fr.nantes1900.view.isletprocess;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import fr.nantes1900.view.components.HelpButton;

public class CharacteristicsStep5View extends CharacteristicsView
{
    /**
     * default serial UID
     */
    private static final long serialVersionUID = 1L;
    private JCheckBox cbMerge;
    private JCheckBox cbNoise;
    
    public CharacteristicsStep5View()
    {
        super();
        this.cbMerge = new JCheckBox();
        cbMerge.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0)
            {
                if (((JCheckBox) arg0.getSource()).isSelected())
                {
                    cbNoise.setEnabled(false);
                } else
                {
                    cbNoise.setEnabled(true);
                }
            }
            
        });
        this.addCaracteristic(createSimpleCaracteristic(cbMerge, "Fusionner", new HelpButton()));
        this.cbNoise = new JCheckBox();
        cbNoise.addItemListener(new ItemListener(){
            
            @Override
            public void itemStateChanged(ItemEvent arg0)
            {
                if (((JCheckBox) arg0.getSource()).isSelected())
                {
                    cbMerge.setEnabled(false);
                    bValidate.setEnabled(true);
                } else
                {
                    cbMerge.setEnabled(true);
                    bValidate.setEnabled(false);
                }
            }
            
        });
        this.addCaracteristic(createSimpleCaracteristic(cbNoise, "Mettre dans le bruit", new HelpButton()));
        this.bValidate.setEnabled(true);
    }

    public boolean isMergeSelected()
    {
        return this.cbMerge.isSelected();
    }

    public boolean isNoiseSelected()
    {
        return this.cbNoise.isSelected();
    }
}
