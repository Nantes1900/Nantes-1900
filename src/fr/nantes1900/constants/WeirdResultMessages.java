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
     * Key for a pop-up message when the matrix is not good.
     */
    public static final String BAD_MATRIX = "BadMatrix";
    /**
     * Key for a pop-up message when there is no ground and that there should
     * be.
     */
    public static final String NO_GROUND = "NoGround";
    /**
     * Key for a pop-up message when there is no noise and that there should be.
     */
    public static final String NO_NOISE = "NoNoise";
    /**
     * Key for a pop-up message when there is no buildings and that there should
     * be.
     */
    public static final String NO_BUILDINGS = "NoBuildings";
    /**
     * Key for a pop-up message when the initial ground is empty.
     */
    public static final String EMPTY_GROUND = "EmptyGround";
}
