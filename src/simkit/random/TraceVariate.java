package simkit.random;

public class TraceVariate extends RandomVariateBase {
    
    private double[] traceValues;
    private double defaultValue;
    
    public TraceVariate() {
        this.setRandomNumber(new Sequential());
    }
    
    public void setTraceValues(double[] values) {
        traceValues = (double[]) values.clone() ;
    }
    
    public double[] getTraceValues() { return (double[]) traceValues.clone(); }
    
    public void setDefaultValue(double value) { defaultValue = value; }
    
    public double getDefaultValue() { return defaultValue; }
    
    public void setParameters(Object[] params) {
        if (params.length != 1 || params.length != 2) {
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
}