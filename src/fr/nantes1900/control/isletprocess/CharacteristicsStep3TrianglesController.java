package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.CharacteristicsStep3TrianglesView;

/**
 * Characteristics panel for the second step of process of an islet. User can
 * select one or more triangles and delete them.
 * @author Camille Bouquet
 * @author Luc Jallerat
 */
public class CharacteristicsStep3TrianglesController extends
        AbstractCharacteristicsTrianglesController {

    /**
     * Creates a new step 3 characteristics controller for triangles selection
     * which will create the panel and sets the action to perform when validate
     * button is clicked.
     * @param parentControllerIn
     *            the parent controller
     * @param trianglesSelected
     *            the selected triangles
     */
    public CharacteristicsStep3TrianglesController(
            final IsletProcessController parentControllerIn,
            final List<Triangle> trianglesSelected) {
        super(parentControllerIn, trianglesSelected);
        this.cView = new CharacteristicsStep3TrianglesView();

        this.cView.getValidateButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (((CharacteristicsStep3TrianglesView) cView)
                        .isDeleteSelected()) {
                    // try {
                    // FIXME
                    // CharacteristicsStep3TrianglesController.this.parentController
                    // .getBiController().action3(trianglesList,
                    // ActionTypes.REMOVE);
                    // } catch (InvalidCaseException e) {
                    // JOptionPane.showMessageDialog(cView, FileTools
                    // .readInformationMessage(
                    // TextsKeys.KEY_ERROR_INCORRECTACTION,
                    // TextsKeys.MESSAGETYPE_MESSAGE),
                    // FileTools.readInformationMessage(
                    // TextsKeys.KEY_ERROR_INCORRECTACTION,
                    // TextsKeys.MESSAGETYPE_TITLE),
                    // JOptionPane.ERROR_MESSAGE);
                    // }
                }
                CharacteristicsStep3TrianglesController.this.parentController
                        .refreshViews();
            }

        });
        this.modifyViewCharacteristics();
    }

    @Override
    public void modifyViewCharacteristics() {
        // Nothing special to do.
    }
}
