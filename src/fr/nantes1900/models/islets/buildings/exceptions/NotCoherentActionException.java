package fr.nantes1900.models.islets.buildings.exceptions;

/**
 * Implements an exception if a method actionN has found some incoherence during
 * its execution.
 * @author Daniel Lefevre
 */
public final class NotCoherentActionException extends Exception {

    /**
     * Version attribute.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Private constructor.
     */
    public NotCoherentActionException() {
    }
}
