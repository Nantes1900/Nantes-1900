/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.constants.Characteristics;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.CharacteristicsStep4View;

/**
 * Characteristics panel for the fourth step of process of an islet. TODO
 * @author Camille
 * @author Luc
 */
public class CharacteristicsStep4Controller extends
        AbstractCharacteristicsTrianglesController {

    /**
     * Constructor.
     * @param parentController
     * @param trianglesSelected
     */
    public CharacteristicsStep4Controller(
            IsletProcessController parentController, List<Triangle> trianglesSelected) {
        super(parentController, trianglesSelected);

        this.cView = new CharacteristicsStep4View();
        this.cView.getValidateButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String typeChosen = ((CharacteristicsStep4View) cView)
                        .getTypeSelected();

                int actionType = -1;
                switch (typeChosen) {
                case Characteristics.TYPE_WALL:
                    actionType = ActionTypes.TURN_TO_WALL;
                    break;

                case Characteristics.TYPE_ROOF:
                    actionType = ActionTypes.TURN_TO_ROOF;
                    break;
                }

                try {
                    CharacteristicsStep4Controller.this.parentController
                            .getBiController().action4(trianglesList,
                                    actionType);
                } catch (InvalidCaseException e) {
                    JOptionPane.showMessageDialog(cView, FileTools
                            .readInformationMessage(
                                    TextsKeys.KEY_ERROR_INCORRECTTYPE,
                                    TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                            .readInformationMessage(
                                    TextsKeys.KEY_ERROR_INCORRECTTYPE,
                                    TextsKeys.MESSAGETYPE_TITLE),
                            JOptionPane.ERROR_MESSAGE);
                }
                CharacteristicsStep4Controller.this.parentController.refreshViews();
            }

        });
        modifyViewCharacteristics();
    }

    @Override
    public void modifyViewCharacteristics() {
        if (trianglesList.size() == 1) {
            try {
                ((CharacteristicsStep4View) this.cView)
                        .setType(parentController.getBiController()
                                .getCharacteristics4(trianglesList.get(0)));
            } catch (InvalidCaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            ((CharacteristicsStep4View) this.cView).setType("");
        }
    }
}
