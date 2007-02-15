package simkit.random;

import java.text.DecimalFormat;
import simkit.random.DiscreteVariate;
import simkit.random.RandomNumber;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 * @version $Id$
 * @author ahbuss
 */
public class FrequencyRandomObjectVariate implements RandomObjectVariate {
    
    private DiscreteVariate discreteVariate;
    
    private RandomNumber randomNumber;
    
    private Object[] values;
    
    private DecimalFormat decimalFormat;
    
    private int maxNameLength;
    
    public FrequencyRandomObjectVariate() {
        setRandomNumber(RandomVariateFactory.getDefaultRandomNumber());
        setDecimalFormat(new DecimalFormat("0.0000"));
    }
    
    public double generate() {
        return Double.NaN;
    }
    
    public Object generateObject() {
        int index = (int) discreteVariate.generate();
        return values[index];
    }
    
    public Object[] getParameters() {
        return new Object[] {values.clone(), discreteVariate.getCDF() };
    }
    
    public RandomNumber getRandomNumber() {
        return discreteVariate.getRandomNumber();
    }
    
    public void setParameters(Object... obj) {
        if (obj.length != 2) {
            throw new IllegalArgumentException("Must have 2 arguments: " + obj.length);
        }
        
        if ( !(obj[0] instanceof Object[]) || !(obj[1] instanceof double[])) {
            throw new IllegalArgumentException("Need (Object[], double[]): (" +
                    obj[0].getClass().getName() + ", " +
                    obj[1].getClass().getName());
        }
        
        Object[] values = (Object[]) obj[0];
        double[] frequencies = (double[]) obj[1];
        if (values.length != frequencies.length) {
            throw new IllegalArgumentException("Values and frequencies must be same length: " +
                    values.length + "!=" + frequencies.length);
        }
        setValues(values);
        setFrequencies(frequencies);
        
    }
    
    public void setRandomNumber(RandomNumber randomNumber) {
        this.randomNumber = randomNumber;
    }
    
    public Object[] getValues() {
        return (Object[]) values.clone();
    }
    
    public void setValues(Object[] values) {
        this.values = (Object[]) values.clone();
        maxNameLength = 0;
        for (int i = 0; i < this.values.length; ++i) {
            maxNameLength = Math.max(maxNameLength, this.values[i].toString().length());
        }
    }
    
    public String toString() {
        String tabs = "\t";
        for (int i = 0; i < maxNameLength / 8; ++i) {
            tabs += "\t";
        }
        StringBuffer stringBuffer = new StringBuffer(System.getProperty("line.separator"));
        stringBuffer.append("Value");
        stringBuffer.append(tabs);
        stringBuffer.append("Frequency\tCumulativeFrequency");
        double[] frequencies = discreteVariate.getProbabilities();
        double[] cumulativeFrequencies = discreteVariate.getCDF();
        for (int i = 0; i < values.length; ++i) {
            stringBuffer.append(System.getProperty("line.separator"));
            stringBuffer.append(values[i]);
            int extra = maxNameLength - values[i].toString().length();
            int extraTabs = (int) Math.ceil(extra / 8.0);
            stringBuffer.append('\t');
            for (int j = 0; j < extraTabs; ++j) {
                stringBuffer.append('\t');
            }
            stringBuffer.append(decimalFormat.format(frequencies[i]));
            stringBuffer.append('\t');
            stringBuffer.append('\t');
            stringBuffer.append(decimalFormat.format(cumulativeFrequencies[i]));
        }
        return stringBuffer.toString();
    }
    
    public void setFrequencies(double[] frequencies) {
        double[] indexes = new double[frequencies.length];
        for (int i = 0; i < indexes.length; ++i) {
            indexes[i] = i;
        }
        discreteVariate = (DiscreteVariate) RandomVariateFactory.getInstance(
                "simkit.random.DiscreteVariate", new Object[] { indexes, frequencies } );
    }
    
    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }
    
    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }
    
}
