package fr.nantes1900.control.isletprocess;

import java.util.ArrayList;

import fr.nantes1900.models.basis.Triangle;

public abstract class AbstractCharacteristicsTrianglesController extends
        CharacteristicsController {

    protected ArrayList<Triangle> trianglesList;

    public AbstractCharacteristicsTrianglesController(
            IsletProcessController parentController, Triangle triangleSelected) {
        super(parentController);
        trianglesList = new ArrayList<Triangle>();
        trianglesList.add(triangleSelected);
    }

    public void addTriangleSelected(Triangle triangleSelected) {
        if (!trianglesList.contains(triangleSelected))
        {
            this.trianglesList.add(triangleSelected);
            modifyViewCharacteristics();
        }
    }

    public abstract void modifyViewCharacteristics();

    public boolean removeTriangleSelected(Triangle triangleSelected) {
        trianglesList.remove(triangleSelected);
        modifyViewCharacteristics();
        return trianglesList.isEmpty();
    }

    public ArrayList<Triangle> getTriangles() {
        return this.trianglesList;
    }
}
