/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import fr.nantes1900.constants.Characteristics;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * Characteristics panel for the 3rd step of an islet process when one or more
 * surfaces are selected.
 * @author Camille Bouquet
 * @author Luc Jallerat
 */
public class CharacteristicsStep3ElementsView extends CharacteristicsView {

    /**
     * Default serial UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Combo box to set the type of selected surfaces. The two types are :
     * buildings
     * and noise.
     */
    private JComboBox<String> cbType;

    /**
     * Creates a new panel to display and modify step 3 characteristics for one
     * or more surfaces.
     */
    public CharacteristicsStep3ElementsView() {
        super();
        String[] types = {"", Characteristics.TYPE_BUILDING,
                Characteristics.TYPE_NOISE};

        this.cbType = new JComboBox<String>(types);
        this.cbType.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                CharacteristicsStep3ElementsView.this.checkTypeSelected();
            }

        });
        this.addCaracteristic(createSimpleCaracteristic(this.cbType,
                new JLabel(FileTools.readElementText(TextsKeys.KEY_TYPETEXT)),
                new HelpButton()));
        this.bValidate.setEnabled(true);
    }

    /**
     * Checks the selected type and enables or disables the validate button.
     */
    private void checkTypeSelected() {
        if (cbType.getSelectedItem().equals(""))
        {
            bValidate.setEnabled(false);
        } else
        {
            bValidate.setEnabled(true);
        }
    }

    /**
     * Gets the selectted type.
     * @return the selected type
     */
    public final String getTypeSelected() {
        return (String) this.cbType.getSelectedItem();
    }

    /**
     * Selects the given type.
     * @param string
     *            the new type to select
     */
    public final void setType(final String string) {
        this.cbType.setSelectedItem(string);
        checkTypeSelected();
    }
}
