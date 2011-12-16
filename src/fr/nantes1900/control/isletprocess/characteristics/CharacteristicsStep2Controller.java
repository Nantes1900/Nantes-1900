package fr.nantes1900.control.isletprocess.characteristics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.constants.Characteristics;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.control.isletprocess.IsletProcessController;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.exceptions.InvalidCaseException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.characteristics.CharacteristicsStep2View;

/**
 * Characteristics panel for the second step of process of an islet. User can
 * select one or more triangles and modifies the type they belong to : building
 * or ground.
 * @author Camille Bouquet, Luc Jallerat
 */
public class CharacteristicsStep2Controller extends
        AbstractCharacteristicsTrianglesController {

    /**
     * Creates a new step 2 characteristics controller which will create the
     * panel and sets the action to perform when VALIDATE button is clicked.
     * @param parentControllerIn
     *            the parent controller
     * @param trianglesSelected
     *            the selected triangles
     */
    public CharacteristicsStep2Controller(
            final IsletProcessController parentControllerIn,
            final List<Triangle> trianglesSelected) {
        super(parentControllerIn, trianglesSelected);

        this.cView = new CharacteristicsStep2View();
        this.cView.getValidateButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (((CharacteristicsStep2View) cView).isDeleteSelected()) {
                    try {
                        CharacteristicsStep2Controller.this.parentController
                                .getBiController().action2(trianglesList,
                                        ActionTypes.REMOVE);
                    } catch (InvalidCaseException e) {
                        JOptionPane.showMessageDialog(cView, FileTools
                                .readInformationMessage(
                                        TextsKeys.KEY_ERROR_INCORRECTACTION,
                                        TextsKeys.MESSAGETYPE_MESSAGE),
                                FileTools.readInformationMessage(
                                        TextsKeys.KEY_ERROR_INCORRECTACTION,
                                        TextsKeys.MESSAGETYPE_TITLE),
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    String typeChosen = ((CharacteristicsStep2View) CharacteristicsStep2Controller.this.cView)
                            .getTypeSelected();

                    // Gets the selected type
                    int actionType = -1;
                    switch (typeChosen) {

                    case Characteristics.TYPE_GROUND:
                        actionType = ActionTypes.TURN_TO_GROUND;
                        break;

                    case Characteristics.TYPE_BUILDING:
                        actionType = ActionTypes.TURN_TO_BUILDING;
                        break;

                    default:
                        break;
                    }

                    try {
                        CharacteristicsStep2Controller.this.parentController
                                .getBiController()
                                .action2(
                                        CharacteristicsStep2Controller.this.trianglesList,
                                        actionType);
                    } catch (InvalidCaseException e) {
                        JOptionPane.showMessageDialog(
                                CharacteristicsStep2Controller.this.cView,
                                FileTools.readInformationMessage(
                                        TextsKeys.KEY_ERROR_INCORRECTTYPE,
                                        TextsKeys.MESSAGETYPE_MESSAGE),
                                FileTools.readInformationMessage(
                                        TextsKeys.KEY_ERROR_INCORRECTTYPE,
                                        TextsKeys.MESSAGETYPE_TITLE),
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

                CharacteristicsStep2Controller.this.parentController
                        .refreshViews();
            }

        });
        parentController.addShortcut(KeyStroke.getKeyStroke("DELETE"),
                "action delete", new AbstractAction() {

                    /**
                     * default serial UID.
                     */
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JCheckBox deleteCB = ((CharacteristicsStep2View) CharacteristicsStep2Controller.this.cView)
                                .getDeleteCheckBox();
                        if (!deleteCB.isSelected()) {
                            deleteCB.doClick();
                        }
                        ((CharacteristicsStep2View) CharacteristicsStep2Controller.this.cView)
                                .getValidateButton().doClick();
                    }
                });

        modifyViewCharacteristics();
    }

    @Override
    public final void modifyViewCharacteristics() {
        if (this.trianglesList.size() == 1) {
            try {
                ((CharacteristicsStep2View) this.cView)
                        .setType(this.parentController.getBiController()
                                .getCharacteristics2(this.trianglesList.get(0)));
            } catch (InvalidCaseException e) {
                JOptionPane.showMessageDialog(cView, FileTools
                        .readInformationMessage(
                                TextsKeys.KEY_ERROR_INVALIDCASETYPE,
                                TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                        .readInformationMessage(
                                TextsKeys.KEY_ERROR_INVALIDCASETYPE,
                                TextsKeys.MESSAGETYPE_TITLE),
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            ((CharacteristicsStep2View) this.cView).setType("");
        }
    }
}
