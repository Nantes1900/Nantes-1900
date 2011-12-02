/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.constants.Characteristics;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.CharacteristicsStep2View;

/**
 * Characteristics panel for the fourth step of process of an islet. TODO
 * @author Camille
 * @author Luc
 */
public class CharacteristicsStep4Controller extends AbstractCharacteristicsTrianglesController
{
    /**
     * Constructor.
     * @param parentController
     * @param triangleSelected
     */
    public CharacteristicsStep4Controller(
            IsletProcessController parentController, Triangle triangleSelected)
    {
        super(parentController, triangleSelected);

        this.cView = new CharacteristicsStep2View();
        this.cView.getValidateButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                String typeChosen = ((CharacteristicsStep2View) cView)
                        .getTypeSelected();

                int actionType = -1;
                switch (typeChosen)
                {
                    case Characteristics.TYPE_WALL:
                        actionType = ActionTypes.TURN_TO_WALL;
                    break;

                    case Characteristics.TYPE_ROOF:
                        actionType = ActionTypes.TURN_TO_ROOF;
                    break;
                }

                try
                {
                    CharacteristicsStep4Controller.this.parentController
                            .getBiController().action4(trianglesList,
                                    actionType);
                } catch (InvalidCaseException e)
                {
                    JOptionPane.showMessageDialog(cView, FileTools
                            .readErrorMessage(
                                    TextsKeys.KEY_ERROR_INCORRECTTYPE,
                                    TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                            .readErrorMessage(
                                    TextsKeys.KEY_ERROR_INCORRECTTYPE,
                                    TextsKeys.MESSAGETYPE_TITLE),
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        });
    }

    public ArrayList<Triangle> getTriangles()
    {
        return this.trianglesList;
    }

    public void addTriangleSelected(Triangle triangleSelected)
    {
        this.trianglesList.add(triangleSelected);
        ((CharacteristicsStep2View) this.cView).setType("");
    }

    @Override
    public void modifyViewCharacteristics()
    {
        if (trianglesList.size() == 1)
        {
            try
            {
                ((CharacteristicsStep2View) this.cView).setType(parentController.getBiController().getCharacteristics4(trianglesList.get(0)));
            } catch (InvalidCaseException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
        {
            ((CharacteristicsStep2View) this.cView).setType("");
        }
    }
}
