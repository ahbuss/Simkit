/*
 * MagicMoveException.java
 *
 * Created on March 30, 2002, 5:51 PM
 */

package simkit.smdx;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class MagicMoveException extends java.lang.Exception {

    /**
     * Creates new <code>MagicMoveException</code> without detail message.
     */
    public MagicMoveException() {
    }


    /**
     * Constructs an <code>MagicMoveException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public MagicMoveException(String msg) {
        super(msg);
    }
}


