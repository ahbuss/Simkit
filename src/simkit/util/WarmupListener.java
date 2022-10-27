package simkit.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import simkit.BasicSimEntity;
import simkit.SimEvent;

/**
 * An instance of this class will collect individual successive observations
 * from a simulation (via PropertyChangeEvents) and compute the average of each
 * one.
 *
 * @author ahbuss
 */
public class WarmupListener extends BasicSimEntity implements PropertyChangeListener {

    /**
     * Stores the sums of successive observations
     */
    private final List<Number> sums;

    /**
     * The count for each observation within a replication
     */
    private int count;

    /**
     * The number of replications
     */
    private int replications;

    /**
     * The name of the state this will listen to - only PropertyChangeEvents
     * with this propertyName will be "heard"
     */
    private final String name;

    /**
     * '
     * Instantiate sums; initialize count &amp; replications to 0; set name.
     *
     * @param name Given property name to be listened for
     */
    public WarmupListener(String name) {
        this.sums = new ArrayList<>();
        this.count = 0;
        this.replications = 0;
        this.name = name;
    }

    /**
     * Reset count to 0 &amp; increments replications
     */
    public void reset() {
        super.reset();
        this.count = 0;
        this.replications += 1;
    }

    /**
     * If propertyName matches name and newValue is a Number, then if it is the
     * first one of the given count, add to sums; otherwise increment the sums
     * item at count by the new value;
     *
     * @param evt Given PropertyChangeEvent
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(name) && evt.getNewValue() instanceof Number) {
            if (sums.size() <= count) {
                sums.add((Number) evt.getNewValue());
            } else {
                double oldSum = sums.get(count).doubleValue();
                sums.set(count, oldSum + ((Number) evt.getNewValue()).doubleValue());
            }
            count += 1;
        }
    }

    /**
     *
     * @return array of averages
     */
    public double[] getAverages() {
        double[] averages = new double[sums.size()];

        for (int i = 0; i < averages.length; ++i) {
            averages[i] = sums.get(i).doubleValue() / replications;
        }

        return averages;
    }

    @Override
    public void handleSimEvent(SimEvent event) {
    }

    @Override
    public void processSimEvent(SimEvent event) {
    }

}
