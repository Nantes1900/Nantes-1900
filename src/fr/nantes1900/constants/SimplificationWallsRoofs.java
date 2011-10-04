package fr.nantes1900.constants;

/**
 * Contains some coefficients used in the algorithms.
 * @author Daniel Lefevre
 */
public class SimplificationWallsRoofs {

    /**
     * Minimum angle between two neighbours of a surface. Used in the edges
     * search during the vectorization of the surfaces. If two neighbours have
     * the same orientation, the intersection of them during the vectorization
     * will often creates aberrant points.
     */
    public static double IS_ORIENTED_FACTOR = 30;

}
