package simkit;
import java.util.*;
import java.beans.*;
import java.io.*;
import java.text.*;
/**
 * //TODO: add reallyVerbose and/or warn level scheme
 * @version $Id$
 * @author  Arnold Buss
 */
public class EventList {
    
    private SortedSet eventList;
    
    private double simTime;
    
    private SimEvent currentSimEvent;
    
    private HashMap eventCounts;
    
    private boolean verbose;
    
    private boolean singleStep;
    
    private boolean dumpEventSources;
    
    private SortedSet reRun;
    
    private String stopEventName;
    
    private int numberStopEvents;
    
    private boolean stopOnEvent;
    
    private boolean stopAtTime;
    
    private double stopTime;
    
    private boolean running;
    
    private SimEntity stopInstance;
    
    private HashSet ignoreOnDump;
    
    private int id;
    
    private DecimalFormat form;
    
    private boolean pauseAfterEachEvent;
    
    public EventList(int id) {
        eventList = Collections.synchronizedSortedSet(new TreeSet(new SimEventComp()));
        simTime = 0.0;
        running = false;
        eventCounts = new HashMap();
        reRun = Collections.synchronizedSortedSet(new TreeSet(new SimEntityComparator()));
        ignoreOnDump = new HashSet();
        this.id = id;
        setFormat("0.0000");
    }
    
    public int getID() { return id; }
    
    public double getSimTime() { return simTime; }
    
    public void setVerbose(boolean b) { verbose = b; }
    
    public boolean isVerbose() { return verbose; }
    
    public void setSingleStep(boolean b) {
        singleStep = b;
        setVerbose(singleStep);
        if (isPauseAfterEachEvent()) {
            setPauseAfterEachEvent(false);
        }
    }
    
    public boolean isSingleStep() { return singleStep; }
    
    public void setDumpEventSources(boolean b) { dumpEventSources = b; }
    
    public boolean isDumpEventSources() { return dumpEventSources; }
    
    public boolean isStopOnEvent() { return stopOnEvent; }
    
    public void setFormat(String format) {
        form = new DecimalFormat(format);
    }
    
    public int getNumberStopEvents() { return numberStopEvents; }
    
    public boolean isStopAtTime() { return stopAtTime; }
    
    public double getStopTime() { return stopTime; }
    
    public boolean isRunning() { return running; }
    
    public String getStopEventName() { return stopEventName; }
    
    public synchronized SimEvent getCurrentSimEvent() { return currentSimEvent; }
    
    public void setPauseAfterEachEvent(boolean b) { 
        pauseAfterEachEvent = b;
        if (isPauseAfterEachEvent()) {
            setSingleStep(false);
        }
    }
    
    public boolean isPauseAfterEachEvent() { return pauseAfterEachEvent; }
    
    public void reset() {
        clearEventList();
        currentSimEvent = null;
        simTime = 0.0;
        synchronized(reRun) {
            for (Iterator i = reRun.iterator(); i.hasNext(); ) {
                SimEntity simEntity = (SimEntity) i.next();
                if (simEntity.isPersistant()) {
                    simEntity.reset();
                    if (simEntity.isReRunnable()) {
                        simEntity.waitDelay("Run", 0.0, null,
                        Double.POSITIVE_INFINITY);
                    }
                }
                else {
                    removeListeners(simEntity);
                    i.remove();
                }
            }
        }
        if (isStopAtTime()) {
            stopAtTime(getStopTime());
        }
    }
    
    protected void removeListeners(SimEntity se) {
        SimEventListener[] seListener = se.getSimEventListeners();
        for (int i = 0; i < seListener.length; ++i) {
            se.removeSimEventListener(seListener[i]);
        }
        PropertyChangeListener[] pcListener = se.getPropertyChangeListeners();
        for (int i = 0; i < pcListener.length; ++i) {
            se.removePropertyChangeListener(pcListener[i]);
        }
    }
    
    protected void clearEventList() {
        while( !eventList.isEmpty() ) {
            SimEvent event = (SimEvent) eventList.first();
            eventList.remove(eventList.first());
            SimEventFactory.returnSimEventToPool(event);
        }
        eventCounts.clear();
    }
    
    protected void clearDeadEvents() {
        while ( !eventList.isEmpty() ) {
            SimEvent simEvent = (SimEvent) eventList.first();
            if (!simEvent.isPending()) {
                eventList.remove(simEvent);
                SimEventFactory.returnSimEventToPool(simEvent);
            }
            else {
                break;
            }
        }
    }
    
    public void stopAtTime(double time) {
        stopAtTime = true;
        stopOnEvent = false;
        stopTime = time;
        if (stopInstance == null) {
            stopInstance = new Stop();
            stopInstance.setEventListID(getID());
        }
        else {
            stopInstance.interruptAll("Stop", new Object[0]);
        }
        stopInstance.waitDelay("Stop", getStopTime() - getSimTime(), null, Double.NEGATIVE_INFINITY);
    }
    
    public void stopOnEvent(String eventName, int numberEvents) {
        stopOnEvent(eventName, new Class[0], numberEvents);
    }
    
    public void stopOnEvent(String eventName, Class[] signature, int numberEvents) {
        if (numberEvents > 0) {
            stopOnEvent = true;
            stopAtTime = false;
            StringBuffer fullName = new StringBuffer("do" + eventName +"(");
            for (int i = 0; i < signature.length; ++i) {
                fullName.append(signature[i].getName());
                if (i < signature.length - 1) { fullName.append(','); }
            }
            fullName.append(')');
            stopEventName = fullName.toString();
            numberStopEvents = numberEvents;
        }
        else {
            throw new IllegalArgumentException("Need positive number of events: " 
                + numberEvents);
        }
    }
    
    public void setUserDefinedStop() {
        stopAtTime = false;
        stopTime = Double.NaN;
        stopOnEvent = false;
    }
    
    public void scheduleEvent(SimEvent event) throws InvalidSchedulingException {
        if (event.getScheduledTime() < getSimTime()) {
            throw new InvalidSchedulingException("Attempt to reverse time!: " +
            event.getScheduledTime() + " > " + getSimTime());
        }
        else if (!Double.isNaN(event.getScheduledTime())) {
            boolean success = eventList.add(event);
            if (!success) {
                throw new InvalidSchedulingException("Problem adding event to " +
                "Event List: " + event);
            }
        }
    }
    
    public void startSimulation() {
        if (isRunning()) {
            throw new RuntimeException("Simulation already running!");
        }
        running = true;
        
        if (isVerbose()) { dump("Starting Simulation"); }
        
        if (isSingleStep()) { 
            System.err.println("Press [Enter] for next step; (s)top, (g)o or (f)inish");
        }
        
        while (!eventList.isEmpty() && isRunning()) {
            currentSimEvent = (SimEvent) eventList.first();
            eventList.remove(currentSimEvent);
            simTime = currentSimEvent.getScheduledTime();
            
            if (currentSimEvent.isPending()) {
                updateEventCounts(currentSimEvent);
                SimEntity simEntity = (SimEntity) currentSimEvent.getSource();
                simEntity.handleSimEvent(currentSimEvent);
                simEntity.notifyListeners(currentSimEvent);
                
                if (isStopOnEvent()) { checkStopEvent(); }
                if (isSingleStep()) { step(); }
                if (isVerbose()) { dump(""); }
                if (isPauseAfterEachEvent()) { pause(); }
            }
            SimEventFactory.returnSimEventToPool(currentSimEvent);
            currentSimEvent = null;
        }
        running = false;
    }
    
    public void step() {
        while(true) {
            try {
                char response = (char) System.in.read();
                if (  response == 's') { stopSimulation(); }
                if (  response == 'g') { setSingleStep(false); setVerbose(true); }
                if (  response == 'f') { setSingleStep(false); setVerbose(false);}
                if (  response == '\n') { return;}
            }
            catch (IOException e) { System.err.println(); e.printStackTrace(System.err); }
        }    
    }

    public void dump(String reason) {
        System.out.println(getEventListAsString(reason));
    }
    
    public void dump() {
        dump("");
    }
    
    protected void updateEventCounts(SimEvent event) {
        int[] serial = (int[]) eventCounts.get(event.getFullMethodName());
        if (serial == null) {
            serial = new int[] { 0 };
            eventCounts.put(event.getFullMethodName(), serial);
        }
        synchronized(serial) {
            serial[0]++;
            event.setSerial(serial[0]);
        }
    }

    protected void checkStopEvent() {
        if (isStopOnEvent() && currentSimEvent.getFullMethodName().equals(stopEventName) &&
                currentSimEvent.getSerial() >= numberStopEvents) {
            stopSimulation();
        }
    }
    
    public void pause() {
        running = false;
        if (isVerbose()) {
            System.out.println("Simulation is paused");
        }
    }
    
    public void stopSimulation() {
        clearEventList();
        running = false;
    }
    
    public void interrupt(SimEntity simEntity, String eventName) {
        clearDeadEvents();
        for (Iterator i = eventList.iterator(); i.hasNext(); ) {
            SimEvent event = (SimEvent) i.next();
            if ((event.getSource() == simEntity) &&
                (event.getEventName().equals(eventName)) &&
                (event.isPending())) {
                    i.remove();
                    SimEventFactory.returnSimEventToPool(event);
                    break;
            }
        }
    }
    
    public void interrupt(SimEntity simEntity, String eventName,
            Object[] parameters) {
        clearDeadEvents();
        for (Iterator i = eventList.iterator(); i.hasNext(); ) {
            SimEvent event = (SimEvent) i.next();
            if ((event.getSource() == simEntity) &&
                (event.getEventName().equals(eventName)) &&
                (event.interruptParametersMatch(parameters)) &&
                (event.isPending())) {
                    i.remove();
                    SimEventFactory.returnSimEventToPool(event);
                    break;
            }
        }
    }
    
    public void interruptAll(SimEntity simEntity) {
        clearDeadEvents();
        for (Iterator i = eventList.iterator(); i.hasNext(); ) {
            SimEvent simEvent = (SimEvent) i.next();
            if (simEvent.getSource() == simEntity) {
                i.remove();
                SimEventFactory.returnSimEventToPool(simEvent);
            }
        }
    }
    
    public void interruptAll(SimEntity simEntity, String eventName) {
        clearDeadEvents();
        for (Iterator i = eventList.iterator(); i.hasNext(); ) {
            SimEvent simEvent = (SimEvent) i.next();
            if ((simEvent.getSource() == simEntity) &&
                (simEvent.getEventName().equals(eventName)) ){
                i.remove();
                SimEventFactory.returnSimEventToPool(simEvent);
            }
        }
    }
    
    public void interruptAll(SimEntity simEntity, String eventName,
        Object[] parameters) {
        clearDeadEvents();
        for (Iterator i = eventList.iterator(); i.hasNext(); ) {
            SimEvent simEvent = (SimEvent) i.next();
            if ((simEvent.getSource() == simEntity) &&
                (simEvent.getEventName().equals(eventName)) &&
                (simEvent.interruptParametersMatch(parameters)) ){
                i.remove();
                SimEventFactory.returnSimEventToPool(simEvent);
            }
        }
    }
    
    public void addRerun(SimEntity simEntity) {
        reRun.add(simEntity);
    }
    
    public void removeRerun(SimEntity simEntity) {
        reRun.remove(simEntity);
    }
    
    public void clearRerun() {
        reRun.clear();
    }

    public Set getRerun() {
        return new HashSet(reRun);
    }
    
    public void addIgnoreOnDump(String eventName) {
        ignoreOnDump.add(eventName);
    }
    
    public void removeIgnoreOnDump(String eventName) {
        ignoreOnDump.remove(eventName);
    }
    
    public Set getIgnoredEvents() {
        return new HashSet(ignoreOnDump);
    }

//TODO: add event source if set.    
    protected String getEventListAsString(String reason) {
        clearDeadEvents();
        StringBuffer buf = new StringBuffer();
        if (currentSimEvent != null) {
            buf.append("Time: ");
            buf.append(form.format(getSimTime()));
            buf.append("\tCurrentEvent: ");
            buf.append(currentSimEvent.paramString());
            buf.append(' ');
            buf.append('[');
            buf.append(currentSimEvent.getSerial());
            buf.append(']');
            buf.append(SimEntity.NL);
        }
        buf.append("** Event List ");
        buf.append(getID());
        buf.append(" -- ");
        buf.append(reason);
        buf.append(" **");

        buf.append(SimEntity.NL);
        if (eventList.isEmpty()) {
            buf.append("            << empty >>");
            buf.append(SimEntity.NL);
        }
        else {
            synchronized(eventList) {
                for (Iterator i = eventList.iterator(); i.hasNext(); ) {
                    SimEvent simEvent = (SimEvent) i.next();
                    if (ignoreOnDump.contains(simEvent.getEventName())) {
                        continue;
                    }
                    if (simEvent.isPending()) {
                        buf.append(simEvent);
                        buf.append(SimEntity.NL);
                    }
                }
            }
        }
        buf.append(" ** End of Event List -- ");
        buf.append(reason);
        buf.append(" **");
        buf.append(SimEntity.NL);
        return buf.toString();
    }
    
    //TODO: Event List class stuff.
}
