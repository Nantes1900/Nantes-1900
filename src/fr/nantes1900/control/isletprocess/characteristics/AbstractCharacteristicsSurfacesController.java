package fr.nantes1900.control.isletprocess.characteristics;

import java.util.ArrayList;

import fr.nantes1900.control.isletprocess.IsletProcessController;
import fr.nantes1900.models.extended.Surface;

/**
 * Abstract class for characteristics controller which handle surface list.
 * @author Camille Bouquet
 */
public abstract class AbstractCharacteristicsSurfacesController extends
        CharacteristicsController {

    /**
     * List of selected surfaces.
     */
    protected ArrayList<Surface> surfacesList;

    /**
     * Creates a new basic controller with the list of selected surfaces.
     * @param parentControllerIn
     *            the parent controller
     */
    public AbstractCharacteristicsSurfacesController(
            final IsletProcessController parentControllerIn) {
        super(parentControllerIn);
        this.surfacesList = new ArrayList<>();
    }

    /**
     * Creates a new basic controller with the list of selected triangles.
     * @param parentControllerIn
     *            the parent controller
     * @param surfaceSelected
     *            the selected surface
     */
    public AbstractCharacteristicsSurfacesController(
            final IsletProcessController parentControllerIn,
            final Surface surfaceSelected) {

        super(parentControllerIn);
        this.surfacesList = new ArrayList<>();
        this.surfacesList.add(surfaceSelected);
    }

    /**
     * Adds a surface to the selected surfaces.
     * @param surfaceSelected
     *            the surface to add
     */
    public void addSurfaceSelected(final Surface surfaceSelected) {
        this.surfacesList.add(surfaceSelected);
        modifyViewCharacteristics();
    }

    /**
     * Gets the surface list.
     * @return the surface list
     */
    public final ArrayList<Surface> getSurfaces() {
        return this.surfacesList;
    }

    /**
     * Updates the view when the list of selected surface is updated.
     */
    public abstract void modifyViewCharacteristics();

    /**
     * Removes a surface from the selected surfaces.
     * @param surfaceSelected
     *            the surface to remove
     */
    public void removeSurfaceSelected(final Surface surfaceSelected) {
        this.surfacesList.remove(surfaceSelected);
        modifyViewCharacteristics();
    }
}
