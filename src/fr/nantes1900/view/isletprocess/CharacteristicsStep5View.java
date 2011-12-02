package fr.nantes1900.view.isletprocess;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * TODO .
 * @author Camille Bouquet
 */
public class CharacteristicsStep5View extends CharacteristicsView {

    /**
     * Default serial UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * TODO .
     */
    private JCheckBox cbMerge;
    /**
     * TODO .
     */
    private JCheckBox cbNoise;

    /**
     * TODO .
     */
    public CharacteristicsStep5View() {
        super();
        this.cbMerge = new JCheckBox();
        this.cbMerge.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent arg0) {
                if (((JCheckBox) arg0.getSource()).isSelected()) {
                    CharacteristicsStep5View.this.getCBMerge()
                            .setEnabled(false);
                } else {
                    CharacteristicsStep5View.this.getCBMerge().setEnabled(true);
                }
            }

        });
        this.addCaracteristic(createSimpleCaracteristic(this.cbMerge,
                FileTools.readElementText(TextsKeys.KEY_MERGETEXT),
                new HelpButton()));
        this.cbNoise = new JCheckBox();
        this.cbNoise.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent arg0) {
                if (((JCheckBox) arg0.getSource()).isSelected()) {
                    CharacteristicsStep5View.this.getCBMerge()
                            .setEnabled(false);
                    CharacteristicsStep5View.this.bValidate.setEnabled(true);
                } else {
                    CharacteristicsStep5View.this.getCBMerge().setEnabled(true);
                    CharacteristicsStep5View.this.bValidate.setEnabled(false);
                }
            }

        });
        this.addCaracteristic(createSimpleCaracteristic(this.cbNoise,
                FileTools.readElementText(TextsKeys.KEY_PUTINNOISETEXT),
                new HelpButton()));
        this.bValidate.setEnabled(true);
    }

    /**
     * TODO .
     */
    public final void deselectAll() {
        this.cbMerge.setSelected(false);
        this.cbNoise.setSelected(false);
    }

    /**
     * Getter.
     * @return TODO .
     */
    public final JCheckBox getCBMerge() {
        return this.cbMerge;
    }

    /**
     * TODO .
     * @return TODO .
     */
    public final boolean isMergeSelected() {
        return this.cbMerge.isSelected();
    }

    /**
     * TODO .
     * @return TODO .
     */
    public final boolean isNoiseSelected() {
        return this.cbNoise.isSelected();
    }

    /**
     * TODO .
     * @param mergeEnable
     *            TODO .
     */
    public final void setMergeEnable(final boolean mergeEnable) {
        this.cbMerge.setEnabled(mergeEnable);
    }
}
