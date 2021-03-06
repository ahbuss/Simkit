package simkit;

/**
 * Similar to the Bridge class, instances of Adapter listen for a single event
 * and, when heard, dispatch an event that has the name "passedEvent" but is
 * otherwise identical (i.e. same source, parameters, and priority). In contrast
 * to the Bridge, instances of Adapter bypass the Event List and directly notify
 * their SimEventListeners.
 *
 * @author ahbuss
 */
public class Adapter extends BasicSimEntity {

    /**
     * Will only respond to events of this name
     */
    private String heardEvent;
    /**
     * Will change events to be named this
     */
    private String passedEvent;

    /**
     * @param heard The name of the heard event
     * @param passed The name of the passed event
     */
    public Adapter(String heard, String passed) {
        setHeardEvent(heard);
        setPassedEvent(passed);
    }
    
    /**
     * Zero-argument constructor for beans-like instantiation. This sets the
     * heardEvent and passedEvent each to ""
     */
    public Adapter() {
        this("", "");
    }

    /**
     * @param he Name of heard event
     */
    public void setHeardEvent(String he) {
        heardEvent = he;
    }

    /**
     * @return Current heard event
     */
    public String getHeardEvent() {
        return heardEvent;
    }

    /**
     * @param pe Name of the passed event
     */
    public void setPassedEvent(String pe) {
        passedEvent = pe;
    }

    /**
     * @return Name of passed event
     */
    public String getPassedEvent() {
        return passedEvent;
    }

    /**
     * Does nothing, since instances of Adapter will never schedule their own
     * events.
     *
     * @param event SimEvent from Event List
     */
    @Override
    public void handleSimEvent(SimEvent event) {
    }

    /**
     * If the heard event is the one we are listening for, dispatch an event
     * with the name "passedEvent" that is otherwise identical.
     *
     * @param event Heard event
     */
    @Override
    public void processSimEvent(SimEvent event) {
        if (event.getEventName().equals(getHeardEvent())) {
            SimEvent se = new SimEvent(
                    event.getSource(),
                    getPassedEvent(),
                    event.getParameters(),
                    0.0,
                    event.getPriority()
            );
            notifyListeners(se);
        }
    }

    /**
     * 
     * @param source Given SimEventSource of heardEvent
     * @param listener Given SimEventListsner for passedEvent
     */
    public void connect(SimEventSource source, SimEventListener listener) {
        source.addSimEventListener(this);
        this.addSimEventListener(listener);
    }

    /**
     * 
     * @param source Given SimEventSource to disconnect
     * @param listener Given SimEventListener to disconnect
     */
    public void disconnect(SimEventSource source, SimEventListener listener) {
        source.removeSimEventListener(this);
        this.removeSimEventListener(listener);
    }
    
    /**
     * Helper method to conform to "addXXX()" convention. Simple invokes connect()
     * @param source Given SimEventSource of heardEvent
     * @param listener Given SimEventListsner for passedEvent
     */
    public void addConnect(SimEventSource source, SimEventListener listener) {
        this.connect(source, listener);
    }
    
    /**
     * Helper method to conform to "addXXX()" convention; simply invokes connect()
     * @param source Given SimEventSource to disconnect
     * @param listener Given SimEventListener to disconnect
     */
    public void addDisconnect(SimEventSource source, SimEventListener listener) {
        this.disconnect(source, listener);
    }

}
