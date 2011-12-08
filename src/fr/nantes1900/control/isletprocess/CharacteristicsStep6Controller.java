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
 * @author Camille Bouquet, Luc Jallerat
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
     * @param parentControllerIn
     *            the controller which handles this controller.
     * @param newSurface
     *            the new selected surface
     * @param neighbors
     *            the list of current neighbors
     */
    public CharacteristicsStep6Controller(
            final IsletProcessController parentControllerIn,
            final Surface newSurface, final ArrayList<Surface> neighbors) {
        super(parentControllerIn);

        this.surfaceToCheck = newSurface;
        this.surfacesList = neighbors;
        this.cView = new CharacteristicsStep6View(neighbors);
        this.surfaceLocked = false;
        ((CharacteristicsStep6View) this.cView)
                .setModificationsEnabled(this.surfaceLocked);
        ((CharacteristicsStep6View) this.cView).getLockButton()
                .addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent arg0) {
                        // TODO tell the parent controller that the surface is
                        // locked
                        // FIXME : weird
                        surfaceLocked = !surfaceLocked;
                        JButton source = ((JButton) arg0.getSource());
                        if (surfaceLocked) {
                            source.setText("Unlock");
                            source.setToolTipText(FileTools
                                    .readElementText(TextsKeys.KEY_LOCKMESH));
                            CharacteristicsStep6Controller.this.parentController
                                    .lock(true);
                        } else {
                            source.setText("Lock");
                            source.setToolTipText(FileTools
                                    .readElementText(TextsKeys.KEY_UNLOCKMESH));
                            CharacteristicsStep6Controller.this.parentController
                                    .lock(false);
                        }
                        ((CharacteristicsStep6View) cView)
                                .setModificationsEnabled(surfaceLocked);
                    }

                });

        this.cView.getValidateButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                surfacesList = ((CharacteristicsStep6View) cView).getList();
                parentController.getBiController().action6(surfaceToCheck,
                        surfacesList);
                parentController.refreshViews();
            }
        });
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.control.isletprocess.AbstractCharacteristicsSurfacesController
     * #addSurfaceSelected(fr.nantes1900.models.extended.Surface)
     */
    @Override
    public final void addSurfaceSelected(final Surface surfaceSelected) {
        if (this.surfaceLocked) {
            this.surfacesList.add(surfaceSelected);
            modifyViewCharacteristics();
            System.out.println("Voisin ajoutée");
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.control.isletprocess.AbstractCharacteristicsSurfacesController
     * #removeSurfaceSelected(fr.nantes1900.models.extended.Surface)
     */
    @Override
    public final void removeSurfaceSelected(final Surface surfaceSelected) {
        if (this.surfaceLocked) {
            this.surfacesList.remove(surfaceSelected);
            modifyViewCharacteristics();
            System.out.println("Voisin enlevée");
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.control.isletprocess.AbstractCharacteristicsSurfacesController
     * #modifyViewCharacteristics()
     */
    @Override
    public final void modifyViewCharacteristics() {
        ((CharacteristicsStep6View) this.cView).setList(this.surfacesList);
    }

    /**
     * Tells if the surface is locked or not.
     * @return true - the surface is locked\n false - the surface is not locked
     */
    public final boolean isSurfaceLocked() {
        return this.surfaceLocked;
    }
}
