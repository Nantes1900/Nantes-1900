package fr.nantes1900.models.exceptions;

/**
 * Exception thrown when the previous results don't match current step
 * expectations. Specific information are in the message.
 * 
 * @author Camille
 * 
 */
public class WeirdPreviousResultsException extends Exception {

	/**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * @param message
     *            the message
     */
    public WeirdPreviousResultsException(final String message) {
        super(message);
    }
}
