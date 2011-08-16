package fr.nantes1900.constants;

/**
 * Implements a class to define some coefficients used in the algorithms.
 * 
 * @author Daniel Lefevre
 */
public final class SeparationFloorBuilding {

    /**
     * Error factor for the floor extraction algorithm.
     */
    public static final double ALTITUDE_ERROR = 0.2;
    /**
     * Error factor for the first step of the extraction of the floors : it is
     * the maximum angle from the normal (gravity-oriented) to the floor that
     * can describe a floor triangle (in the first step). In degrees.
     */
    public static final double ANGLE_FLOOR_ERROR = 8;
    /**
     * Angle factor considered as the maximum angle from the normal
     * (gravity-oriented) to the floor that can describe a floor triangle. In
     * degrees.
     */
    public static final double LARGE_ANGLE_FLOOR_ERROR = 60;
    /**
     * Coefficient used after the extraction of the building, to determine if a
     * building has enough triangles to be considered as a real building.
     */
    public static final double BLOCK_BUILDING_SIZE_ERROR = 500;
    /**
     * Coefficient used after the extraction of the floors, to determine if a
     * floor has enough triangles to be considered as a real floor.
     */
    public static final double BLOCK_FLOORS_SIZE_ERROR = 50;

    /**
     * Private constructor.
     */
    private SeparationFloorBuilding() {
    }

}
