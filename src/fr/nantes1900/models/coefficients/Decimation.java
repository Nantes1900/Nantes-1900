package fr.nantes1900.models.coefficients;

/**
 * Contains one coefficient for the decimation.
 * @author Daniel Lefevre
 */
public final class Decimation {

    /**
     * The default value of the coefficient.
     */
    private static final double PERCENT_DECIMATION = 0;

    /**
     * Coefficient used in the decimation. It is the percentage of the number of
     * the triangles before and after the decimation.
     */
    private static double percentDecimation = PERCENT_DECIMATION;

    /**
     * Private constructor.
     */
    private Decimation() {
    }

    /**
     * Getter.
     * @return the percent decimation
     */
    public static double getPercentDecimation() {
        return percentDecimation;
    }

    /**
     * Setter.
     * @param percentDecimationIn
     *            the percent decimation
     */
    public static void setPercentDecimation(final double percentDecimationIn) {
        Decimation.percentDecimation = percentDecimationIn;
    }
}
