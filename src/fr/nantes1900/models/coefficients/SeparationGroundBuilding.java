package fr.nantes1900.models.coefficients;

/**
 * Contains some coefficients used in the algorithms.
 * @author Daniel Lefevre
 */
public final class SeparationGroundBuilding {

    /**
     * The default value of the coefficient.
     */
    private static final double BLOCK_GROUNDS_SIZE_ERROR_DEFAULT = 50;

    /**
     * Coefficient used after the extraction of the grounds, to determine if a
     * ground has enough TRIANGLES to be considered as a real one.
     */
    private static double blockGroundsSizeError = BLOCK_GROUNDS_SIZE_ERROR_DEFAULT;

    /**
     * Default value of the coefficient.
     */
    private static final double ALTITUDE_ERROR_DEFAULT = 0.2;

    /**
     * Error factor for the altitude check. If the difference of altitude
     * (considering the normal to the ground, and not the gravity) between a
     * block of TRIANGLES and the totality of the grounds is higher than this
     * error, the block is not taken as a ground. This error is in the scale
     * used in the files (if millimeters are used, it's in millimeters, etc.).
     */
    private static double altitureError = ALTITUDE_ERROR_DEFAULT;

    /**
     * Default value of the coefficient.
     */
    private static final double ANGLE_GROUND_ERROR_DEFAULT = 8;
    /**
     * Error factor for the first step of the extraction of the grounds : it is
     * the maximum angle from the normal (gravity-oriented) to the ground that
     * can describe a ground triangle (in the first step). In degrees.
     */
    private static double angleGroundError = ANGLE_GROUND_ERROR_DEFAULT;

    /**
     * Default value of the coefficient.
     */
    private static final double LARGE_ANGLE_GROUND_ERROR_DEFAULT = 60;
    /**
     * Angle factor considered as the maximum angle from the normal
     * (gravity-oriented) to the ground that can describe a ground triangle. In
     * degrees.
     */
    private static double largeAngleGroundError = LARGE_ANGLE_GROUND_ERROR_DEFAULT;

    /**
     * Private constructor.
     */
    private SeparationGroundBuilding() {
    }

    /**
     * Getter.
     * @return the altitude error
     */
    public static double getAltitureError() {
        return altitureError;
    }

    /**
     * Getter.
     * @return the angle ground error
     */
    public static double getAngleGroundError() {
        return angleGroundError;
    }

    /**
     * Getter.
     * @return the block ground size error
     */
    public static double getBlockGroundsSizeError() {
        return blockGroundsSizeError;
    }

    /**
     * Getter.
     * @return the large angle ground error
     */
    public static double getLargeAngleGroundError() {
        return largeAngleGroundError;
    }

    /**
     * Setter.
     * @param altitureErrorIn
     *            the new altitude error
     */
    public static void setAltitureError(final double altitureErrorIn) {
        SeparationGroundBuilding.altitureError = altitureErrorIn;
    }

    /**
     * Setter.
     * @param angleGroundErrorIn
     *            the new angle ground error
     */
    public static void setAngleGroundError(final double angleGroundErrorIn) {
        SeparationGroundBuilding.angleGroundError = angleGroundErrorIn;
    }

    /**
     * Setter.
     * @param blockGroundsSizeErrorIn
     *            the new block ground size error
     */
    public static void setBlockGroundsSizeError(
            final double blockGroundsSizeErrorIn) {
        SeparationGroundBuilding.blockGroundsSizeError = blockGroundsSizeErrorIn;
    }

    /**
     * Setter.
     * @param largeAngleGroundErrorIn
     *            the new large angle ground error
     */
    public static void setLargeAngleGroundError(
            final double largeAngleGroundErrorIn) {
        SeparationGroundBuilding.largeAngleGroundError = largeAngleGroundErrorIn;
    }

}
