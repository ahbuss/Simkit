package simkit;
/**
 *  An abstract basic implementation of <CODE>SimEntity</CODE> that does not use
 *  reflection but rather
 *  relies on the user subclassing and implementing the <CODE>SimEventListener</CODE> method
 *  <CODE>processSimEvent(SimEvent)</CODE>.  The easiest way is for the user to directly dispatch the
 *  event based on the <CODE>SimEvent</CODE> name and paramters. For eexample, if there is an
 *  Event method called "<CODE>doThis()</CODE>", then a simple implementation of
 *  <CODE>processSimEvent(SimEvent)</CODE> is:
 <PRE>
 public void processSimEvent(SimEvent event) {
   if (event.getMethodName().equals("doThis")) {this.doThis();}
 }
 </PRE>
 *  The primary disadvantage of this class (vice <CODE>SimEntityBase</CODE>) is
 *  that the user must remember to invoke each "do" method and pass it the
 *  correct parameters, if necessary.  Any further subclasses must remember to
 *  override <CODE>processSimEvent(SimEvent)</CODE> to
 *  add all additional "do" methods (or else they will be omitted altogether without
 *  comment).  The advantages are that it is slightly easier to understand, since
 *  it doesn't rely on the complex reflection of <CODE>SimEntityBase</CODE>.  This
 *  also runs about twice as fast as <CODE>SimEntityBase</CODE> based on very
 *  small-sample ad-hoc tests.
 *
 *  <P>One extremely important point about writing a <CODE>processSimEvent(SimEvent)</CODE>
 *  method: <CODE>processSimEvent(SimEvent)</CODE> <STRONG>must not</STRONG>
 *  execute a <CODE>doRun())</CODE> method if it is not the "owner" (i.e. if it
 *  gets a "Run" event by through listening).  Failure to do this will result in
 *  multiple "Run" events for the same object at the start of a simulation.
 *  @author Arnold Buss
 *  @version 0.5
**/

import java.util.*;
import java.lang.reflect.*;
import java.beans.*;

public abstract class BasicSimEntity extends BasicSimEventSource implements SimEntity {

    public static final String DEFAULT_NAME = "BasicSimEntity";
    private static int nextSerial;
    static {
        nextSerial = 0;
    }
    
    private String name;
    private double priority;
    private int serial;
    protected PropertyChangeSupport property;

    public BasicSimEntity(String name) {
        super();
        serial = ++nextSerial;
        this.setName(name);
        property = new PropertyChangeSupport(this);
        Schedule.addRerun(this);
    }

    public BasicSimEntity() {
        this(DEFAULT_NAME);
    }

    public boolean isReRunnable() {
        boolean reRunnable = false;
        try {
            this.getClass().getMethod("doRun", null);
            reRunnable = true;
        }
        catch (NoSuchMethodException e) {}
        return reRunnable;
    }

    public void reset() {
        interruptAll();
    }

    public void setName(String name) {this.name = name;}
    public String getName() {return name;}

    public void setPriority(double p) {priority = p;}
    public double getPriority() {return priority;}

    public void interruptAll() {
        Schedule.interruptAll(this);
    }
    public void interruptAll(String eventName, Object[] parameters) {
        Schedule.interruptAll(this, eventName, parameters);
    }
    public void interrupt() {
        Schedule.interrupt(this);
    }

    public void interrupt(String eventName, Object[] parameters) {
        Schedule.interrupt(eventName, parameters);
    }

    public SimEvent waitDelay( String eventName, double delay, Object[] parameters,
          double priority){
        SimEvent newEvent = SimEventFactory.createSimEvent(
                           this,
                           eventName,
                           delay,
                           parameters,
                           priority);
        try {
            Schedule.scheduleEvent(newEvent);
        }
        catch(InvalidSchedulingException e) {System.err.println(e); e.printStackTrace(System.err);}
        return newEvent;
    }

    public SimEvent waitDelay(String name, double delay, Object[] params) {
        return this.waitDelay(name, delay, params, SimEvent.DEFAULT_PRIORITY);
    }

    public SimEvent waitDelay(String name, double delay) {
        return this.waitDelay(name, delay, null, SimEvent.DEFAULT_PRIORITY);
    }
    public SimEvent waitDelay(String name, double delay, Object param) {
        return this.waitDelay(name, delay, new Object[] {param}, SimEvent.DEFAULT_PRIORITY);
    }

    protected static boolean parametersMatch(Object[] fromEvent, Object[] fromInterrupt) {
        boolean match = true;
        if ( (fromEvent == null && fromInterrupt != null) ||
                (fromEvent == null && fromInterrupt != null) ) {
            match = false;
        }
        else if ((fromEvent == null) && (fromInterrupt == null)) {
            match = true;
        }
        else if (fromEvent.length != fromInterrupt.length) {
            match = false;
        }
        else {
            for (int i = 0; i < fromEvent.length; i++) {
                if (! (fromEvent[i].equals(fromInterrupt[i])) ) {
                    match = false;
                    break;
                }
            }
        }
        return match;
    }

    public abstract void handleSimEvent(SimEvent event) ;

    public abstract void processSimEvent(SimEvent event);

    public int getSerial() { return serial; }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        property.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        property.removePropertyChangeListener(listener);
    }
    
}
