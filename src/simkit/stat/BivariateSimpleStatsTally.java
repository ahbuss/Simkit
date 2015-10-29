package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import static simkit.SimEventScheduler.NL;

/**
 * Instances of this class compute up to second-order statistics on
 * bivariate data. The main contribution is the computation of sample
 * covariance and correlation. They use a Kalman filter-like algorithm
 * to maintain numerical stability.
 * @author ahbuss
 */
public class BivariateSimpleStatsTally implements PropertyChangeListener {

    protected SimpleStatsTally[] marginals;
    
    protected double[] diff;
    
    protected double covariance;
    
    public BivariateSimpleStatsTally(String name) {
        marginals = new SimpleStatsTally[2];
        marginals[0] = new SimpleStatsTally(name);
        marginals[1] = new SimpleStatsTally(name);
        diff = new double[2];
    }

    public BivariateSimpleStatsTally() {
        this(AbstractSimpleStats.DEFAULT_NAME);
    }
    
    public void reset() {
        for (int i = 0; i < 2; ++i) {
            marginals[i].reset();
            diff[i] = 0.0;
        }
        covariance = 0.0;
    }
    
    public void newObservation(double x, double y) {
        this.newObservation(new double[] { x, y });
    }
    
    public void newObservation(double[] x) {
        if (x.length != 2) {
            throw new IllegalArgumentException(
                    "BivariateSimpleStateTally only accepts 2 observations");
        }
        if (marginals[0].getCount() > 0) {
            diff[0] = x[0] - marginals[0].getMean();
            diff[1] = x[1] - marginals[1].getMean();
            covariance += diff[0] * diff[1] / (marginals[0].getCount() + 1) -
                    covariance / marginals[0].getCount();
        }
        marginals[0].newObservation(x[0]);
        marginals[1].newObservation(x[1]);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public SimpleStatsTally[] getMarginals() {
        return marginals.clone();
    }

    public double[] getDiff() {
        return diff.clone();
    }    

    public double getCovariance() {
        return covariance;
    }
    
    public double getCorrelation() {
        return getCovariance() / 
                (marginals[0].getStandardDeviation() * marginals[1].getStandardDeviation());
    }
    
    @Override
    public String toString() {
        return marginals[0].toString() + NL + marginals[1].getDataLine() +
                NL + String.format("%.4f %.4f", getCovariance(), getCorrelation());
    }
    
}
