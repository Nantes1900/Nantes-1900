package fr.nantes1900.view.isletprocess;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * Characteristics panel for the 5th step of an islet process.
 * @author Camille Bouquet
 */
public class CharacteristicsStep5View extends CharacteristicsView {

    /**
     * Default serial UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Check box to merge surfaces into one.
     */
    private JCheckBox cbMerge;
    /**
     * Check box to put surfaces into noise.
     */
    private JCheckBox cbNoise;

    /**
     * Label of the title of the merge button.
     */
    private JLabel lMerge;

    /**
     * Creates a new panel for step 5 characteristics.
     * Adds 2 checkboxes, one for merge which is enabled only when several
     * surfaces are selected.
     * Only one checkbox can be selected at the same time.
     */
    public CharacteristicsStep5View() {
        super();

        // Merge actions
        this.cbMerge = new JCheckBox();
        this.cbMerge.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent arg0) {
                if (((JCheckBox) arg0.getSource()).isSelected())
                {
                    CharacteristicsStep5View.this.bValidate.setEnabled(true);
                    CharacteristicsStep5View.this.getCBNoise().setSelected(
                            false);
                } else
                {
                    CharacteristicsStep5View.this.getCBNoise().setEnabled(true);
                }
            }

        });
        lMerge = new JLabel(FileTools.readElementText(TextsKeys.KEY_MERGETEXT));
        this.addCaracteristic(createSimpleCaracteristic(this.cbMerge, lMerge,
                new HelpButton()));

        // Put in noise actions
        this.cbNoise = new JCheckBox();
        this.cbNoise.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent arg0) {
                if (((JCheckBox) arg0.getSource()).isSelected())
                {
                    if (CharacteristicsStep5View.this.lMerge.isEnabled())
                    {
                        CharacteristicsStep5View.this.getCBMerge().setSelected(
                                false);
                    }

                    CharacteristicsStep5View.this.bValidate.setEnabled(true);
                } else
                {
                    if (CharacteristicsStep5View.this.lMerge.isEnabled())
                    {
                        CharacteristicsStep5View.this.getCBMerge().setEnabled(
                                true);
                    }
                    CharacteristicsStep5View.this.bValidate.setEnabled(false);
                }
            }

        });

        this.addCaracteristic(createSimpleCaracteristic(
                this.cbNoise,
                new JLabel(FileTools
                        .readElementText(TextsKeys.KEY_PUTINNOISETEXT)),
                new HelpButton()));
    }

    /**
     * Deselects all checkboxes.
     */
    public final void deselectAll() {
        this.cbMerge.setSelected(false);
        this.cbNoise.setSelected(false);
    }

    /**
     * Gets the merge checkbox.
     * @return the merge checkbox
     */
    public final JCheckBox getCBMerge() {
        return this.cbMerge;
    }

    /**
     * Gets the noise checkbox.
     * @return the noise checkbox
     */
    public final JCheckBox getCBNoise() {
        return this.cbNoise;
    }

    /**
     * Is the merge checkbox selected.
     * @return true - the merge checkbox is selected\n
     *         false - the merge checkbox is not selected
     */
    public final boolean isMergeSelected() {
        return this.cbMerge.isSelected();
    }

    /**
     * Is the noise checkbox selected.
     * @return true - the noise checkbox is selected\n
     *         false - the noise checkbox is not selected
     */
    public final boolean isNoiseSelected() {
        return this.cbNoise.isSelected();
    }

    /**
     * Enables or disables the merge label and checkbox.
     * @param mergeEnable
     *            true - enables merge elements\n
     *            false - disables the merge elements
     */
    public final void setMergeEnable(final boolean mergeEnable) {
        this.cbMerge.setEnabled(mergeEnable);
        this.lMerge.setEnabled(mergeEnable);
    }
}
