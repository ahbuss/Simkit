/*
 * NoMediatorDefinedException.java
 *
 * Created on May 31, 2002, 10:21 AM
 */

package simkit.smdx;

/**
 *
 * @author  Arnold Buss
 */
public class NoMediatorDefinedException extends java.lang.RuntimeException {
    
    /**
     * Creates a new instance of <code>NoMediatorDefinedException</code> without detail message.
     */
    public NoMediatorDefinedException() {
    }
    
    
    /**
     * Constructs an instance of <code>NoMediatorDefinedException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NoMediatorDefinedException(String msg) {
        super(msg);
    }
}
