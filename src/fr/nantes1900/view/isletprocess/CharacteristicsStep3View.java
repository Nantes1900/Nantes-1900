package fr.nantes1900.view.isletprocess;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * Characteristics panel for the 3rd step of an islet process when one or more
 * surfaces are selected.
 * @author Camille Bouquet, Luc Jallerat
 */
public class CharacteristicsStep3View extends CharacteristicsView {

    /**
     * Default serial UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Check box to put surfaces into noise.
     */
    private JCheckBox cbNoise;

    /**
     * Creates a new panel to display and modify step 3 characteristics for one
     * or more surfaces.
     */
    public CharacteristicsStep3View() {
        super();
        // Puts in noise actions
        this.cbNoise = new JCheckBox();
        this.cbNoise.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent arg0) {
                if (((JCheckBox) arg0.getSource()).isSelected())
                {
                    CharacteristicsStep3View.this.bValidate.setEnabled(true);
                } else
                {
                    CharacteristicsStep3View.this.bValidate.setEnabled(false);
                }
            }

        });

        this.addCaracteristic(createSimpleCaracteristic(
                this.cbNoise,
                new JLabel(FileTools
                        .readElementText(TextsKeys.KEY_PUTINNOISETEXT)),
                new HelpButton(FileTools.readHelpMessage(
                        TextsKeys.KEY_HELP_C_NOISE,
                        TextsKeys.MESSAGETYPE_TOOLTIP), FileTools
                        .readHelpMessage(TextsKeys.KEY_HELP_C_NOISE,
                                TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                        .readHelpMessage(TextsKeys.KEY_HELP_C_NOISE,
                                TextsKeys.MESSAGETYPE_TITLE))));
    }

    /**
     * Is the noise checkbox selected.
     * @return true - the noise checkbox is selected\n false - the noise
     *         checkbox is not selected
     */
    public final boolean isNoiseSelected() {
        return this.cbNoise.isSelected();
    }
}
