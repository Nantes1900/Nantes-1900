package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.CharacteristicsStep3View;

/**
 * Characteristics panel for the second step of process of an islet. User can
 * select one or more triangles and modifies the type they belong to : building
 * or ground.
 * @author Camille Bouquet, Luc Jallerat
 */
public class CharacteristicsStep3Controller extends
        AbstractCharacteristicsSurfacesController {

    /**
     * Creates a new step 3 characteristics controller for surfaces selection
     * which will create the panel and sets the action to perform when validate
     * button is clicked.
     * @param parentControllerIn
     *            the parent controller
     * @param surfaceSelected
     *            the selected surface
     */
    public CharacteristicsStep3Controller(
            final IsletProcessController parentControllerIn,
            final Surface surfaceSelected) {
        super(parentControllerIn, surfaceSelected);

        this.cView = new CharacteristicsStep3View();
        modifyViewCharacteristics();

        this.cView.getValidateButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (((CharacteristicsStep3View) cView)
                        .isNoiseSelected())
                {
                    for (Surface surface : CharacteristicsStep3Controller.this.surfacesList)
                    {
                        try
                        {
                            // TODO : use new action 3 put in noise
                            CharacteristicsStep3Controller.this.parentController
                                    .getBiController().action3(surface,
                                            ActionTypes.TURN_TO_NOISE);
                        } catch (InvalidCaseException e)
                        {
                            JOptionPane.showMessageDialog(
                                    cView,
                                    FileTools
                                            .readInformationMessage(
                                                    TextsKeys.KEY_ERROR_INCORRECTACTION,
                                                    TextsKeys.MESSAGETYPE_MESSAGE),
                                    FileTools
                                            .readInformationMessage(
                                                    TextsKeys.KEY_ERROR_INCORRECTACTION,
                                                    TextsKeys.MESSAGETYPE_TITLE),
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                CharacteristicsStep3Controller.this.parentController
                        .refreshViews();
            }
        });
    }

    @Override
    public final void modifyViewCharacteristics() {
        // nothing special to do
    }
}
