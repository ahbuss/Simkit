package simkit.util;

/**
 * Used to indicate that a INI file contained a formatting error on read.
 *
 * @version $Id: IllegalINIFormatException.java 1083 2008-06-11 20:41:21Z
 * kastork $
 */
public class IllegalINIFormatException extends RuntimeException {

    /**
     * Creates a new exception with no description.
     */
    public IllegalINIFormatException() {
        super();
    }

    /**
     * Creates an exception with the given description.
     *
     * @param description given description
     */
    public IllegalINIFormatException(String description) {
        super(description);
    }
}
