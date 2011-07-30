package nantes1900.constants;

/**
 * Implement class to define some coefficients used in the algorithms.
 * 
 * @author Daniel Lefevre
 */
public final class SeparationFloorBuilding {

	// TODO : doc
	/**
	 * Error factor for the floor extraction algorithm.
	 */
	public static final double ALTITUDE_ERROR = 5;
	/**
	 * Angle factor considered as the maximum angle from the normal
	 * (gravity-oriented) to the floor that can describe a floor triangle.
	 */
	public static final double ANGLE_FLOOR_ERROR = 60;
	/**
	 * Coefficient used after the extraction of the building, to determine if a
	 * building has enough triangle to be considered as a real building.
	 */
	public static final double BLOCK_SIZE_ERROR = 1;

	/**
	 * Constructor.
	 */
	private SeparationFloorBuilding() {
	}

}
