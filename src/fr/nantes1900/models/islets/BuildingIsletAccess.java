package fr.nantes1900.models.islets;

import java.util.List;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;

/**
 * Interface to get easier components of a building islet without dealing with
 * steps.
 * @author Camille
 */
public interface BuildingIsletAccess {

    /**
     * Gets the buildings of the current step. If no buildings are defined for
     * the current step, the list will be empty.
     * @return list of all buildings in the current step.
     */
    List<Building> getBuildings();

    /**
     * Gets the ground of the current step. If no ground is defined for the
     * current step, the return value will be null.
     * @return the ground of the current step.
     */
    Ground getGround();
}
