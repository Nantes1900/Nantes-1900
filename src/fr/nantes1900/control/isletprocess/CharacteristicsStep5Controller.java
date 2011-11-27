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
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.models.islets.buildings.exceptions.UnCompletedParametersException;
import fr.nantes1900.view.isletprocess.CharacteristicsStep2View;
import fr.nantes1900.view.isletprocess.CharacteristicsStep5View;

/**
 * Characteristics panel for the fifth step of process of an islet.
 * TODO
 * @author Camille
 * @author Luc
 */
public class CharacteristicsStep5Controller extends CharacteristicsController
{
    public ArrayList<Surface> surfacesList;
    /**
     * Constructor.
     * @param parentController
     * @param triangleSelected 
     */
    public CharacteristicsStep5Controller(IsletProcessController parentController, Surface surfaceSelected)
    {
        super(parentController);
        surfacesList = new ArrayList<Surface>();
        surfacesList.add(surfaceSelected);
        
        this.cView = new CharacteristicsStep2View();
        this.cView.getValidateButton().addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                              
                if (((CharacteristicsStep5View) cView).isMergeSelected())
                {
                    try
                    {
                        CharacteristicsStep5Controller.this.parentController.getBiController().action5(surfacesList, ActionTypes.MERGE);
                    } catch (InvalidCaseException e1)
                    {
                        JOptionPane.showMessageDialog(cView,
                                "Le type choisi est incorrrect",
                                "Validation impossible", JOptionPane.ERROR_MESSAGE);
                    } catch (UnCompletedParametersException e2)
                    {
                        // TODO : remove when implemented
                        JOptionPane.showMessageDialog(cView,
                                "Non implémenté encore",
                                "Validation impossible", JOptionPane.ERROR_MESSAGE);
                    }
                } 

                if (((CharacteristicsStep5View) cView).isRemoveSelected())
                {
                    try
                    {
                        CharacteristicsStep5Controller.this.parentController.getBiController().action5(surfacesList, ActionTypes.TURN_TO_NOISE);
                    } catch (InvalidCaseException e1)
                    {
                        JOptionPane.showMessageDialog(cView,
                                "Le type choisi est incorrrect",
                                "Validation impossible", JOptionPane.ERROR_MESSAGE);
                    } catch (UnCompletedParametersException e2)
                    {
                        // TODO : remove when implemented
                        JOptionPane.showMessageDialog(cView,
                                "Non implémenté encore",
                                "Validation impossible", JOptionPane.ERROR_MESSAGE);
                    }
                } 
            }
        });
    }
    
    public ArrayList<Surface> getSurfaces()
    {
        return this.surfacesList;
    }
    
    public void addSurfaceSelected(Surface surfaceSelected)
    {
        this.surfacesList.add(surfaceSelected);
    }
}
