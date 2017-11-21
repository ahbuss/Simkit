package simkit.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version $Id$
 * @author ahbuss
 */
public class CSVPropertyDataLogger implements PropertyChangeListener {

    private static final Logger LOGGER = Logger.getLogger(CSVPropertyDataLogger.class.getName());

    protected Map<String, File> temporaryDataFiles;

    protected Map<String, BufferedWriter> temporaryDataWriters;

    private File outputFile;

    /**
     * Instantiate a CSVPropertyDataLogger with given output file. Any contents
     * of the given outputFile will be overwritten without notification.
     * @param outputFile Given output file
     */
    public CSVPropertyDataLogger(File outputFile) {
        this.temporaryDataFiles = new LinkedHashMap<>();
        this.temporaryDataWriters = new LinkedHashMap<>();
        this.outputFile = outputFile;
    }
    
    /**
     * Clear all internal data
     */
    public void reset() {
        this.temporaryDataFiles.clear();
        this.temporaryDataWriters.clear();
    }

    /**
     * If hearing a property for the first time, create a temporary file
     * and store in temporaryDataFiles keyed by the property name. If the property
     * has been heard before, append the new value to the temporary file
     * for that property.
     * @param evt Given PropertyChangeEvent
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        BufferedWriter writer;
        if (!temporaryDataWriters.containsKey(evt.getPropertyName())) {
            try {
                File tempFile = File.createTempFile(evt.getPropertyName(), ".csv");
                temporaryDataFiles.put(evt.getPropertyName(), tempFile);
                writer = new BufferedWriter(new FileWriter(tempFile));
                temporaryDataWriters.put(evt.getPropertyName(), writer);
                writer.append(evt.getNewValue().toString());
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        } else {
            writer = temporaryDataWriters.get(evt.getPropertyName());
            try {
                writer.newLine();
                writer.append(evt.getNewValue().toString());
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * Write the output file in comma separate value (CSV) format. The first
     * line consists of the property names and each column consists of the 
     * values for that property that have been "heard." Note that the columns
     * will, in general, be of different sizes.
     */
    public void writeOutputFile() {
        BufferedWriter writer;
        try {
//            Builds the header row and saves a Scanner for each property
            Map<String, Scanner> scanners = new LinkedHashMap<>();
            writer = new BufferedWriter(new FileWriter(outputFile));
            StringBuilder builder = new StringBuilder();
            for (String property : temporaryDataWriters.keySet()) {
                temporaryDataWriters.get(property).close();
                File tempFile = temporaryDataFiles.get(property);
                Scanner scanner = new Scanner(tempFile);
                scanners.put(property, scanner);
                builder.append(property).append(',');
            }
//            This removes the extra comma at the end.
            int lastIndex = builder.lastIndexOf(",");
            if (lastIndex > 0) {
                builder.deleteCharAt(lastIndex);
            }
            writer.append(builder);
//            As long as there is more data, continue processing the temporary
//            files. If a value is present, append it, othewise append an empty
//            cell (i.e. a comma)
            boolean moreData;
            do {
                moreData = false;
                builder.setLength(0);
                for (String property : scanners.keySet()) {
                    Scanner scanner = scanners.get(property);
                    if (scanner.hasNext()) {
                        builder.append(scanner.next()).append(',');
                        moreData = true;
                    } else {
                        builder.append(',');
                    }
                }
                if (builder.toString().length() > 0) {
                    builder.deleteCharAt(builder.lastIndexOf(","));
                    if (!builder.toString().replaceAll(",", "").equals("")) {
                        writer.newLine();
                        writer.append(builder);
                    }
                }
            } while (moreData);
//            close the output writer and all the Scanners
            writer.close();
            for (String property : scanners.keySet()) {
                scanners.get(property).close();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the temporaryDataFiles
     */
    public Map<String, File> getTemporaryDataFiles() {
        return new LinkedHashMap<>(temporaryDataFiles);
    }

    /**
     * @return the outputFile
     */
    public File getOutputFile() {
        return outputFile;
    }

    /**
     * @param outputFile the outputFile to set
     */
    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }
}
