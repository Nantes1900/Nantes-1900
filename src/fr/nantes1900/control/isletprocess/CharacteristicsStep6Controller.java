/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.CharacteristicsStep6View;

/**
 * Characteristics panel for the sixth step of process of an islet. TODO
 * @author Camille
 * @author Luc
 */
public class CharacteristicsStep6Controller extends
        AbstractCharacteristicsSurfacesController {

    private boolean surfaceLocked = false;
    private Surface surfaceToCheck;

    /**
     * Constructor.
     * @param parentController
     * @param triangleSelected
     */
    public CharacteristicsStep6Controller(
            IsletProcessController parentController, Surface newSurface,
            ArrayList<Surface> neighbours) {
        super(parentController);

        this.surfaceToCheck = newSurface;
        this.surfacesList = neighbours;
        this.cView = new CharacteristicsStep6View(neighbours);
        this.surfaceLocked = false;
        ((CharacteristicsStep6View) this.cView)
                .setModificationsEnabled(this.surfaceLocked);
        ((CharacteristicsStep6View) this.cView).getLockButton()
                .addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        CharacteristicsStep6Controller.this.surfaceLocked = !CharacteristicsStep6Controller.this.surfaceLocked;
                        JButton source = ((JButton) arg0.getSource());
                        if (CharacteristicsStep6Controller.this.surfaceLocked)
                        {
                            source.setText("Unlock");
                            source.setToolTipText(FileTools
                                    .readElementText(TextsKeys.KEY_LOCKMESH));
                        } else
                        {
                            source.setText("Lock");
                            source.setToolTipText(FileTools
                                    .readElementText(TextsKeys.KEY_UNLOCKMESH));
                        }
                        ((CharacteristicsStep6View) CharacteristicsStep6Controller.this.cView)
                                .setModificationsEnabled(CharacteristicsStep6Controller.this.surfaceLocked);
                    }

                });

        this.cView.getValidateButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

            }
        });
    }

    public ArrayList<Surface> getSurfaces() {
        return this.surfacesList;
    }

    @Override
    public void modifyViewCharacteristics() {
        ((CharacteristicsStep6View) this.cView).setList(this.surfacesList);
    }
}
