package fr.nantes1900.constants;

/**
 * Implement class to define some coefficients used in the algorithms.
 * 
 * @author Daniel Lefevre
 */
public final class SeparationTreatmentWallsRoofs {

    // If there is a matrix exception durint the findEdges, then try to increase
    // the wallFactor.
    /**
     * TODO .
     */
    public static final double ANGLE_WALL_ERROR = 15;
    /**
     * TODO .
     */
    public static final double ANGLE_ROOF_ERROR = 15;
    /**
     * TODO .
     */
    public static final double LARGE_ANGLE_ERROR = 30;
    /**
     * TODO .
     */
    public static final double NORMALTO_ERROR = 0.2;
    /**
     * TODO .
     */
    public static final double WALL_SIZE_ERROR = 5;
    /**
     * TODO .
     */
    public static final double ROOF_SIZE_ERROR = 10;

    /**
     * Private constructor.
     */
    private SeparationTreatmentWallsRoofs() {
    }
}
