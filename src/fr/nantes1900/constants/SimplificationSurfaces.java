package fr.nantes1900.constants;

/**
 * Contains some coefficients used in the algorithms.
 * @author Daniel Lefevre
 */
public final class SimplificationSurfaces
{

    /**
     * The default value of the coefficient.
     */
    private static final double IS_ORIENTED_FACTOR_DEFAULT = 30;

    /**
     * Minimum angle between two neighbours of a surface. Used in the edges
     * search during the vectorization of the surfaces. If two neighbours have
     * the same orientation, the intersection of them during the vectorization
     * will often creates aberrant points.
     */
    private static double isOrientedFactor = IS_ORIENTED_FACTOR_DEFAULT;

    /**
     * Private constructor.
     */
    private SimplificationSurfaces()
    {
    }

    /**
     * Getter.
     * @return the is oriented factor
     */
    public static double getIsOrientedFactor()
    {
        return isOrientedFactor;
    }

    /**
     * Setter.
     * @param isOrientedFactorIn
     *            the new is oriented factor
     */
    public static void setIsOrientedFactor(final double isOrientedFactorIn)
    {
        SimplificationSurfaces.isOrientedFactor = isOrientedFactorIn;
    }
}
