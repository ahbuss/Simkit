package simkit.random;

public class TraceVariate extends RandomVariateBase {

    private double[] traceValues;

    public TraceVariate() {
        this.setRandomNumber(new Sequential());
    }

    public void setTraceValues(double[] values) {
        traceValues = (double[]) values.clone() ; 
    }
    
    public double[] getTraceValues() { return (double[]) traceValues.clone(); }

    public void setParameters(Object[] params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("Need parameters length 1: " + params.length);
        }
        if (params[0] instanceof double[]) {
            setTraceValues((double[])params[0]);
        }
        else {
            throw new IllegalArgumentException("Need type double[]: " + params[0].getClass().getName());
        }
    }

    public Object[] getParameters() { return new Object[] { getTraceValues() }; }
    
    public double generate() {
        double value = (rng.getSeed() < traceValues.length) ? traceValues[(int) rng.getSeed() ] : Double.NaN;
        rng.draw();
        return value;
    }
} 