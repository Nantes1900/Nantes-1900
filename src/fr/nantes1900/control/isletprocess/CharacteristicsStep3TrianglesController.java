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
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.view.isletprocess.CharacteristicsStep2View;
import fr.nantes1900.view.isletprocess.CharacteristicsStep3TrianglesView;

/**
 * Characteristics panel for the second step of process of an islet. User can
 * select one or more triangles and modifies the type they belong to : building
 * or ground.
 * @author Camille
 * @author Luc
 */
public class CharacteristicsStep3TrianglesController extends
        CharacteristicsController
{
    private ArrayList<Triangle> trianglesList;

    /**
     * Constructor.
     * @param parentController
     * @param triangleSelected
     */
    public CharacteristicsStep3TrianglesController(
            IsletProcessController parentController, Triangle triangleSelected)
    {
        super(parentController);
        trianglesList = new ArrayList<Triangle>();
        trianglesList.add(triangleSelected);

        this.cView = new CharacteristicsStep2View();
        this.cView.getValidateButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                if (((CharacteristicsStep3TrianglesView) cView)
                        .isDeleteSelected())
                {
                    CharacteristicsStep3TrianglesController.this.parentController
                    .launchAction(3, ActionTypes.REMOVE,
                            Characteristics.SELECTION_TYPE_TRIANGLE);
                } else
                {
                    String typeChosen = ((CharacteristicsStep3TrianglesView) cView)
                            .getElementSelected();
                    
                    int actionType = -1;
                    switch (typeChosen)
                    {
                        case Characteristics.TYPE_NOISE:
                            actionType = ActionTypes.TURN_TO_NOISE;
                            break;
                            
                        case Characteristics.TYPE_BUILDING:
                            actionType = ActionTypes.TURN_TO_BUILDING;
                            break;
                    }
                    
                    if (actionType != -1)
                    {
                        CharacteristicsStep3TrianglesController.this.parentController
                        .launchAction(3, actionType,
                                Characteristics.SELECTION_TYPE_TRIANGLE);
                    } else
                    {
                        JOptionPane.showMessageDialog(cView,
                                "Le type choisi est incorrrect",
                                "Validation impossible",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
    }

    public void addTriangleSelected(Triangle triangleSelected)
    {
        this.trianglesList.add(triangleSelected);
        ((CharacteristicsStep2View) this.cView).setType("");
    }

    public ArrayList<Triangle> getTriangles()
    {
        return this.trianglesList;
    }
}
