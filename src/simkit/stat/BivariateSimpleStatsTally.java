package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import static simkit.SimEventScheduler.NL;

/**
 * Instances of this class compute up to second-order statistics on
 * bivariate data. The main contribution is the computation of sample
 * covariance and correlation. They use a Kalman filter-like algorithm
 * to maintain numerical stability. That is, instead of maintaining cumulative
 * cross-product sums, the covariance itself is updated with the new
 * value according to the formula:<br>
 * Cov(n) += &Delta;X<sub>n+1</sub>&Delta;Y<sub>n+1</sub>/n - Cov(n-1)/n <br>
 * Where &Delta;X<sub>n+1</sub> = X<sub>n+1</sub> - <span style="text-decoration:
  overline">X</span><sub>n</sub> and 
 * &Delta;Y<sub>n+1</sub> = Y<sub>n+1</sub> - <span style="text-decoration:
  overline">Y</span><sub>n</sub>
 * 
 * 
 * @author ahbuss
 */
public class BivariateSimpleStatsTally implements PropertyChangeListener {

    protected SimpleStatsTally[] marginals;
    
    protected double[] diff;
    
    protected double covariance;
    
    /**
     * Instantiate the marginals and diff arrays.
     * @param name Name of BivariateSimpleStatsTally. If used as a 
     * PropertyChangeListener, this must correspond to the name of the property
     * being fired.
     */
    public BivariateSimpleStatsTally(String name) {
        marginals = new SimpleStatsTally[2];
        marginals[0] = new SimpleStatsTally(name);
        marginals[1] = new SimpleStatsTally(name);
        diff = new double[2];
    }

    /**
     * Instantiate a BivariateSimpleStatsTally with the default name
     * (from {@link AbstractSimpleStats}).
     */
    public BivariateSimpleStatsTally() {
        this(AbstractSimpleStats.DEFAULT_NAME);
    }
    
    /**
     * reset marginals, diff array and covariance to 0.0
     */
    public void reset() {
        for (int i = 0; i < 2; ++i) {
            marginals[i].reset();
            diff[i] = 0.0;
        }
        covariance = 0.0;
    }
    
    /**
     * 
     * @param x New value of index 0
     * @param y New value of index 1
     */
    public void newObservation(double x, double y) {
        this.newObservation(new double[] { x, y });
    }
    
    /**
     * Adds a new observation with the contents of the given array
     * @param x Given array of length 2
     * @throws IllegalArgumentException if length of given array is not 2
     */
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
    
    /**
     * If property name matches this object's name and new value is
     * a double[], then add a new observation with the new value. If
     * neither of these is met, the heard event is simply ignored.
     * @param evt Heard PropertyChnageEvent
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (marginals[0].getName().equals(evt.getPropertyName())) {
            Object raw = evt.getNewValue();
            if (raw instanceof double[]) {
                this.newObservation((double[]) raw);
            }
        }
    }

    /**
     * 
     * @return current value of covariance
     */
    public double getCovariance() {
        return covariance;
    }
    
    /**
     * 
     * @return current value of correlation
     */
    public double getCorrelation() {
        return getCovariance() / 
                (marginals[0].getStandardDeviation() * marginals[1].getStandardDeviation());
    }
    
    /**
     * 
     * @param index index of count to get
     * @return count of index
     * @throws ArrayIndexOutOfBoundsException if index is not 0 or 1
     */
    public int getCount(int index) {
        return marginals[index].getCount();
    }
    
    /**
     * 
     * @param index index of min observation to get
     * @return minimum observation of index
     * @throws ArrayIndexOutOfBoundsException if index is not 0 or 1
     */
    public double getMinObs(int index) {
        return marginals[index].getMinObs();
    }
    
    /**
     * 
     * @param index index of max observation to get
     * @return maximum observation of index
     * @throws ArrayIndexOutOfBoundsException if index is not 0 or 1
     */
    public double getMaxObs(int index) {
        return marginals[index].getMaxObs();
    }
    
    /**
     * 
     * @param index index of mean to get
     * @return mean of index
     * @throws ArrayIndexOutOfBoundsException if index is not 0 or 1
     */
    public double getMean(int index) {
        return marginals[index].getMean();
    }
    
    /**
     * 
     * @param index index of standard deviation to get
     * @return standard deviation of index
     * @throws ArrayIndexOutOfBoundsException if index is not 0 or 1
     */
    public double getStandardDeviation(int index) {
        return marginals[index].getStandardDeviation();
    }
    
    /**
     * 
     * @param index index of variance to get
     * @return variance of index
     * @throws ArrayIndexOutOfBoundsException if index is not 0 or 1
     */
    public double getVariance(int index) {
        return marginals[index].getVariance();
    }
    
    @Override
    public String toString() {
        return marginals[0].toString() + NL + marginals[1].getDataLine() +
                NL + String.format("%.4f %.4f", getCovariance(), getCorrelation());
    }
    
}
