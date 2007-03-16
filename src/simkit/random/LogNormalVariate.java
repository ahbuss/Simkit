package simkit.random;

/**
 * @version $Id$
 * @author ahbuss
 */
public class LogNormalVariate extends NormalVariate {
    
    public LogNormalVariate() {
    }

    public double generate() {
        return Math.exp(super.generate());
    }
    
    public static double[] getNormalParameters(double mean, double stdDeviation) {
        return new double[]{
            Math.log(mean) - 0.5 * Math.log(Math.pow(stdDeviation/mean, 2) + 1.0),
            Math.sqrt(Math.log(Math.pow(stdDeviation/mean, 2) + 1.0))
        };
    }
    
    public static double[] getLogNormalParameters(double mean, double stdDeviation) {
        return new double[] {
            Math.exp(mean + 0.5 * Math.pow(stdDeviation, 2)),
            Math.sqrt(Math.exp(2.0 * mean + Math.pow(stdDeviation, 2)) *
                    (Math.exp(0.5 * Math.pow(stdDeviation, 2)) - 1.0))
        };
    }

    public String toString() {
        return super.toString().replaceAll("Normal", "Log Normal");
    }
}