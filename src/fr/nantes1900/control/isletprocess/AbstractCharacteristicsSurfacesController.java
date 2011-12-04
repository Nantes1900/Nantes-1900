package fr.nantes1900.control.isletprocess;

import java.util.ArrayList;

import fr.nantes1900.models.extended.Surface;

public abstract class AbstractCharacteristicsSurfacesController extends
        CharacteristicsController {

    protected ArrayList<Surface> surfacesList;

    public AbstractCharacteristicsSurfacesController(
            IsletProcessController parentController) {
        super(parentController);
        surfacesList = new ArrayList<Surface>();
    }

    public AbstractCharacteristicsSurfacesController(
            IsletProcessController parentController, Surface surfaceSelected) {
        super(parentController);
        surfacesList = new ArrayList<Surface>();
        surfacesList.add(surfaceSelected);
    }

    public void addSurfaceSelected(Surface surfaceSelected) {
        this.surfacesList.add(surfaceSelected);
        modifyViewCharacteristics();
    }

    public abstract void modifyViewCharacteristics();

    public boolean removeSurfaceSelected(Surface surfaceSelected) {
        surfacesList.remove(surfaceSelected);
        modifyViewCharacteristics();
        return surfacesList.isEmpty();
    }
    
    public ArrayList<Surface> getSurfaces() {
        return this.surfacesList;
    }
}
