package fr.nantes1900.constants;

/**
 * Contains some coefficients used in the algorithms.
 * @author Daniel Lefevre
 */
public final class SeparationGrounds
{

    /**
     * The default value of the coefficient.
     */
    private static final double BLOCK_GROUNDS_SIZE_ERROR_DEFAULT = 50;

    /**
     * Coefficient used after the extraction of the grounds, to determine if a
     * ground has enough triangles to be considered as a real one.
     */
    private static double       blockGroundsSizeError            = BLOCK_GROUNDS_SIZE_ERROR_DEFAULT;

    /**
     * Private constructor.
     */
    private SeparationGrounds()
    {
    }

    /**
     * Getter.
     * @return the block ground size error
     */
    public static double getBlockGroundsSizeError()
    {
        return blockGroundsSizeError;
    }

    /**
     * Setter.
     * @param blockGroundsSizeErrorIn
     *            the new block ground size error
     */
    public static void
            setBlockGroundsSizeError(final double blockGroundsSizeErrorIn)
    {
        SeparationGrounds.blockGroundsSizeError = blockGroundsSizeErrorIn;
    }
}
