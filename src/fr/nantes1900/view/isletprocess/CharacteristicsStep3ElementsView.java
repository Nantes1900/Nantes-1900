/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import javax.swing.JComboBox;

import fr.nantes1900.constants.Characteristics;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * TODO .
 * @author Camille
 */
public class CharacteristicsStep3ElementsView extends CharacteristicsView
{
    /**
     * Default serial UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * TODO .
     */
    private JComboBox<String> cbType;

    /**
     * TODO .
     */
    public CharacteristicsStep3ElementsView()
    {
        super();
        String[] types = {"", Characteristics.TYPE_BUILDING,
                Characteristics.TYPE_NOISE};

        this.cbType = new JComboBox<>(types);
        this.addCaracteristic(createSimpleCaracteristic(this.cbType,
                FileTools.readElementText(TextsKeys.KEY_TYPETEXT),
                new HelpButton()));
        this.bValidate.setEnabled(true);
    }

    /**
     * Getter.
     * @return TODO .
     */
    public final String getTypeSelected()
    {
        return (String) this.cbType.getSelectedItem();
    }

    /**
     * Getter.
     * @param string
     *            TODO .
     */
    public final void setType(final String string)
    {
        this.cbType.setSelectedItem(string);
    }
}
