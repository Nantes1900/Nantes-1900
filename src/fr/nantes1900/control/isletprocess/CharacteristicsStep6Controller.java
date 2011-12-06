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
 * Characteristics panel for the sixth step of process of an islet.
 * @author Camille Bouquet
 * @author Luc Jallerat
 */
public class CharacteristicsStep6Controller extends
        AbstractCharacteristicsSurfacesController {

    /**
     * Tells if the surface is locked.
     */
    private boolean surfaceLocked = false;
    /**
     * Surface to display neighbors from.
     */
    private Surface surfaceToCheck;

    /**
     * Creates a new controller for the 6th step characteristics panel.
     * @param parentController
     *            the controller which handles this controller.
     * @param newSurface
     *            the new selected surface
     * @param neighbors
     *            the list of current neighbors
     */
    public CharacteristicsStep6Controller(
            IsletProcessController parentController, Surface newSurface,
            ArrayList<Surface> neighbors) {
        super(parentController);

        this.surfaceToCheck = newSurface;
        this.surfacesList = neighbors;
        this.cView = new CharacteristicsStep6View(neighbors);
        this.surfaceLocked = false;
        ((CharacteristicsStep6View) this.cView)
                .setModificationsEnabled(this.surfaceLocked);
        ((CharacteristicsStep6View) this.cView).getLockButton()
                .addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        // TODO tell the parent controller that the surface is locked
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
                surfacesList = ((CharacteristicsStep6View) cView).getList();
                CharacteristicsStep6Controller.this.parentController.getBiController().action6(surfaceToCheck, surfacesList);
                CharacteristicsStep6Controller.this.parentController
                        .refreshViews();
            }
        });
    }

    @Override
    public void addSurfaceSelected(Surface surfaceSelected) {
        if (this.surfaceLocked)
        {
            this.surfacesList.add(surfaceSelected);
            modifyViewCharacteristics();
        }
    }

    @Override
    public void removeSurfaceSelected(Surface surfaceSelected) {
        if (this.surfaceLocked)
        {
            surfacesList.remove(surfaceSelected);
            modifyViewCharacteristics();
        }
    }

    @Override
    public void modifyViewCharacteristics() {
        ((CharacteristicsStep6View) this.cView).setList(this.surfacesList);
    }
}
