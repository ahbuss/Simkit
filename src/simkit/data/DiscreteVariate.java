package simkit.data;

public class DiscreteVariate extends RandomVariateBase {

    private double[] cdf;
    private double[] value;

    public DiscreteVariate(Object[] params) {
        super(params, RandomFactory.getRandomNumber());
    }

    public DiscreteVariate(Object[] params, long seed) {
        super(params, RandomFactory.getRandomNumber(), seed);
    }

    public DiscreteVariate(Object[] params, RandomNumber rng) {
        super(params, rng);
    }

    public DiscreteVariate(Object[] params, RandomNumber rng, long seed) {
        super(params, rng, seed);
    }

    public void setParameters(Object[] params) {
        if (params[1] instanceof double[]) {
            normalize((double[])params[1]);
            if (params[0] instanceof double[]) {
                value = (double[]) params[0];
            }
            super.setParameters(new Object[] {params[0], params[1], cdf});
        }
        else {
            throw new IllegalArgumentException("Parameters not of type {double[], double[]}");
        }
    }


    public double generate() {
        int index;
        double uniform = this.rng.draw();
        for (index = 0; (uniform > cdf[index]) && (index < cdf.length - 1); index++) ;

//        System.out.println("U = " + uniform + " index = " + index + " value = " + value[index] );
        return value[index];
    }

    protected void normalize(double[] freq) {
        double sum = 0.0;
        cdf = new double[freq.length];
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] >= 0.0) {
                sum += freq[i];
                cdf[i] = sum;
            }
            else {
                throw new IllegalArgumentException("Bad frequency value at index " +
                    i + " (value = " + freq[i] + ")");
            }
        }
        if (sum > 0.0) {
            for (int i = 0; i < cdf.length; i++) {
                cdf[i] = cdf[i] / sum;
            }
        }
        else {
            throw new IllegalArgumentException("Frequency sum not positive: " + sum);
        }
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


}
