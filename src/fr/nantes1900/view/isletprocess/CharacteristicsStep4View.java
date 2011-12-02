package fr.nantes1900.view.isletprocess;

import javax.swing.JComboBox;

import fr.nantes1900.constants.Characteristics;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * TODO .
 * @author Camille Bouquet
 */
public class CharacteristicsStep4View extends CharacteristicsView {

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
    public CharacteristicsStep4View() {
        super();
        String[] types = { "", Characteristics.TYPE_WALL,
                Characteristics.TYPE_ROOF
        };

        this.cbType = new JComboBox<>(types);
        this.addCaracteristic(createSimpleCaracteristic(this.cbType,
                FileTools.readElementText(TextsKeys.KEY_TYPETEXT),
                new HelpButton()));
        this.bValidate.setEnabled(true);
    }

    /**
     * TODO .
     * @return TODO .
     */
    public final String getTypeSelected() {
        return (String) this.cbType.getSelectedItem();
    }

    /**
     * TODO .
     * @param string
     *            TODO .
     */
    public final void setType(final String string) {
        this.cbType.setSelectedItem(string);
    }
}
