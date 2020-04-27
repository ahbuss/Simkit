/*
 * MagicMoveException.java
 *
 * Created on March 30, 2002, 5:51 PM
 */

package simkit.smdx;

/**
 * An Exception that indicates that a "magic move" was
 * attempted on a Mover on which magic moves are not allowed.
 * @author  Arnold Buss
 * 
 */
public class MagicMoveException extends java.lang.Exception {

    /**
     * Creates a new <code>MagicMoveException</code> without detail message.
     */
    public MagicMoveException() {
    }


    /**
     * Constructs a <code>MagicMoveException</code> with the specified detail message.
     * @param msg The detail message.
     */
    public MagicMoveException(String msg) {
        super(msg);
    }
}


