/*
 * NoSuchACtionMethodException.java
 *
 * Created on February 15, 2002, 11:01 PM
 */

package simkit.actions;

/**
 *
 * @author  Arnold Buss
 * 
 */
public class NoSuchActionMethodException extends NoSuchMethodException {

    /**
     * Creates new <code>NoSuchACtionMethodException</code> without detail message.
     */
    public NoSuchActionMethodException() {
    }


    /**
     * Constructs an <code>NoSuchACtionMethodException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NoSuchActionMethodException(String msg) {
        super(msg);
    }
}


