package fr.nantes1900.view.isletprocess.characteristics;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import fr.nantes1900.constants.Characteristics;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * Characteristics panel for the 2nd step of an islet process.
 * @author Camille Bouquet, Luc Jallerat
 */
public class CharacteristicsStep2View extends CharacteristicsView {

    /**
     * Default serial UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Combo box to set the type of selected triangles. The two types are :
     * buildings and ground.
     */
    private JComboBox<String> cbType;

    /**
     * Check box to delete a list of triangles.
     */
    private JCheckBox cbDelete;

    /**
     * Creates a new panel to display and modify characteristics for step 2.
     */
    public CharacteristicsStep2View() {
        super();
        String[] types = {"", Characteristics.TYPE_BUILDING,
                Characteristics.TYPE_GROUND};

        this.cbType = new JComboBox<>(types);
        this.cbType.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent arg0) {
                CharacteristicsStep2View.this.checkEnableValidateButton();
            }

        });

        // Puts in noise actions
        this.cbDelete = new JCheckBox();
        this.cbDelete.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent arg0) {
                if (((JCheckBox) arg0.getSource()).isSelected())
                {
                    CharacteristicsStep2View.this.cbType.setEnabled(false);
                } else
                {
                    CharacteristicsStep2View.this.cbType.setEnabled(true);
                }
                CharacteristicsStep2View.this.checkEnableValidateButton();
            }

        });

        this.addCaracteristic(createSimpleCaracteristic(
                this.cbType,
                new JLabel(FileTools.readElementText(TextsKeys.KEY_TYPETEXT)),
                new HelpButton(FileTools.readHelpMessage(
                        TextsKeys.KEY_HELP_C_TYPE,
                        TextsKeys.MESSAGETYPE_TOOLTIP), FileTools
                        .readHelpMessage(TextsKeys.KEY_HELP_C_TYPE_TRIANGLES,
                                TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                        .readHelpMessage(TextsKeys.KEY_HELP_C_TYPE,
                                TextsKeys.MESSAGETYPE_TITLE))));
        this.addCaracteristic(createSimpleCaracteristic(
                this.cbDelete,
                new JLabel(FileTools.readElementText(TextsKeys.KEY_DELETETEXT)),
                new HelpButton(FileTools.readHelpMessage(
                        TextsKeys.KEY_HELP_C_DELETE,
                        TextsKeys.MESSAGETYPE_TOOLTIP), FileTools
                        .readHelpMessage(TextsKeys.KEY_HELP_C_DELETE,
                                TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                        .readHelpMessage(TextsKeys.KEY_HELP_C_DELETE,
                                TextsKeys.MESSAGETYPE_TITLE))));
    }

    /**
     * Gets the current selected type.
     * @return the current selected type
     */
    public final String getTypeSelected() {
        return (String) this.cbType.getSelectedItem();
    }

    /**
     * Tells if the delete check box is selected.
     * @return true - delete check box is selected\n false - delete check box is
     *         not selected
     */
    public final boolean isDeleteSelected() {
        return this.cbDelete.isSelected();
    }

    /**
     * Gets the check box delete.
     * @return the delete check box
     */
    public JCheckBox getDeleteCheckBox() {
        return cbDelete;
    }

    /**
     * Selects the given type.
     * @param string
     *            the new type to select
     */
    public final void setType(final String string) {
        this.cbType.setSelectedItem(string);
        checkEnableValidateButton();
    }

    /**
     * Checks if the VALIDATE button should be enabled or not.
     */
    public final void checkEnableValidateButton() {
        if (!this.cbType.getSelectedItem().equals("")
                || this.cbDelete.isSelected())
        {
            this.bValidate.setEnabled(true);
        } else
        {
            this.bValidate.setEnabled(false);
        }
    }
}
