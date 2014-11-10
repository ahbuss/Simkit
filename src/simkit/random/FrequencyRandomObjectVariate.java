package simkit.random;

import java.text.DecimalFormat;

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
    
    @Override
    public double generate() {
        return Double.NaN;
    }
    
    @Override
    public Object generateObject() {
        int index = (int) discreteVariate.generate();
        return values[index];
    }
    
    @Override
    public Object[] getParameters() {
        return new Object[] {values.clone(), discreteVariate.getCDF() };
    }
    
    @Override
    public RandomNumber getRandomNumber() {
        return discreteVariate.getRandomNumber();
    }
    
    @Override
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
    
    @Override
    public String toString() {
        String tabs = "\t";
        for (int i = 0; i < maxNameLength / 8; ++i) {
            tabs += "\t";
        }
        StringBuilder StringBuilder = new StringBuilder(System.getProperty("line.separator"));
        StringBuilder.append("Value");
        StringBuilder.append(tabs);
        StringBuilder.append("Frequency\tCumulativeFrequency");
        double[] frequencies = discreteVariate.getProbabilities();
        double[] cumulativeFrequencies = discreteVariate.getCDF();
        for (int i = 0; i < values.length; ++i) {
            StringBuilder.append(System.getProperty("line.separator"));
            StringBuilder.append(values[i]);
            int extra = maxNameLength - values[i].toString().length();
            int extraTabs = (int) Math.ceil(extra / 8.0);
            StringBuilder.append('\t');
            for (int j = 0; j < extraTabs; ++j) {
                StringBuilder.append('\t');
            }
            StringBuilder.append(decimalFormat.format(frequencies[i]));
            StringBuilder.append('\t');
            StringBuilder.append('\t');
            StringBuilder.append(decimalFormat.format(cumulativeFrequencies[i]));
        }
        return StringBuilder.toString();
    }
    
    public void setFrequencies(double[] frequencies) {
        double[] indexes = new double[frequencies.length];
        for (int i = 0; i < indexes.length; ++i) {
            indexes[i] = i;
        }
        discreteVariate = (DiscreteVariate) RandomVariateFactory.getInstance(
                "simkit.random.DiscreteVariate", new Object[] { indexes, frequencies } );
    }
    
    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }
}
