/*
 * NoMediatorDefinedException.java
 *
 * Created on May 31, 2002, 10:21 AM
 */

package simkit.smdx;

/**
 * Thrown to indicate that a Mediator could not be found for
 * a pair of Objects for which a referee was determining
 * how to handle an interaction.
 *
 * @author  Arnold Buss
 * @version $Id: NoMediatorDefinedException.java 441 2003-11-01 00:08:27Z jlruck $
 */
public class NoMediatorDefinedException extends java.lang.RuntimeException {
    
    /**
     * Creates a new instance of <code>NoMediatorDefinedException</code> without detail message.
     */
    public NoMediatorDefinedException() {
    }
    
    
/**
* Constructs an instance of <code>NoMediatorDefinedException</code> 
* with the specified detail message.
* @param msg the detail message.
*/
    public NoMediatorDefinedException(String msg) {
        super(msg);
    }
}
