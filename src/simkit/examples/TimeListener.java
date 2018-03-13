/*
 * TimeListener.java
 *
 * Created on February 27, 2004, 1:28 PM
 */
package simkit.examples;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import java.util.Map;

import simkit.SimEntityBase;

/**
 * @author Arnold Buss
 * @version $Id$
 */
public class TimeListener extends SimEntityBase implements PropertyChangeListener {

    protected Map<Integer, Double> arrivalTimes;

    public TimeListener() {
        arrivalTimes = new LinkedHashMap<>();
    }

    @Override
    public void reset() {
        super.reset();
        arrivalTimes.clear();
    }

    public void doArrival(Integer customer) {
        arrivalTimes.put(customer, eventList.getSimTime());
    }

    public void doEndService(Integer customer) {
        Double arrivalTime = arrivalTimes.remove(customer);
        if (arrivalTime != null) {
            firePropertyChange("timeInSystem",
                    eventList.getSimTime() - arrivalTime);
        }
    }

    public void doRenege(Integer customer) {
        Double arrivalTime = arrivalTimes.remove(customer);
        if (arrivalTime != null) {
            firePropertyChange("delayInQueue",
                    eventList.getSimTime() - arrivalTime.doubleValue());
            firePropertyChange("delayInQueueRenege",
                    eventList.getSimTime() - arrivalTime.doubleValue());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("customer")) {
            Double arrivalTime = arrivalTimes.get((Integer) evt.getNewValue());
            if (arrivalTime != null) {
                firePropertyChange("delayInQueue", eventList.getSimTime()
                        - arrivalTime);
                firePropertyChange("delayInQueueServed", eventList.getSimTime()
                        - arrivalTime);
            }
        }
    }

}
