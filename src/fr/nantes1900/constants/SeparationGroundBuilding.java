package fr.nantes1900.constants;

/**
 * Contains some coefficients used in the algorithms.
 * @author Daniel Lefevre
 */
public final class SeparationGroundBuilding
{

    /**
     * Error factor for the altitude check. If the difference of altitude
     * (considering the normal to the ground, and not the gravity) between a
     * block of triangles and the totality of the grounds is higher than this
     * error, the block is not taken as a ground. This error is in the scale
     * used in the files (if millimeteres are used, it's in millimeters, etc.).
     */
    public static double ALTITUDE_ERROR           = 0.2;

    /**
     * Error factor for the first step of the extraction of the grounds : it is
     * the maximum angle from the normal (gravity-oriented) to the ground that
     * can describe a ground triangle (in the first step). In degrees.
     */
    public static double ANGLE_GROUND_ERROR       = 8;

    /**
     * Angle factor considered as the maximum angle from the normal
     * (gravity-oriented) to the ground that can describe a ground triangle. In
     * degrees.
     */
    public static double LARGE_ANGLE_GROUND_ERROR = 60;

    /**
     * Private constructor.
     */
    private SeparationGroundBuilding()
    {
    }

}
