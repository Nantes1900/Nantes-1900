package fr.nantes1900.models.extended;

import java.util.List;

/**
 * Interface to get easier components of a building.
 * @author Camille
 */
public interface BuildingAccess {
	/**
	 * Gets the wall of the building for the current step.
	 * If walls have not already been cuts, returns an empty list.
	 * @param
	 * 		number of the step we want the walls of.
	 * @return
	 * 		the walls of the building the for the current step.
	 */
	public List<Wall> getWalls(int step);
	
	/**
	 * Gets the roofs of the building for the current step.
	 * If roofs have not already been cuts, returns an empty list.
	 * @param
	 * 		number of the step we want the roofs of.
	 * @return
	 * 		the roofs of the building for the current step.
	 */
	public List<Roof> getRoofs(int step);
}
