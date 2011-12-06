package fr.nantes1900.models.islets.buildings.exceptions;

/**
 * Exception thrown when something is going wrong in the current step. The
 * explanation is put in the message of the Exception.
 * @author Daniel Lefèvre
 */
public class WeirdResultException extends Exception {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * @param message
     *            the message
     */
    public WeirdResultException(final String message) {
        super(message);
    }
}
