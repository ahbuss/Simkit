package simkit.random;

import java.text.DecimalFormat;
public class TraceVariate extends RandomVariateBase {
    
    private static final double DEFAULT_DEFAULT_VALUE = Double.NaN;
    
    private static final DecimalFormat DF = new DecimalFormat("0.000;-0.000");
    private double[] traceValues;
    private double defaultValue;
    private boolean allDataInToString;
    private int shortNumber = 5;
    
    public TraceVariate() {
        this.setRandomNumber(new Sequential());
        this.setDefaultValue(DEFAULT_DEFAULT_VALUE);
        this.setAllDataInToString(false);
    }
    
    public void setTraceValues(double[] values) {
        traceValues = (double[]) values.clone() ;
    }
    
    public double[] getTraceValues() { return (double[]) traceValues.clone(); }
    
    public void setDefaultValue(double value) { defaultValue = value; }
    
    public double getDefaultValue() { return defaultValue; }
    
    public void setAllDataInToString(boolean b) { allDataInToString = b; }
    
    public boolean isAllDataInToString() { return allDataInToString; }
    
    public void setParameters(Object[] params) {
        if (params.length != 1 && params.length != 2) {
            throw new IllegalArgumentException("Need parameters length 1: " + params.length);
        }
        if (params[0] instanceof double[]) {
            setTraceValues((double[])params[0]);
        }
        else {
            throw new IllegalArgumentException("Need type double[]: " + params[0].getClass().getName());
        }
        if (params.length == 2) {
            if (params[1] instanceof Number) {
                setDefaultValue(((Number) params[1]).doubleValue());
            }
        }
    }
    
    public Object[] getParameters() {
        if (Double.isNaN(defaultValue)) {
            return new Object[] { getTraceValues() }; 
        }
        else {
            return new Object[] { getTraceValues(), new Double(getDefaultValue()) };
        }
    }
    
    public double generate() {
        double value = (rng.getSeed() < traceValues.length) ? traceValues[(int) rng.getSeed() ] : getDefaultValue();
        rng.draw();
        return value;
    }
    
    public String toString() {
        
        StringBuffer buf = new StringBuffer("Trace: [");
        if (allDataInToString || traceValues.length <= 2 * shortNumber) {
            for (int i = 0; i < traceValues.length; i++) {
                buf.append(DF.format(traceValues[i]));
                if (i < traceValues.length - 1) { buf.append(','); }
            }
        }
        else{
            for(int i = 0; i < shortNumber; i++) {
                buf.append(DF.format(traceValues[i]));
                buf.append(',');
            }
            buf.append("...,");
            for (int i = traceValues.length - shortNumber; i < traceValues.length; i++) {
                buf.append(DF.format(traceValues[i]));
                if (i < traceValues.length - 1) { buf.append(','); }
            }
        }
        buf.append("] (");
        buf.append(traceValues.length);
        buf.append(" values)");
        return buf.toString();
    }
}