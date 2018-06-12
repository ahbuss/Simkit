package simkit.random;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ahbuss
 */
public class DataVariate extends RandomVariateBase {

    private static final Logger LOGGER = Logger.getLogger(DataVariate.class.getName());

    private static final double DEFAULT_VALUE = Double.NaN;

    private Scanner scanner;

    private File inputFile;

    private double defaultValue;

    @Override
    public double generate() {
        if (scanner.hasNext()) {
            return scanner.nextDouble();
        } else {
            return getDefaultValue();
        }
    }

    public void reset() {
        try {
            close();
            this.scanner = new Scanner(inputFile);
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }

    @Override
    public void setParameters(Object... params) {
        if (params.length <= 2) {
            if (params[0] instanceof File) {
                setInputFile((File) params[0]);
            } else if (params[0] instanceof String) {
                setInputFile(new File(params[0].toString()));
            } else {
                throw new IllegalArgumentException("first parameter must be filename or File: "
                        + params[0].getClass().getSimpleName());
            }
            if (params.length == 2) {
                if (params[1] instanceof Number) {
                    setDefaultValue(((Number) params[1]).doubleValue());
                } else {
                    throw new IllegalArgumentException("Second (optional) parameter must be Number: "
                            + params[1].getClass().getSimpleName());
                }
            } else if (params.length == 1) {
                setDefaultValue(DEFAULT_VALUE);
            }
        }
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{getInputFile(), getDefaultValue()};
    }

    /**
     * @return the inputFile
     */
    public File getInputFile() {
        return inputFile;
    }

    /**
     * @param inputFile the inputFile to set
     */
    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
        this.reset();
    }

    /**
     * @return the defaultValue
     */
    public double getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(double defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %.2f)", getClass().getSimpleName(), getInputFile().getName(), getDefaultValue());
    }

}
