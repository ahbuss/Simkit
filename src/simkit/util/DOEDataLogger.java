package simkit.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Outputs a file suitable for further analysis using design of experiments
 * (DOE). The first row consists of the header names - each factor name followed
 * by the dependent parameter name, separated by the delimiter. Each further row
 * of the output file consists of the factor settings, separated by the
 * delimiter. Using the default delimiter of comma (","), the result is a
 * comma-separated value file (csv) that can be read in by most statistical
 * packages.
 *
 * <p>
 * To use, whenever the factor settings change, be sure to call
 * <code>setFactors(Object...)</code> with a complete list of the factor values.
 * Also, after all runs are complete, call <code>close()</code> to close and
 * write the output file.
 *
 * 
 * @author ahbuss
 */
public class DOEDataLogger implements PropertyChangeListener {

    private static final Logger LOGGER = Logger.getLogger(DOEDataLogger.class.getName());

    private BufferedWriter bufferedWriter;

    private String delimiter;

    private Object[] factors;

    private String propertyName;

    private String[] header;

    private String factorString;

    /**
     *
     * @param propertyName Name of property to listen for
     * @param outputFile File to which output is written
     * @param header First row names of parameters; must be same length as
     * factors
     * @param delimiter Delimiter - default is ","
     * @param factors Values of factors corresponding to the heard property
     * values
     * @throws java.io.IOException if thrown when writing to outputFile
     * @throws IllegalArgumentException if header.length &ne; factors.length
     */
    public DOEDataLogger(String propertyName, File outputFile,
            String[] header, String delimiter, Object... factors) throws IOException {
        this.setPropertyName(propertyName);
        this.setDelimiter(delimiter);
        if (header.length != factors.length) {
            throw new IllegalArgumentException(
                    "Different number of factors and factor names: "
                    + header.length + " - " + factors.length);
        }
        this.setOutputFile(outputFile);
        this.setHeader(header);
        this.writeHeader();
        this.setFactors(factors);
    }

    /**
     * Zero parameter constructor - user must explicitly call setters plus
     * buildFactorString() and writeHeader()
     */
    public DOEDataLogger() {
    }

    /**
     * Default delimiter ","
     *
     * @param propertyName Name of property to listen for
     * @param outputFileName name of File to which output is written
     * @param header First row names of parameters; must be same length as
     * factors
     * @param factors Values of factors corresponding to the heard property
     * values
     * @throws java.io.IOException If thrown when writing to File given by
     * outputFileName
     * @throws IllegalArgumentException if header.length &ne; factors.length
     */
    public DOEDataLogger(String propertyName, String outputFileName,
            String[] header, Object... factors) throws IOException {
        this(propertyName, new File(outputFileName), header, ",", factors);
    }

    /**
     *
     * @param propertyName Name of property to listen for
     * @param outputFile File to which output is written
     * @param header First row names of parameters; must be same length as
     * factors
     * @param factors Values of factors corresponding to the heard property
     * values
     * @throws java.io.IOException if thrown when writing to outputFile
     * @throws IllegalArgumentException if header.length &ne; factors.length
     */
    public DOEDataLogger(String propertyName, File outputFile,
            String[] header, Object... factors) throws IOException {
        this(propertyName, outputFile, header, ",", factors);
    }

    /**
     * 
     * @param propertyName Given property name
     * @param outputFile Given output file
     * @param header Given header array
     * @param delimiter Given delimiter
     */
    public DOEDataLogger(String propertyName, File outputFile,
            String[] header, String delimiter) {
        this.setPropertyName(propertyName);
        this.setDelimiter(delimiter);
        this.setHeader(header);
        this.writeHeader();
        this.setFactors(factors);
        try {
            this.setOutputFile(outputFile);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Uses default delimiter ","
     * @param propertyName Given property name
     * @param outputFile Given output file
     * @param header Given header
     */
    public DOEDataLogger(String propertyName, File outputFile,
            String[] header) {
        this(propertyName, outputFile, header, ",");
    }

    /**
     * If evt has right propertyName, append a line to the output, consisting of
     * the factors followed by the newValue, separated by the delimiter.
     *
     * @param evt Heard PropertyChangeEvent
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(propertyName)) {
            try {
                bufferedWriter.newLine();
                bufferedWriter.append(factorString);
                bufferedWriter.append(evt.getNewValue().toString());
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Builds the factorString from the factors object
     */
    protected void buildFactorString() {
        StringBuilder builder = new StringBuilder();
        for (Object factor : factors) {
            builder.append(factor);
            builder.append(getDelimiter());
        }
        factorString = builder.toString();
    }

    /**
     * Writes the first row with the factor names and the property name to the
     * output file
     */
    protected void writeHeader() {
        try {
            for (int i = 0; i < header.length; ++i) {
                bufferedWriter.append(header[i]);
                bufferedWriter.append(getDelimiter());
            }
            bufferedWriter.append(getPropertyName());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    /**
     * @return the delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * @return shallow copy of the factors
     */
    public Object[] getFactors() {
        return factors.clone();
    }

    /**
     * @param factors the factors to set
     * @throws IllegalArgumentException if # factors &ne; header.length
     */
    public void setFactors(Object... factors) {
        if (factors.length != header.length) {
            throw new IllegalArgumentException(
                    "Different number of factors and factor names: "
                    + header.length + " - " + factors.length);
        }
        this.factors = factors.clone();
        this.buildFactorString();
    }

    /**
     * @return the propertyName
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * flush and close bufferedWriter
     */
    public void close() {
        try {
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(DOEDataLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the header
     */
    public String[] getHeader() {
        return header.clone();
    }

    /**
     * @param header the header to set
     * @throws RuntimeException if header has already been set
     */
    public void setHeader(String... header) {
        if (this.header != null) {
            throw new RuntimeException(
                    "header is already set - cannot be set twice");
        }
        this.header = header.clone();
    }

    /**
     * @param delimiter the delimiter to set
     */
    public void setDelimiter(String delimiter) {
        if (this.delimiter != null) {
            throw new RuntimeException("delimiter can only be set once");
        }
        this.delimiter = delimiter;
    }

    /**
     * @param propertyName the propertyName to set
     */
    public void setPropertyName(String propertyName) {
        if (this.propertyName != null) {
            throw new RuntimeException("propertyName can only be set once");
        }
        this.propertyName = propertyName;
    }

    /**
     *
     * @param outputFile Given output file
     * @throws java.io.IOException if thrown by bufferedWriter
     */
    public void setOutputFile(File outputFile) throws IOException {
        FileWriter fileWriter = new FileWriter(outputFile);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    /**
     * @return the factorString
     */
    public String getFactorString() {
        return factorString;
    }

}
