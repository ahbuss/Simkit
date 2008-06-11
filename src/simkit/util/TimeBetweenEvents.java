package simkit.util;

import java.util.LinkedList;
import java.util.List;
import simkit.BasicSimEntity;
import simkit.Schedule;
import simkit.SimEvent;

/**
 * Instances of this class listen for Events that are
 * supposed to occur in pairs, with a FIFO sequence
 * between successive events.  The successive times between
 * the first and the second event are computed and
 * a property specified by the user is fired with
 * that value.
 * Technically, every time a time is added or removed from the
 * list, a PropertyChangeEvent should be fired.  This is not done, however.
 * 
 * @version $Id$
 * @author ahbuss
 */
public class TimeBetweenEvents extends BasicSimEntity {

    private String firstEvent;
    private String secondEvent;
    private String propertyName;

    protected LinkedList<Double> times;

    /**
     * Creates a new instance of TimeBetweenEvents
     * @param fe First event listened to
     * @param se Second event listened to
     * @param pn Name of the property to be fired
     */

    public TimeBetweenEvents(String fe,  String se, String pn) {
        setFirstEvent(fe);
        setSecondEvent(se);
        setPropertyName(pn);
        times = new LinkedList<Double>();
    }
    
    /**
     * Does nothing.
     * @param event Previously Scheduled SimEvent
     */    
    public void handleSimEvent(SimEvent event) {
    }    

    /**
     * If the event is the first one, store the current
     * value of simTime on the list.
     *
     * If the event is the second one, pop the first
     * time off the list, compute the diffence between it
     * and current simTime, and fire that value as a
     * property named propertyName
     * @param event Heard SimEvent
     */    
    public void processSimEvent(SimEvent event) {
        if (event.getEventName().equals(getFirstEvent())) {
            times.add(Schedule.getSimTime());
        }
        else if (event.getEventName().equals(getSecondEvent())) {
            double firstTime = times.removeFirst();
            firePropertyChange(getPropertyName(), Schedule.getSimTime() - firstTime);
        }
    }    

    /** Set initial values of all state variables */
    public void reset() {

        super.reset();

        /** StateTransitions for the Run Event */

        times.clear();
    }

    /**
     * @param fe Name of the first event to be heard
     */    
    public void setFirstEvent(String fe) {
        firstEvent = fe;
    }

    /**
     * @return Name of the first event
     */    
    public String getFirstEvent() {
        return firstEvent;
    }

    /**
     *
     * @param se Name of the second event heard
     */    
    public void setSecondEvent(String se) {
        secondEvent = se;
    }

    /**
     * @return Name of the second event heard
     */    
    public String getSecondEvent() {
        return secondEvent;
    }

    /**
     *
     * @param pn Name of the property fired
     */    
    public void setPropertyName(String pn) {
        propertyName = pn;
    }
    
    /**
     * @return Name of the property fired
     */    
    public String getPropertyName() {
        return propertyName;
    }
        
    /**
     * @return Shallow copy of the current list of times.
     */    
    public List<Double> getTimes() {
        return new LinkedList<Double>(times);
    }


}
