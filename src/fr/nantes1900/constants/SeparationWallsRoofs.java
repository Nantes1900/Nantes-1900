package fr.nantes1900.constants;

/**
 * Contains some coefficients used in the algorithms.
 * @author Daniel Lefevre
 */
public class SeparationWallsRoofs {

    /**
     * Error factor : coefficient compared to a dot product to determine if a
     * triangle normal is normal to the ground (to determine if it's a wall).
     * Not in degrees !
     */
    public static double NORMALTO_ERROR = 0.2;
}
