/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * TODO .
 * @author Camille
 */
public class CharacteristicsStep3TrianglesView extends CharacteristicsView {

    /**
     * Default serial UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * TODO .
     */
    private JCheckBox cbDelete;

    /**
     * TODO .
     */
    public CharacteristicsStep3TrianglesView() {
        super();

        this.cbDelete = new JCheckBox();
        this.cbDelete.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent arg0) {
                if (((JCheckBox) arg0.getSource()).isSelected()) {
                    CharacteristicsStep3TrianglesView.this.bValidate
                            .setEnabled(true);
                } else {
                    CharacteristicsStep3TrianglesView.this.bValidate
                            .setEnabled(false);
                }
            }

        });
        this.addCaracteristic(createSimpleCaracteristic(this.cbDelete,
                new JLabel(FileTools.readElementText(TextsKeys.KEY_DELETETEXT)),
                new HelpButton()));

        this.bValidate.setEnabled(true);
    }

    /**
     * TODO .
     * @return TODO .
     */
    public final boolean isDeleteSelected() {
        return this.cbDelete.isSelected();
    }
}
