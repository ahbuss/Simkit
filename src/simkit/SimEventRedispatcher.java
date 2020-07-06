package simkit;

/**
 * This is to add support for a SimEntity re-dispatching its "heard" events to
 * its listeners. This should be used very cautiously, since it is easy to
 * create an infinite loop of events.
 *
 * @author ahbuss
 */
public class SimEventRedispatcher extends BasicSimEntity {

    /**
     * The
     */
    private BasicSimEntity source;

    /**
     * Sets this instance to be non-persistent
     */
    public SimEventRedispatcher() {
        this.setPersistant(false);
    }

    /**
     *
     * @param source Given source of "heard" events.
     */
    public SimEventRedispatcher(BasicSimEntity source) {
        this();
        this.setSource(source);
    }

    /**
     * Does nothing, since this only responds to events that are heard.
     *
     * @param event Given SimEvent
     */
    @Override
    public void handleSimEvent(SimEvent event) {
    }

    /**
     * Creates a copy of the SimEvent and dispatches it to the SimEventListeners
     * of the source instance.
     *
     * @param event Given SimEvent
     */
    @Override
    public void processSimEvent(SimEvent event) {
        if (event.getSource() != this.getSource()) {
            SimEvent copy = new SimEvent(source, event.getName(),
                    event.getParameters(), 0.0, event.getPriority());
            source.notifyListeners(event);
        }
    }

    /**
     * @return the source
     */
    public BasicSimEntity getSource() {
        return source;
    }

    /**
     * If source had previously been set, remove this as a SimEventListener and
     * remove all the source's as SimEventListener to this instance.
     * <p>
     * Then add this as a listener to the new source and the new source's
     * SimEventListeners as SimEventListsners to this. the listeners of the new
     * source that are also SimEventSources
     *
     * @param source the source to set
     */
    public void setSource(BasicSimEntity source) {
        if (this.source != null) {
            this.source.removeSimEventListener(this);
            SimEventListener[] listeners = this.getSimEventListeners();
            for (SimEventListener listener : listeners) {
                this.removeSimEventListener(listener);
            }
        }
        this.source = source;
        for (SimEventListener listener : source.getSimEventListeners()) {
            this.addSimEventListener(listener);
        }
        this.source.addSimEventListener(this);
    }

}
