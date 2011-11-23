/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.constants.Characteristics;
import fr.nantes1900.view.isletprocess.CaracteristicsStep2View;

/**
 * Characteristics panel for the second step of process of an islet.
 * 
 * User can select one or more triangles and modifies the type they belong to : building or ground.
 * @author Camille
 * @author Luc
 */
public class CaracteristicsStep2Controller extends CaracteristicsController
{
    /**
     * Constructor.
     * @param parentController
     */
    public CaracteristicsStep2Controller(IsletProcessController parentController)
    {
        super(parentController);
        this.cView = new CaracteristicsStep2View();
        this.cView.getValidateButton().addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                String typeChosen = ((CaracteristicsStep2View) cView).getTypeSelected();
                
                int actionType = -1;
                switch (typeChosen)
                {
                    case Characteristics.TYPE_GROUND:
                        actionType = ActionTypes.TURN_TO_GROUND;
                        break;
                        
                    case Characteristics.TYPE_BUILDING:
                        actionType = ActionTypes.TURN_TO_BUILDING;
                        break;
                }
                
                if (actionType != -1)
                {
                    CaracteristicsStep2Controller.this.parentController.launchAction(actionType);
                    // TODO : remove when visual test is ok
                    System.out.println("Type modifié en " + typeChosen);
                } else
                {
                    JOptionPane.showMessageDialog(cView, "Le type choisi est incorrrect", "Validation impossible", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        });
    }
}
