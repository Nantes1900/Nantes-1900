/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.view.isletprocess.CharacteristicsStep6View;

/**
 * Characteristics panel for the sixth step of process of an islet. TODO
 * @author Camille
 * @author Luc
 */
public class CharacteristicsStep6Controller extends CharacteristicsController
{
    public ArrayList<Surface> surfacesList;
    public Surface surfaceLocked;

    /**
     * Constructor.
     * @param parentController
     * @param triangleSelected
     */
    public CharacteristicsStep6Controller(
            IsletProcessController parentController, Surface surfaceLocked)
    {
        super(parentController);
        surfacesList = new ArrayList<Surface>();
        this.surfaceLocked = surfaceLocked;

        this.cView = new CharacteristicsStep6View();
        this.cView.getValidateButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                
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
