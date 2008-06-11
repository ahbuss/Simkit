/*
 * PropertyDataLogger.java
 *
 * Created on March 14, 2002, 3:40 PM
 */

package simkit.util;
import java.beans.*;
import java.io.*;

/**
 * Logs the values of a property to an OutputStream. Only the values are
 * written, they are not time stamped.
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class PropertyDataLogger implements PropertyChangeListener {
    
/**
* The output Writer associated with the OutputStream.
**/
    private BufferedWriter output;

/**
* The name of the property to log.
**/
    private String propertyName ;

/**
* The number of values to write per line. Currently always 1.
**/
    private int numberPerLine = 1;

/**
* The current number of values on the current line.
**/
    private int current = 0;

/**
* The delimiter to place between values on the same line.
* Currently a space.
**/
    private String delimiter = " ";
    
/** 
* Creates new PropertyDataLogger 
* @param name The name of the property to log.
* @param outputStream The OutputStream to write the log to.
**/
    public PropertyDataLogger(String name, OutputStream outputStream) {
        propertyName = name;
        output = new BufferedWriter(new OutputStreamWriter(outputStream));
        current = 0;
    }
    
/**
* If the PropertyChangeEvent is for the desired property,
* writes the value to the OutputStream.
**/
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals(propertyName)) {
            try {
                output.write(e.getNewValue().toString());
                if (++current == numberPerLine) {
                    output.newLine();
                    output.flush();
                    current = 0;
                }
                else {
                    output.write(delimiter);
                }
            }
            catch (IOException exc) {
                System.err.println(exc);
                throw(new RuntimeException(exc));
            }
        }
        
    }
    
/**
* Closes the OutputStream.
**/
    public void closeOutput() {
        try {
            output.flush();
            output.close();
        }
        catch (IOException e) {
            System.err.println(e);
            throw(new RuntimeException(e));
        }
    }
}
