package fr.nantes1900.control.isletprocess;

import java.util.ArrayList;
import java.util.List;

import fr.nantes1900.models.basis.Triangle;

public abstract class AbstractCharacteristicsTrianglesController extends
        CharacteristicsController {

    protected ArrayList<Triangle> trianglesList;

    public AbstractCharacteristicsTrianglesController(
            IsletProcessController parentController, List<Triangle> trianglesSelected) {
        super(parentController);
        trianglesList = (ArrayList<Triangle>) trianglesSelected;
    }

    public abstract void modifyViewCharacteristics();

    public ArrayList<Triangle> getTriangles() {
        return this.trianglesList;
    }
}
