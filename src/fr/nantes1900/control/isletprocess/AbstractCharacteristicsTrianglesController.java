package fr.nantes1900.control.isletprocess;

import java.util.ArrayList;
import java.util.List;

import fr.nantes1900.models.basis.Triangle;

/**
 * Abstract class for triangles selection conrollers.
 * @author Camille
 */
public abstract class AbstractCharacteristicsTrianglesController extends
        CharacteristicsController {

    /**
     * List of selected triangles.
     */
    protected ArrayList<Triangle> trianglesList;

    /**
     * Creates a new basic controller with the list of selected triangles.
     * @param parentController
     *            the parent controller
     * @param trianglesSelected
     *            the list of selected triangles
     */
    public AbstractCharacteristicsTrianglesController(
            IsletProcessController parentController,
            List<Triangle> trianglesSelected) {
        super(parentController);
        trianglesList = (ArrayList<Triangle>) trianglesSelected;
    }

    /**
     * Modifies the characteristics panel when the list is updated.
     */
    public abstract void modifyViewCharacteristics();

    /**
     * Gets the list of selected triangles.
     * @return
     *         the list of selected triangles.
     */
    public ArrayList<Triangle> getTriangles() {
        return this.trianglesList;
    }
}
