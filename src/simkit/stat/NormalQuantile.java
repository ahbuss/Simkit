package simkit.stat;

/**
 * @version $Id$
 * @author ahbuss
 */
public class NormalQuantile {

    private static final double[] c = new double[] {
        2.515517, 0.802853, 0.010328
    };

    private static final double[] d = new double[] {
        0.0, 1.432788, 0.189269, 0.001308
    };

    private double mean;
    private double stdDev;

    public NormalQuantile(double mean, double stdDev) {
        this.setMean(mean);
        this.setStdDev(stdDev);
    }

    public NormalQuantile() {
        this(0.0, 1.0);
    }

    public double getValue(double p) {
        if (Math.abs(p - 0.5) > 1.0) {
            throw new IllegalArgumentException("p must be betwen 0.0 and 1.0: " + p);
        }

        double t = Math.sqrt(Math.log(1/(p * p)));
        double q = t - (c[0] + t * (c[1] +  t * c[2]) ) /
                (1.0 + t * ( d[1] +  t * (d[2]  +  t * d[3] ) ) );
        
        return q * stdDev + mean;
    }

    /**
     * @return the mean
     */
    public double getMean() {
        return mean;
    }

    /**
     * @param mean the mean to set
     */
    public void setMean(double mean) {
        this.mean = mean;
    }

    /**
     * @return the stdDev
     */
    public double getStdDev() {
        return stdDev;
    }

    /**
     * @param stdDev the stdDev to set
     */
    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }

}
