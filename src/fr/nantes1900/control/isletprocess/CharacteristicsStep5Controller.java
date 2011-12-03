/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.CharacteristicsStep5View;

/**
 * Characteristics panel for the fifth step of process of an islet. TODO
 * @author Camille
 * @author Luc
 */
public class CharacteristicsStep5Controller extends
        AbstractCharacteristicsSurfacesController {

    /**
     * Constructor.
     * @param parentController
     * @param triangleSelected
     */
    public CharacteristicsStep5Controller(
            IsletProcessController parentController, Surface surfaceSelected) {
        super(parentController, surfaceSelected);

        this.cView = new CharacteristicsStep5View();
        this.cView.getValidateButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                if (((CharacteristicsStep5View) cView).isMergeSelected()) {
                    try {
                        CharacteristicsStep5Controller.this.parentController
                                .getBiController().action5(surfacesList,
                                        ActionTypes.MERGE);
                    } catch (InvalidCaseException e1) {
                        JOptionPane.showMessageDialog(cView, FileTools
                                .readErrorMessage(
                                        TextsKeys.KEY_ERROR_INCORRECTACTION,
                                        TextsKeys.MESSAGETYPE_MESSAGE),
                                FileTools.readErrorMessage(
                                        TextsKeys.KEY_ERROR_INCORRECTACTION,
                                        TextsKeys.MESSAGETYPE_TITLE),
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (((CharacteristicsStep5View) cView).isNoiseSelected()) {
                    try {
                        CharacteristicsStep5Controller.this.parentController
                                .getBiController().action5(surfacesList,
                                        ActionTypes.TURN_TO_NOISE);
                    } catch (InvalidCaseException e1) {
                        JOptionPane.showMessageDialog(cView, FileTools
                                .readErrorMessage(
                                        TextsKeys.KEY_ERROR_INCORRECTACTION,
                                        TextsKeys.MESSAGETYPE_MESSAGE),
                                FileTools.readErrorMessage(
                                        TextsKeys.KEY_ERROR_INCORRECTACTION,
                                        TextsKeys.MESSAGETYPE_TITLE),
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                CharacteristicsStep5Controller.this.parentController.refreshViews();
            }
        });
    }

    public void addSurfaceSelected(Surface surfaceSelected) {
        this.surfacesList.add(surfaceSelected);
    }

    public ArrayList<Surface> getSurfaces() {
        return this.surfacesList;
    }

    @Override
    public void modifyViewCharacteristics() {
        ((CharacteristicsStep5View) cView).deselectAll();
        if (surfacesList.size() > 1) {
            ((CharacteristicsStep5View) cView).setMergeEnable(true);
        } else {
            ((CharacteristicsStep5View) cView).setMergeEnable(false);
        }
    }
}
