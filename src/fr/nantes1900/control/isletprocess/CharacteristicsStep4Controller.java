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

/**
 * Characteristics panel for the second step of process of an islet.
 * 
 * User can select one or more triangles and modifies the type they belong to : building or ground.
 * @author Camille
 * @author Luc
 */
public class CharacteristicsStep4Controller extends CharacteristicsController
{
    public ArrayList<Triangle> trianglesList;
    /**
     * Constructor.
     * @param parentController
     * @param triangleSelected 
     */
    public CharacteristicsStep4Controller(IsletProcessController parentController, Triangle triangleSelected)
    {
        super(parentController);
        trianglesList = new ArrayList<Triangle>();
        trianglesList.add(triangleSelected);
        
        this.cView = new CharacteristicsStep2View();
        this.cView.getValidateButton().addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                String typeChosen = ((CharacteristicsStep2View) cView).getTypeSelected();
                
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
                
                if (actionType != -1)
                {
                    CharacteristicsStep4Controller.this.parentController.launchAction(2, actionType, Characteristics.SELECTION_TYPE_TRIANGLE);
                } else
                {
                    JOptionPane.showMessageDialog(cView, "Le type choisi est incorrrect", "Validation impossible", JOptionPane.ERROR_MESSAGE);
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
}