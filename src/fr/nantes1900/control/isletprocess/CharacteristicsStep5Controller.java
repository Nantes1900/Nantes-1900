/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.constants.Characteristics;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.view.isletprocess.CharacteristicsStep2View;
import fr.nantes1900.view.isletprocess.CharacteristicsStep5View;

/**
 * Characteristics panel for the second step of process of an islet.
 * 
 * User can select one or more triangles and modifies the type they belong to : building or ground.
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
                    CharacteristicsStep5Controller.this.parentController.launchAction(5, ActionTypes.MERGE, Characteristics.SELECTION_TYPE_ELEMENT);
                } 

                if (((CharacteristicsStep5View) cView).isRemoveSelected())
                {
                    CharacteristicsStep5Controller.this.parentController.launchAction(5, ActionTypes.TURN_TO_NOISE, Characteristics.SELECTION_TYPE_ELEMENT);
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
