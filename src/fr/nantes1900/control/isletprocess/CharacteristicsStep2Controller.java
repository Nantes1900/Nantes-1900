/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.constants.Characteristics;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.CharacteristicsStep2View;

/**
 * Characteristics panel for the second step of process of an islet. User can
 * select one or more triangles and modifies the type they belong to : building
 * or ground.
 * @author Camille
 * @author Luc
 */
public class CharacteristicsStep2Controller extends
        AbstractCharacteristicsTrianglesController {

    /**
     * Constructor.
     * @param parentController
     * @param triangleSelected
     */
    public CharacteristicsStep2Controller(
            IsletProcessController parentController, Triangle triangleSelected) {
        super(parentController, triangleSelected);

        this.cView = new CharacteristicsStep2View();
        modifyViewCharacteristics();
        this.cView.getValidateButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String typeChosen = ((CharacteristicsStep2View) cView)
                        .getTypeSelected();

                int actionType = -1;
                switch (typeChosen) {
                case Characteristics.TYPE_GROUND:
                    actionType = ActionTypes.TURN_TO_GROUND;
                    break;

                case Characteristics.TYPE_BUILDING:
                    actionType = ActionTypes.TURN_TO_BUILDING;
                    break;
                }

                try {
                    CharacteristicsStep2Controller.this.parentController
                            .getBiController().action2(trianglesList,
                                    actionType);
                } catch (InvalidCaseException e) {
                    JOptionPane.showMessageDialog(cView, FileTools
                            .readErrorMessage(
                                    TextsKeys.KEY_ERROR_INCORRECTTYPE,
                                    TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                            .readErrorMessage(
                                    TextsKeys.KEY_ERROR_INCORRECTTYPE,
                                    TextsKeys.MESSAGETYPE_TITLE),
                            JOptionPane.ERROR_MESSAGE);
                }
                
                CharacteristicsStep2Controller.this.parentController.refreshViews();
            }

        });
    }

    @Override
    public void modifyViewCharacteristics() {
        if (trianglesList.size() == 1) {
            try {
                ((CharacteristicsStep2View) this.cView)
                        .setType(parentController.getBiController()
                                .getCharacteristics2(trianglesList.get(0)));
            } catch (InvalidCaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            ((CharacteristicsStep2View) this.cView).setType("");
        }
    }
}
