package fr.nantes1900.constants;

/**
 * Contains some coefficients used in the algorithms.
 * @author Daniel Lefevre
 */
/**
 * @author Daniel
 */
public final class SeparationWallsSeparationRoofs
{

    /**
     * The default value of the coefficient.
     */
    private static final double WALL_ANGLE_ERROR_DEFAULT = 10;

    /**
     * Error factor for the walls : maximum angle between two triangles to
     * determine if they belong to the same surface. In degrees.
     */
    private static double wallAngleError = WALL_ANGLE_ERROR_DEFAULT;

    /**
     * The default value of the coefficient.
     */
    private static final double ROOF_ANGLE_ERROR_DEFAULT = 7.5;

    /**
     * Error factor for the roofs : maximum angle between two triangles to
     * determine if they belong to the same surface. In degrees.
     */
    private static double roofAngleError = ROOF_ANGLE_ERROR_DEFAULT;

    /**
     * Setter.
     * @param roofAngleErrorIn
     *            the new roof angle error
     */
    public static void setRoofAngleError(final double roofAngleErrorIn)
    {
        SeparationWallsSeparationRoofs.roofAngleError = roofAngleErrorIn;
    }

    /**
     * Setter.
     * @param middleAngleErrorIn
     *            the new middle angle error
     */
    public static void setMiddleAngleError(final double middleAngleErrorIn)
    {
        SeparationWallsSeparationRoofs.middleAngleError = middleAngleErrorIn;
    }

    /**
     * Setter.
     * @param largeAngleErrorIn
     *            the new large angle error
     */
    public static void setLargeAngleError(final double largeAngleErrorIn)
    {
        SeparationWallsSeparationRoofs.largeAngleError = largeAngleErrorIn;
    }

    /**
     * Setter.
     * @param wallSizeErrorIn
     *            the new wall size error
     */
    public static void setWallSizeError(final double wallSizeErrorIn)
    {
        SeparationWallsSeparationRoofs.wallSizeError = wallSizeErrorIn;
    }

    /**
     * Setter.
     * @param roofSizeErrorIn
     *            the new roof size error
     */
    public static void setRoofSizeError(final double roofSizeErrorIn)
    {
        SeparationWallsSeparationRoofs.roofSizeError = roofSizeErrorIn;
    }

    /**
     * Setter.
     * @param planesErrorIn
     *            the new planes error
     */
    public static void setPlanesError(final double planesErrorIn)
    {
        SeparationWallsSeparationRoofs.planesError = planesErrorIn;
    }

    /**
     * The default value of the coefficient.
     */
    private static final double MIDDLE_ANGLE_ERROR_DEFAULT = 25;

    /**
     * Error factor used during the process of the new neighbours : if the
     * angle between two surfaces which are neighbours is lesser than this
     * error, then they are merged to form only one wall.
     */
    private static double middleAngleError = MIDDLE_ANGLE_ERROR_DEFAULT;

    /**
     * The default value of the coefficient.
     */
    private static final double LARGE_ANGLE_ERROR_DEFAULT = 40;

    /**
     * Error factor used during the noise process : maximum angle between two
     * triangles to determine that they belong to the same wall or roof. In
     * degrees.
     */
    private static double largeAngleError = LARGE_ANGLE_ERROR_DEFAULT;

    /**
     * The default value of the coefficient.
     */
    private static final double WALL_SIZE_ERROR_DEFAULT = 40;

    /**
     * Error factor for the walls : minimum number of triangles for a block to
     * be considered as a real wall.
     */
    private static double wallSizeError = WALL_SIZE_ERROR_DEFAULT;

    /**
     * The default value of the coefficient.
     */
    private static final double ROOF_SIZE_ERROR_DEFAULT = 100;

    /**
     * Error factor for the roofs : minimum number of triangles for a block to
     * be considered as a real roof.
     */
    private static double roofSizeError = ROOF_SIZE_ERROR_DEFAULT;

    /**
     * The default value of the coefficient.
     */
    private static final double PLANES_ERROR_DEFAULT = 1;

    /**
     * Error factor used in the cutting of the walls and roofs. When a surface
     * is extracted, the algorithm take only the triangles that belong to two
     * planes, parallel to the surface, spaced of this factor from the surface.
     */
    private static double planesError = PLANES_ERROR_DEFAULT;

    /**
     * Private constructor.
     */
    private SeparationWallsSeparationRoofs()
    {
    }

    /**
     * Getter.
     * @return the large angle error
     */
    public static double getLargeAngleError()
    {
        return largeAngleError;
    }

    /**
     * Getter.
     * @return the middle angle error
     */
    public static double getMiddleAngleError()
    {
        return middleAngleError;
    }

    /**
     * Getter.
     * @return the planes error
     */
    public static double getPlanesError()
    {
        return planesError;
    }

    /**
     * Getter.
     * @return the roof angle error
     */
    public static double getRoofAngleError()
    {
        return roofAngleError;
    }

    /**
     * Getter.
     * @return the roof size error
     */
    public static double getRoofSizeError()
    {
        return roofSizeError;
    }

    /**
     * Getter.
     * @return the wall angle error
     */
    public static double getWallAngleError()
    {
        return wallAngleError;
    }

    /**
     * Getter.
     * @return the wall size error
     */
    public static double getWallSizeError()
    {
        return wallSizeError;
    }

    /**
     * Setter.
     * @param wallAngleErrorIn
     *            the new wall angle error
     */
    public static void setWallAngleError(final double wallAngleErrorIn)
    {
        SeparationWallsSeparationRoofs.wallAngleError = wallAngleErrorIn;
    }
}
