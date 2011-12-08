package fr.nantes1900.view.isletprocess;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * Characteristics panel for the 3rd step of an islet process when triangles are
 * selected.
 * @author Camille Bouquet, Luc Jallerat
 */
public class CharacteristicsStep3TrianglesView extends CharacteristicsView {

    /**
     * Default serial UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Check box to delete a list of triangles.
     */
    private JCheckBox cbDelete;

    /**
     * Creates a new characteristics panels for a list of selected triangles in
     * the step 3.
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

        this.addCaracteristic(createSimpleCaracteristic(
                this.cbDelete,
                new JLabel(FileTools.readElementText(TextsKeys.KEY_DELETETEXT)),
                new HelpButton()));

        this.bValidate.setEnabled(true);
    }

    /**
     * Tells if the delete check box is selected.
     * @return true - delete check box is selected\n false - delete check box is
     *         not selected
     */
    public final boolean isDeleteSelected() {
        return this.cbDelete.isSelected();
    }
}
