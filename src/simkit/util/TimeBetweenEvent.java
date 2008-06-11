package simkit.util;

import simkit.BasicSimEntity;

/**
 * Listens for a given event and fires a PropertyChangeEvent called 
 * "timeBetweenEvent" for the time since the event last occured.  The first
 * one is the time since the simulation started and the event firts occured.
 * 
 * <p>This can be heard by a SimpleStatsTally object, for instance, that will
 * tally statistics for the time between events.
 *
 * @version $Id: TimeBetweenEvent.java 872 2006-05-19 00:27:39Z ahbuss $
 * @author ahbuss
 */
public class TimeBetweenEvent extends BasicSimEntity {
    
    private String eventName;
    
    private Object[] eventParameters;
    
    private boolean ignoreParameters;
    
    protected double lastTime;
    
    /**
     * By default there are no arguments and parmaeters must be matched
     *
     * @param eventName The name of the event to be listened for
     */
    public TimeBetweenEvent(String eventName) {
        this(eventName, new Object[0], false);
    }
    
    /**
     * Constrcut instance with given event name, parameters, and whether
     * to ignore them
     * @param eventName Name of event to be listened for
     * @param eventParams Parameters of Event
     * @param ignoreParameters If true, only match event name, ignore parameters
     */
    public TimeBetweenEvent(String eventName, Object[] eventParams, 
            boolean ignoreParameters ) {
        setEventName(eventName);
        setEventParameters(eventParams);
        setIgnoreParameters(ignoreParameters);
    }

    /**
     * set lastTime to eventList's time (normally 0.0)
     */
    public void reset() {
        super.reset();
        lastTime = getEventList().getSimTime();
    }
    
    /**
     * Empty - doesn't schedule any methods
     */
    public void handleSimEvent(simkit.SimEvent event) {
    }

    /**
     * When an event is heard, if it matches eventName and (if ignoreParameters
     * is false) parameters match as well, compute time since last event.  Fire
     * it as "timeBetweenEvent" property.  Set lastTime to currentTime (and
     * fire it as well).
     * 
     * @param event The heard event
     */
    public void processSimEvent(simkit.SimEvent event) {
        if (event.getEventName().equals(eventName) ) {
            if (isIgnoreParameters() ||
                    parametersMatch(event.getParameters(), eventParameters)) {
                firePropertyChange("timeBetweenEvent",
                        getEventList().getSimTime() - getLastTime());
                
                double oldLastTime = getLastTime();
                lastTime = getEventList().getSimTime();
                firePropertyChange("lastTime", oldLastTime, getLastTime());
            }
        }
    }

    /**
     * @return name of event being listened to
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @param eventName name of event being to be listened to
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * @return time of last event's occurance
     */
    public double getLastTime() {
        return lastTime;
    }

    /**
     * Shallow copy
     * @return parameters of event being listened to
     */
    public Object[] getEventParameters() {
        return (Object[]) eventParameters.clone();
    }

    /**
     * If null, sets to new Object[0]
     * @param eventParams event parameters to be matched
     */
    public void setEventParameters(Object[] eventParams) {
        if (eventParams == null) {
            eventParams = new Object[0];
        }
        else {
            this.eventParameters = (Object[]) eventParams.clone();
        }
    }

    /**
     * @return whether parameters are ignored for counting
     */
    public boolean isIgnoreParameters() {
        return ignoreParameters;
    }

    /**
     * @param ignoreParameters whether parameters are ignored for counting
     */
    public void setIgnoreParameters(boolean ignoreParameters) {
        this.ignoreParameters = ignoreParameters;
    }
}