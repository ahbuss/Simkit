package simkit;

/**
 * Thrown when Simkit detects that the total number of Threads in
 * startSimulation(), reset(), and coldReset() is greater than 1. This includes
 * the same Thread attempting to enter reset() or coldReset() while the
 * simulation is running.
 *
 * @author John Ruck (Rolands and Associates Corporation)
 *
 */
public class SimkitConcurrencyException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "Only one call to "
            + "either startSimulation, reset, or coldReset allowed at a time.";

    /**
     * Constructs a new exception with the default message.
     */
    public SimkitConcurrencyException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a new exception with the given message appended to the default
     * message.
     *
     * @param message given message
     */
    public SimkitConcurrencyException(String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

    /**
     * Constructs a new exception with the given message appended to the default
     * message and the given cause.
     *
     * @param message Given message
     * @param cause Given Exception
     */
    public SimkitConcurrencyException(String message, Throwable cause) {
        super(DEFAULT_MESSAGE + " " + message, cause);
    }

    /**
     * Constructs a new exception with the default message and the given cause.
     *
     * @param cause Given Exception
     */
    public SimkitConcurrencyException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
