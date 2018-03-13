package simkit.examples;

import simkit.Priority;

/**
 * @author Arnold Buss
 */
public class IntegerArrivalListener extends simkit.SimEntityBase {

    protected int nextCustomer;

    /**
     * resets nextCustomer to 0
     */
    @Override
    public void reset() {
        super.reset();
        nextCustomer = 0;
    }
    
    public void doRun() {
        firePropertyChange("nextCustomer", getNextCustomer());
    }

    /**
     * increments nextCustomer and schedules Arrival(nextCustomer) event
     */
    public void doArrival() {
        int oldNextCustomer = getNextCustomer();
        nextCustomer += 1;
        firePropertyChange("nextCustomer", oldNextCustomer, getNextCustomer());
        waitDelay("Arrival", 0.0, getNextCustomer());
    }

    public int getNextCustomer() {
        return nextCustomer;
    }

}
