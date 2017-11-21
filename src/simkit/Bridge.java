/*
 * Bridge.java
 *
 * Created on July 13, 2002, 11:46 AM
 */
package simkit;

/**
 * Creates a bridge between two SimEvents. If the Bridge processes a given
 * SimEvent, it will schedule a second specified event at the current time using
 * the parameters and priority of the first event. After construction, the
 * Bridge must be registered with one or more SimEventSources.
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class Bridge extends simkit.BasicSimEntity {

    /**
     * The name of the SimEvent that this Bridge listens for.
     */
    private String heardEvent;

    /**
     * The name of the SimEvent that this Bridge will schedule upon hearing the
     * heardEvent.
*
     */
    private String sentEvent;

    /**
     * Creates a new instance of Bridge
     *
     * @param heard The name of the event to listen for.
     * @param sent The name of the event to schedule.
     * @throws IllegalArgumentException If either of the events names are null.
     */
    public Bridge(String heard, String sent) {
        if (heard == null || sent == null) {
            throw new IllegalArgumentException("Heard or sent name is null: "
                    + heard + " - " + sent);
        }
        setHeardEvent(heard);
        setSentEvent(sent);
    }

    /**
     * Does nothing.
     *
     * @param simEvent Given SimEvent
     */
    @Override
    public void handleSimEvent(simkit.SimEvent simEvent) {
    }

    /**
     * Schedules the sent event using the parameters and priority from the heard
     * event.
     * @param simEvent Given SimEvent
     */
    @Override
    public void processSimEvent(simkit.SimEvent simEvent) {
        if (simEvent.getEventName().equals(heardEvent)) {
            waitDelay(sentEvent, 0.0, simEvent.getEventPriority(), simEvent.getParameters());
        }
    }

    /**
     * @param he The event to be listened for
     */
    public void setHeardEvent(String he) {
        heardEvent = he;
    }

    /**
     * @return Heard event
     */
    public String getHeardEvent() {
        return heardEvent;
    }

    /**
     * @param se Event that will be heard by a listener
     */
    public void setSentEvent(String se) {
        sentEvent = se;
    }

    /**
     * @return New name of event to listeners
     */
    public String getSentEvent() {
        return sentEvent;
    }

}
