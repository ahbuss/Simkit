package simkit.random;

public class DiscreteVariate extends RandomVariateBase {

    private double[] cdf;
    private double[] value;

    public DiscreteVariate() {}

    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Must have Object[] {double[], double[]}");
        }
        else if (params[0] instanceof double[] && params[1] instanceof double[]) {
            value = (double[]) ((double[]) params[0]).clone();
            cdf = normalize((double[]) params[1]);
        }
        else {
            throw new IllegalArgumentException("Parameters not of type {double[], double[]}");
        }
    }
    
    public Object[] getParameters() { return new Object[] { getValues(), getProbabilities() }; }
    
    public double generate() {
        int index;
        double uniform = this.rng.draw();
        for (index = 0; (uniform > cdf[index]) && (index < cdf.length - 1); index++) ;
        return value[index];
    }

    protected double[] normalize(double[] freq) {
        double[] norm = null;
        double sum = 0.0;
        norm = new double[freq.length];
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] >= 0.0) {
                sum += freq[i];
                norm[i] = sum;
            }
            else {
                throw new IllegalArgumentException("Bad frequency value at index " +
                    i + " (value = " + freq[i] + ")");
            }
        }
        if (sum > 0.0) {
            for (int i = 0; i < norm.length; i++) {
                norm[i] = norm[i] / sum;
            }
        }
        else {
            throw new IllegalArgumentException("Frequency sum not positive: " + sum);
        }
        return norm;
    }

    public String toString() {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
        StringBuffer buf = new StringBuffer();
        buf.append("x    \tf(x)   \tF(x)\n");
        for (int i = 0; i < cdf.length; i++) {
            buf.append(value[i]);
            buf.append('\t');
            buf.append(df.format(i == 0 ? cdf[i] : cdf[i] - cdf[i-1]));
            buf.append('\t');
            buf.append(df.format(cdf[i]));
            buf.append('\n');
        }
        return buf.toString();
    }

    public void setValues(double[] values) { this.value = (double[]) values.clone(); }
    
    public void setProbabilities(double[] prob) { cdf = normalize(prob); }
    
    public void setCDF(double[] cdf) { this.cdf = (double[])cdf.clone(); }
    
    public double[] getValues() { return (double[]) value.clone(); }

    public double[] getCDF() { return (double[]) cdf.clone(); }

    public double[] getProbabilities() {
        double[] freq = new double[cdf.length];
        for (int i = 0; i < cdf.length; i++) {
            freq[i] = (i == 0) ? cdf[i] :  cdf[i] - cdf[i-1];
        }
        return freq;
    }
}

