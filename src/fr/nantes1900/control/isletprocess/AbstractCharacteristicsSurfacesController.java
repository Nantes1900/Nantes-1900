package fr.nantes1900.control.isletprocess;

import java.util.ArrayList;

import fr.nantes1900.models.extended.Surface;

/**
 * Abstract class for characteristics controller which handle surface list.
 * @author Camille
 */
public abstract class AbstractCharacteristicsSurfacesController extends
        CharacteristicsController {

    /**
     * List of selected surfaces.
     */
    protected ArrayList<Surface> surfacesList;

    /**
     * Creates a new basic controller with the list of selected surfaces.
     * @param parentController
     *            the parent controller
     */
    public AbstractCharacteristicsSurfacesController(
            IsletProcessController parentController) {
        super(parentController);
        surfacesList = new ArrayList<Surface>();
    }

    /**
     * Creates a new basic controller with the list of selected triangles.
     * @param parentController
     *            the parent controller
     * @param surfaceSelected
     *            the selected surface
     */
    public AbstractCharacteristicsSurfacesController(
            IsletProcessController parentController, Surface surfaceSelected) {
        super(parentController);
        surfacesList = new ArrayList<Surface>();
        surfacesList.add(surfaceSelected);
    }

    /**
     * Adds a surface to the selected surfaces.
     * @param surfaceSelected
     *            the surface to add
     */
    public void addSurfaceSelected(Surface surfaceSelected) {
        this.surfacesList.add(surfaceSelected);
        modifyViewCharacteristics();
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
    public void removeSurfaceSelected(Surface surfaceSelected) {
        surfacesList.remove(surfaceSelected);
        modifyViewCharacteristics();
    }

    /**
     * Gets the surface list.
     * @return
     *         the surface list
     */
    public ArrayList<Surface> getSurfaces() {
        return this.surfacesList;
    }
}
