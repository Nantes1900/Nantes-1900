package fr.nantes1900.constants;

/**
 * Implements a class to define some coefficients used in the algorithms.
 * 
 * @author Daniel Lefevre
 */
public final class SeparationTreatmentWallsRoofs {

    /**
     * Error factor : maximum angle between two triangles to determine if they
     * belong to the same surface. In degrees.
     */
    public static final double WALL_ANGLE_ERROR = 10;
    public static final double ROOF_ANGLE_ERROR = 7.5;
    /**
     * Error factor during the noise treatment : maximum angle between two
     * triangles to dertermine that they belong to the same wall or roof. In
     * degrees.
     */
    public static final double LARGE_ANGLE_ERROR = 40;

    public static final double MIDDLE_ANGLE_ERROR = 25;
    /**
     * Error factor : coefficient compared to a dot product to determine if a
     * triangle normal is normal to the floor. Not in degrees !
     */
    public static final double NORMALTO_ERROR = 0.2;
    /**
     * Error factor : minimum triangle number for a block to be considered as a
     * real wall.
     */
    public static final double WALL_SIZE_ERROR = 100;
    /**
     * Error factor : minimum triangle number for a block to be considered as a
     * real roof.
     */
    public static final double ROOF_SIZE_ERROR = 100;

    public static final double PLANES_ERROR = 1;

    /**
     * Private constructor.
     */
    private SeparationTreatmentWallsRoofs() {
    }
}
