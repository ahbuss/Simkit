package simkit.test;

import java.util.logging.Logger;
import simkit.stat.SimpleStats;
import simkit.util.StateChangeEvent;
/**
 *
 * @author ahbuss
 */
public abstract class AbstractSimpleStats<S> implements SimpleStats<S> {

    private static final Logger LOGGER = Logger.getLogger(AbstractSimpleStats.class.getName());

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getMean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public S getMin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public S getMax() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getVariance() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getStandardDeviation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @SuppressWarnings("unchecked")
    @Override
    public void stateChange(StateChangeEvent<Object, S> evt) {
        this.newObservation((S) evt.getNewValue());
    }



}
