package fr.nantes1900.constants;

/**
 * Contains some coefficients used in the algorithms.
 * 
 * @author Daniel Lefevre
 */
public final class SeparationTreatmentWallsRoofs {

    /**
     * Error factor for the walls : maximum angle between two triangles to
     * determine if they belong to the same surface. In degrees.
     */
    public static double WALL_ANGLE_ERROR = 10;

    /**
     * Error factor for the roofs : maximum angle between two triangles to
     * determine if they belong to the same surface. In degrees.
     */
    public static double ROOF_ANGLE_ERROR = 7.5;

    /**
     * Error factor used during the treatment of the new neighbours : if the
     * angle between two surfaces which are neighbours is lesser than this
     * error, then they are merged to form only one wall.
     */
    public static double MIDDLE_ANGLE_ERROR = 25;

    /**
     * Error factor used during the noise treatment : maximum angle between two
     * triangles to determine that they belong to the same wall or roof. In
     * degrees.
     */
    public static double LARGE_ANGLE_ERROR = 40;

    /**
     * Error factor : coefficient compared to a dot product to determine if a
     * triangle normal is normal to the ground (to determine if it's a wall).
     * Not in degrees !
     */
    public static double NORMALTO_ERROR = 0.2;

    /**
     * Error factor for the walls : minimum number of triangles for a block to
     * be considered as a real wall.
     */
    public static double WALL_SIZE_ERROR = 100;

    /**
     * Error factor for the roofs : minimum number of triangles for a block to
     * be considered as a real roof.
     */
    public static double ROOF_SIZE_ERROR = 100;

    /**
     * Error factor used in the cutting of the walls and roofs. When a surface
     * is extracted, the algorithm take only the triangles that belong to two
     * planes, parallel to the surface, spaced of this factor from the surface.
     */
    public static double PLANES_ERROR = 1;

    /**
     * Minimum angle between two neighbours of a surface. Used in the edges
     * search during the vectorization of the surfaces. If two neighbours have
     * the same orientation, the intersection of them during the vectorization
     * will often creates aberrant points.
     */
    public static double IS_ORIENTED_FACTOR = 30;

    /**
     * Private constructor.
     */
    private SeparationTreatmentWallsRoofs() {
    }
}
