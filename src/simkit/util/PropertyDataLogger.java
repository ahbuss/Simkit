/*
 * PropertyDataLogger.java
 *
 * Created on March 14, 2002, 3:40 PM
 */

package simkit.util;
import java.beans.*;
import java.io.*;

/**
 *
 * @author  Arnold Buss
 * @version
 */
public class PropertyDataLogger implements PropertyChangeListener {
    
    private BufferedWriter output;
    private String propertyName ;
    private int numberPerLine = 1;
    private int current = 0;
    private String delimiter = " ";
    
    /** Creates new PropertyDataLogger */
    public PropertyDataLogger(String name, OutputStream outputStream) {
        propertyName = name;
        output = new BufferedWriter(new OutputStreamWriter(outputStream));
        current = 0;
    }
    
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
            catch (IOException exc) { System.err.println(exc);}
        }
        
    }
    
    public void closeOutput() {
        try {
            output.flush();
            output.close();
        }
        catch (IOException e) {System.err.println(e);}
    }
    
}
