package fr.nantes1900.constants;

/**
 * Contains some coefficients used in the algorithms.
 * @author Daniel Lefevre
 */
public final class SeparationWallRoof
{

    /**
     * The default value of the coefficient.
     */
    private static final double NORMALTO_ERROR_DEFAULT = 0.2;

    /**
     * Error factor : coefficient compared to a dot product to determine if a
     * triangle normal is normal to the ground (to determine if it's a wall).
     * Not in degrees !
     */
    private static double       normalToError          = NORMALTO_ERROR_DEFAULT;

    /**
     * Private constructor.
     */
    private SeparationWallRoof()
    {
    }

    /**
     * Getter.
     * @return the normalTo error
     */
    public static double getNormalToError()
    {
        return normalToError;
    }

    /**
     * Getter.
     * @param normalToErrorIn
     *            the new normalTo error
     */
    public static void setNormalToError(final double normalToErrorIn)
    {
        SeparationWallRoof.normalToError = normalToErrorIn;
    }
}
