// FILE InvalidSchedulingException.java
package simkit;

/**
 * Exception thrown by the simkit EventList/BasicEventList class when a SimEvent cannot be
 * scheduled as requested. For instance if a SimEvent is scheduled in simulated
 * history, an instance of this class will be thrown.
 *
 * @author K. A. Stork
 *
 *
 */
public class InvalidSchedulingException extends RuntimeException {

    /**
     * Create an InvalidSchedulingxception with detailed message s.
     *
     * @param s message for InvalidSchedulingException
*
     */
    public InvalidSchedulingException(String s) {
        super(s);
    }

    /**
     * Create an InvalidSchedulingxception no detailed message.
*
     */
    public InvalidSchedulingException() {
        super();
    }
}
