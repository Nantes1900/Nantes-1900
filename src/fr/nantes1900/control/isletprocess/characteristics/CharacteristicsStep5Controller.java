package fr.nantes1900.control.isletprocess.characteristics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.control.isletprocess.IsletProcessController;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.characteristics.CharacteristicsStep5View;

/**
 * Characteristics panel for the fifth step of process of an islet.
 * @author Camille Bouquet, Luc Jallerat
 */
public class CharacteristicsStep5Controller extends
        AbstractCharacteristicsSurfacesController {

    /**
     * Creates a new step 5 characteristics controller which will create the
     * panel and sets the action to perform when validate button is clicked.
     * @param parentControllerIn
     *            the parent controller
     * @param surfaceSelected
     *            the selected surface
     */
    public CharacteristicsStep5Controller(
            final IsletProcessController parentControllerIn,
            final Surface surfaceSelected) {
        super(parentControllerIn, surfaceSelected);

        this.cView = new CharacteristicsStep5View();
        ((CharacteristicsStep5View) this.cView).setMergeEnable(false);
        this.cView.getValidateButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                // if the merge check box is selected
                if (((CharacteristicsStep5View) cView).isMergeSelected()) {
                    try {
                        CharacteristicsStep5Controller.this.parentController
                                .getBiController().action5(surfacesList,
                                        ActionTypes.MERGE);
                    } catch (InvalidCaseException e1) {
                        JOptionPane.showMessageDialog(cView, FileTools
                                .readInformationMessage(
                                        TextsKeys.KEY_ERROR_INCORRECTACTION,
                                        TextsKeys.MESSAGETYPE_MESSAGE),
                                FileTools.readInformationMessage(
                                        TextsKeys.KEY_ERROR_INCORRECTACTION,
                                        TextsKeys.MESSAGETYPE_TITLE),
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

                // if the put in noise check box is selected
                if (((CharacteristicsStep5View) cView).isNoiseSelected()) {
                    try {
                        CharacteristicsStep5Controller.this.parentController
                                .getBiController().action5(surfacesList,
                                        ActionTypes.TURN_TO_NOISE);
                    } catch (InvalidCaseException e1) {
                        JOptionPane.showMessageDialog(cView, FileTools
                                .readInformationMessage(
                                        TextsKeys.KEY_ERROR_INCORRECTACTION,
                                        TextsKeys.MESSAGETYPE_MESSAGE),
                                FileTools.readInformationMessage(
                                        TextsKeys.KEY_ERROR_INCORRECTACTION,
                                        TextsKeys.MESSAGETYPE_TITLE),
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                CharacteristicsStep5Controller.this.parentController
                        .refreshViews();
            }
        });
    }

    @Override
    public final void modifyViewCharacteristics() {
        ((CharacteristicsStep5View) this.cView).deselectAll();
        if (this.surfacesList.size() == 1) {
            ((CharacteristicsStep5View) this.cView).setMergeEnable(false);
        } else {
            ((CharacteristicsStep5View) this.cView).setMergeEnable(true);
        }
    }
}
