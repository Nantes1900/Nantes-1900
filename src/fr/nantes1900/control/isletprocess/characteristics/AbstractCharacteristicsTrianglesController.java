package fr.nantes1900.control.isletprocess.characteristics;

import java.util.List;

import fr.nantes1900.control.isletprocess.IsletProcessController;
import fr.nantes1900.models.basis.Triangle;

/**
 * Abstract class for triangles selection conrollers.
 * @author Camille Bouquet
 */
public abstract class AbstractCharacteristicsTrianglesController extends
        CharacteristicsController {

    /**
     * List of selected triangles.
     */
    protected List<Triangle> trianglesList;

    /**
     * Creates a new basic controller with the list of selected triangles.
     * @param parentControllerIn
     *            the parent controller
     * @param trianglesSelected
     *            the list of selected triangles
     */
    public AbstractCharacteristicsTrianglesController(
            final IsletProcessController parentControllerIn,
            final List<Triangle> trianglesSelected) {
        super(parentControllerIn);
        this.trianglesList = trianglesSelected;
    }

    /**
     * Gets the list of selected triangles.
     * @return the list of selected triangles.
     */
    public final List<Triangle> getTriangles() {
        return this.trianglesList;
    }

    /**
     * Modifies the characteristics panel when the list is updated.
     */
    public abstract void modifyViewCharacteristics();
}
