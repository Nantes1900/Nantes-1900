package fr.nantes1900.constants;

/**
 * Constants class describing the messages sent to the user when a result is
 * weird : no ground, no roof, or something really wrong.
 * @author Daniel Lefevre
 */
public final class WeirdResultMessages {

    /**
     * Private constructor.
     */
    private WeirdResultMessages() {
    }

    /**
     * Pop-up message when the matrix is not good.
     */
    public static final String BAD_MATRIX = "Error : the matrix is baldy formed ! You must close the program.";
    /**
     * Pop-up message when there is no ground and that there should be.
     */
    public static final String NO_GROUND = "Warning : the process didn't find any ground. This must be an error. You should come back to the previous process and check.";
    /**
     * Pop-up message when there is no noise and that there should be.
     */
    public static final String NO_NOISE = "Warning : the process didn't find any noise. This must be an error. You should come back to the previous process and check.";
    /**
     * Pop-up message when there is no buildings and that there should be.
     */
    public static final String NO_BUILDINGS = "Warning : the buildings are empty. This must be an error. You should come back to the previous process and check.";
}
