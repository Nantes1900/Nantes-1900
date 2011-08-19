package fr.nantes1900.constants;

/**
 * Contains some coefficients used in the algorithms.
 * 
 * @author Daniel Lefevre
 */
public final class SeparationFloorBuilding {

    /**
     * Error factor for the altitude check. If the difference of altitude
     * (considering the normal to the floor, and not the gravity) between a
     * block of triangles and the totality of the floors is higher than this
     * error, the block is not taken as a floor. This error is in the scale used
     * in the files (if millimeteres are used, it's in millimeters, etc.).
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
