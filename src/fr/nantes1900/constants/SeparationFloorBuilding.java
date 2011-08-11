package fr.nantes1900.constants;

/**
 * Implement class to define some coefficients used in the algorithms.
 * 
 * @author Daniel Lefevre
 */
public final class SeparationFloorBuilding {

    /**
     * Error factor for the floor extraction algorithm.
     */
    public static final double ALTITUDE_ERROR = 0.5;
    /**
     * TODO.
     */
    public static final double ANGLE_FLOOR_ERROR = 5;
    /**
     * Angle factor considered as the maximum angle from the normal
     * (gravity-oriented) to the floor that can describe a floor triangle.
     */
    public static final double LARGE_ANGLE_FLOOR_ERROR = 60;
    /**
     * Coefficient used after the extraction of the building, to determine if a
     * building has enough triangle to be considered as a real building.
     */
    public static final double BLOCK_BUILDING_SIZE_ERROR = 500;
    /**
     * Coefficient used after the extraction of the building, to determine if a
     * building has enough triangle to be considered as a real building.
     */
    public static final double BLOCK_FLOORS_SIZE_ERROR = 100;

    /**
     * Constructor.
     */
    private SeparationFloorBuilding() {
    }

}
