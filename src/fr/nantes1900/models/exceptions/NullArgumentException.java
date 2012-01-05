package fr.nantes1900.models.exceptions;

/**
 * Implements an exception used when an process is launched while some arguments
 * have not been initialized.
 * @author Daniel Lefèvre
 */
public class NullArgumentException extends Exception {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Consctructor.
     */
    public NullArgumentException() {
    }
}
