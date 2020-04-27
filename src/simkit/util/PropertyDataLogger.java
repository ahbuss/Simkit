package simkit.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Logs the values of a property to an OutputStream. Only the values are
 * written, they are not time stamped.
 *
 * @author  Arnold Buss
 * 
 */
public class PropertyDataLogger implements PropertyChangeListener {
    
    private static final Logger logger = Logger.getLogger(PropertyDataLogger.class.getName());
/**
* The output Writer associated with the OutputStream.
**/
    private BufferedWriter output;

/**
* The name of the property to log.
**/
    protected String propertyName ;

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
        this.propertyName = name;
        this.output = new BufferedWriter(new OutputStreamWriter(outputStream));
        this.current = 0;
    }
    
    public PropertyDataLogger(String name, File outputFile) {
        this.propertyName = name;
        this.current = 0;
        try {
            this.output = new BufferedWriter(new FileWriter(outputFile));
            logger.log(Level.INFO, "{0} will be overwritten", outputFile.getAbsolutePath());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
/**
* If the PropertyChangeEvent is for the desired property,
* writes the value to the OutputStream.
**/
    @Override
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

    public String getPropertyName() {
        return propertyName;
    }

    public int getNumberPerLine() {
        return numberPerLine;
    }

    public void setNumberPerLine(int numberPerLine) {
        this.numberPerLine = numberPerLine;
    }

    public int getCurrent() {
        return current;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
