package simkit;

import simkit.util.*;
import simkit.util.IndexedPropertyChangeEvent;
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

/////////////////////////// Copyright Notice //////////////////////////
//                                                                   //
// This simkit package or sub-package and this file is Copyright (c) //
// 1997, 1998, 1999 by Kirk A. Stork and Arnold H. Buss.             //
//                                                                   //
// Please forward any changes, comments or suggestions to:           //
//   abuss@nps.navy.mil                                              //
//                                                                   //
///////////////////////////////////////////////////////////////////////

/**
* Default implementation of a SimEntity.
*
* <P>This implementation provides all the basic requirements
* of a SimEntity.  
*
* <P>Changes on 11/10/97 (AB):
*   <OL>
*   <LI> Made class abstract, since it doesn't make sense to have an
*       actual instance of SimEntityBase itself.
*   
*   <LI> Added Hashtable doMethods to store all "do" methods.
*   </OL>
*
* <P> 22 Oct 1998 - SimEntityBase now only needs to implement SimEntity,
* since SimEntity extends SimEventSource and SimEventListener.
*
* <P>Note that "do" methods must still be public [due to a bug(?) in jdk1.1?].
*      
* <P> 28 March 1999 - Removed local event list
* <P> 25 August 1999 - Fixed bug that called overridden "do" methods twice.
*
*  <P> Notes (30 Oct):
*       <P> Make Hashtables static => Hastable2's?
*       <p> Is namesAndSignatures really necessary??
*
* @author Arnold Buss
* @author K. A. Stork
* @version 1.1.1
*
**/

public abstract class SimEntityBase implements SimEntity, PropertyChangeSource {
   
   private static int         serializer = 0;
   private static  String PREFIX = "do";

    private static Hashtable2 allDoMethods;
    private static Hashtable2 allNamesAndSignatures;
    private static Hashtable allImpossibleEvents;

    static {
        allDoMethods = new Hashtable2();
        allNamesAndSignatures = new Hashtable2();
        allImpossibleEvents = new Hashtable();
    }
   
   private double             priority;
   private String             name;
   private int                serial;

   private boolean verbose;   
   private Vector listeners;
   
   private  Map doMethods;
   private  Map namesAndSignatures;

   private  String    stopEventName;
   private  int       numberEvents;

   private boolean reRunnable;
   private PropertyChangeDispatcher psp;
                                                                                   
   private String nameSuffix;
   private String namePrefix;

   private static boolean debug;

   private SimEventSource sourceProxy;
   
/**
Base constructor.
**/
    public SimEntityBase(String name, double priority) {
        psp = new PropertyChangeDispatcher(this);
        priority     = priority;
        serial       = ++serializer;
        setNamePrefix("");
        setNameSuffix("." + serial);

        if ( name.equals(DEFAULT_ENTITY_NAME) ) {
            this.name = getClass().getName() ;
        }
        else {
            this.name         = name;
        }
        listeners = new Vector();

        doMethods = (Map) allDoMethods.get(this.getClass());
        if (doMethods == null) {
            doMethods = new Hashtable();
            Method[] methods = this.getClass().getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().startsWith(PREFIX)) {
                    doMethods.put(getFullMethodName(methods[i]), methods[i]);
                    String methodName = methods[i].getName();
                } // if
            }  // for
            doMethods = new Hashtable(doMethods);
            allDoMethods.put(this.getClass(), doMethods);
        } // if

        sourceProxy = new BasicSimEventSource();

        namesAndSignatures = (Map) allNamesAndSignatures.get(this.getClass());
        if (namesAndSignatures == null) {
            namesAndSignatures = new Hashtable();
            for (Iterator i = doMethods.keySet().iterator(); i.hasNext(); ) {
                Method nextDoMethod = (Method) doMethods.get(i.next());
                Vector v = null;
                if (namesAndSignatures.containsKey(nextDoMethod.getName())) {
                    v = (Vector)namesAndSignatures.get(nextDoMethod.getName());
                }
                else {
                    v = new Vector();
                    namesAndSignatures.put(nextDoMethod.getName(), v);
                }
                v.add(nextDoMethod.getParameterTypes());
            }
            namesAndSignatures = new Hashtable(namesAndSignatures);            
            allNamesAndSignatures.put(this.getClass(), namesAndSignatures);
        }

        reRunnable = doMethods.containsKey("doRun()");
        Schedule.addRerun(this);
    }

/**
Convenience constructors.
**/
   public SimEntityBase() {
      this(DEFAULT_ENTITY_NAME, DEFAULT_PRIORITY);
   }
   
   public SimEntityBase(String name) {
      this(name, DEFAULT_PRIORITY);
   }
   
   public SimEntityBase(double priority) {
      this(DEFAULT_ENTITY_NAME, priority);
   }
   
   public void setVerbose(boolean v) {verbose = v;}
   public boolean isVerbose() {return verbose;}
   
/*
  Four-parameter methods
*/
    public SimEvent waitDelay(
              String      methodName,
              double      delay,
              Object[]    parameters,
              double      eventPriority) {

        SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, parameters, eventPriority);
        attemptSchedule(event);
        return event;
   }    

    public SimEvent waitDelay(
              String      methodName,
              double      delay,
              Object      parameter,
              double      eventPriority) {
        SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, new Object[] {parameter}, eventPriority);
        attemptSchedule(event);
        return event;
    }

/*
  Three parameter methods
*/

   public SimEvent waitDelay(
              String      methodName,
              double      delay,
              Object[]    parameters) {
        SimEvent event =  SimEventFactory.createSimEvent(this, methodName, delay, parameters, DEFAULT_PRIORITY);
        attemptSchedule(event);
        return event;
   }

   public SimEvent waitDelay(
              String      methodName,
              double      delay,
              double      eventPriority) {
      SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, new Object[] {}, eventPriority);
      attemptSchedule(event);
      return event;
   }

    public SimEvent waitDelay(
              String      methodName,
              double      delay,
              Object      parameter ) {

      SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, new Object[] {parameter});
      attemptSchedule(event);
      return event;
    }
   
/*
  Two parameter method
*/

   public SimEvent waitDelay(
              String      methodName,
              double      delay )  {
      SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, new Object[] {}, DEFAULT_PRIORITY);
      attemptSchedule(event);
      return event;
   }
   
/*
   From previous version -- deprecated
   @deprecated
*/

   public SimEvent waitDelay(
              String      methodName,
              Object      parameter,
              double      delay,
	      String      eventName ) {
        SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, new Object[] {parameter}, DEFAULT_PRIORITY);  
        attemptSchedule(event);
        return event;
   }

    protected void attemptSchedule(SimEvent event) {
         try {
            Schedule.scheduleEvent(event);
         }
         catch (InvalidSchedulingException e) {System.err.println(e);}
    }
                  
/**
 *  Implements simkit.SimEntity.interrupt(String, Object[]).
**/   
   public void interrupt(String eventName, Object[] parameters) {
      Schedule.interrupt(this, eventName, parameters);
   }

/**
  Convenience interrupt methods
**/

   public void interrupt(String eventName, Object parameter) {
     interrupt(eventName, new Object[] {parameter});
   }

   public void interrupt(String eventName) {
      Schedule.interrupt(this, eventName);
   }

   public void interrupt() {
      Schedule.interrupt(this);
   }

/**
Implement simkit.SimEntity.interruptAll(String, Object[]).
**/   
   public void interruptAll(String eventName, Object[] parameters) {
      Schedule.interruptAll(this, eventName, parameters);
   }

/**
  Convenience interruptAll methods
**/

   public void interruptAll(String eventName, Object parameter) {
     interruptAll(eventName, new Object[] {parameter});
   }

   public void interruptAll(String eventName) {
      Schedule.interruptAll(this, eventName);
   }

   public void interruptAll() {
      Schedule.interruptAll(this);
   }

/**
 *  Implements simkit.SimEntity.getPriority().
**/
   public final double getPriority () {
      return priority;
   }
   
/**
 *  Implements simkit.SimEntity.setPriority()
**/
   public final void setPriority (double d) {
      priority = d;
   }
   
/**
A default string description of this entity,

name (Entity Priority)
**/
    public String toString() {
        return this.getName();
    }

   public void setNameSuffix(String suffix) {nameSuffix = suffix;}
   public String getNameSuffix() {return nameSuffix;}
   public void setNamePrefix(String prefix) {namePrefix = prefix;}
   public String getNamePrefix() {return namePrefix;}

/**
 * Implements simkit.Named.
**/
    public String getName() {
        StringBuffer buf = new StringBuffer();
        buf.append(getNamePrefix());
        buf.append(name);
        buf.append(getNameSuffix());
        return buf.toString();
    }

/**
 * Implements simkit.Named.
**/
   public void setName(String s) {
      name = s;
      if (!name.equals(getClass().getName()) ){
        setNameSuffix("");
      }
   }

   public int getSerial() {return serial;}


// implements SimEventListener

    public synchronized void handleSimEvent(SimEvent event) {
        processSimEvent(event);
        notifyListeners(event);
        SimEventFactory.returnSimEventToPool(event);
    }

   public synchronized void processSimEvent(SimEvent event) {
      Method m = null;
      String methodName =  event.getMethodName();
// Do not process other SimEntityBase's "doRun()" methods
      if ( !event.getSource().equals(this) && event.getFullMethodName().equals("doRun()")) {
        return;
      } // if
// If no method of that name, then there is no hope.
        if (!namesAndSignatures.containsKey(methodName)) {
            if (debug) {
                System.out.println("No method of name " + methodName + " -- giving up...");
            } // if
            return;
        } // if
        if (verbose) {
            System.out.println("Event processed by " + this + ": " + event);
        } // if

        try {
                // This method has either happened before or matches one in method exactly
if (isDebug()) {
    System.out.println("doMethods hashcode = " + doMethods.hashCode());
    System.out.println("namesAndSignatures hashcode = " + namesAndSignatures.hashCode());
    System.out.println("doMethods: " + doMethods);
}
            if (doMethods.containsKey(event.getFullMethodName())) {
                m = (Method) doMethods.get(event.getFullMethodName());
                m.invoke(this, event.getParameters());
//                updateEventCounts(event);
	        } // if
         else { 
	         if (verbose) {
                System.out.println(
                  "Master lookup failed, trying namesAndSignatures..." +
                   " Method Name = " + event.getFullMethodName());
             }
// Now, we are here only because there is some chance that there will be a match.
// First  
                Object[] params = event.getParameters();
                for (Iterator iter = ( (Vector) namesAndSignatures.get(methodName)).iterator(); iter.hasNext(); ) {
if (isDebug()) {
    System.out.println("namesAndSignatures: " + namesAndSignatures.hashCode());
}
                    Class[] signature = (Class[]) iter.next();
                    if (debug) {
                        System.out.print("  Signature: (");
                        for (int k = 0; k < signature.length; k++) {
                            System.out.print(signature[k]);
                            if (k < signature.length - 1) {System.out.print(", ");}
                        } // for
                        System.out.println(")");
                    }  // if
                    if (signature.length == params.length) {
                        boolean match = true;
                        if (debug) {
                            System.out.println("There are " + signature.length + " arguments to check...");
                        } // if
                        for (int i = 0; i < signature.length; i++) {
                            if (debug) {
                                System.out.println("\tChecking: " + signature[i]);
                            }  // if
                            if (signature[i].isPrimitive()) {
                                if (signature[i].equals(Float.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Float.class);
                                }  // if
                                if (signature[i].equals(Integer.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Integer.class);
                                }  // if
                                if (signature[i].equals(Double.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Double.class);
                                }  // if
                                if (signature[i].equals(Long.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Long.class);
                                }  // if
                                if (signature[i].equals(Boolean.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Boolean.class);
                                }  // if
                                if (signature[i].equals(Byte.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Byte.class);
                                }  // if
                                if (signature[i].equals(Short.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Short.class);
                                }  // if
                                if (signature[i].equals(Character.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Character.class);
                                }  // if
                            }  // if
                            else {
                                match = match && signature[i].isAssignableFrom(params[i].getClass());
                            }  // else
                            if (verbose) {
                                System.out.println(signature[i].getName() + " ?=? " +
                                    params[i].getClass().getName());
		                    } // if
                        } // for
                        if (match) {
      		                try {
                                if (debug) {
                                    System.out.println("Match found: " + event.getFullMethodName());
                                }
                                m = getClass().getMethod(methodName, signature);
                                m.invoke(this, params);
//                                updateEventCounts(event);
                                doMethods.put(event.getFullMethodName(), m);
		                    }
                            catch (NoSuchMethodException f) {f.printStackTrace(System.err);}
                        }  //if
                    }
                }
            }
//        }
	 
    }
      catch (NullPointerException e) {
         System.out.println("Attempted method: " + event.getFullMethodName());
         e.printStackTrace();
      }
      catch(IllegalAccessException e) {
         System.out.println("Attempted method: " + m );
         System.out.println("  [key = " + event.getFullMethodName() +"]");
         System.out.println("  [name = " + event.getMethodName() + "]");
         System.out.print  ("  [params = (");
         for (int i = 0; i < event.getParameters().length; i++) {
            System.out.print(event.getParameters()[i].getClass().getName());
               if (i < event.getParameters().length - 1) {System.out.print(",");}
         } 
         System.out.println(") ]");
         System.out.println("This object: " + 
         Integer.toHexString(this.hashCode()) );
         System.out.print("This class: " );
         Class c = this.getClass();
         while(!c.equals(SimEntityBase.class)) {
            System.out.print(c);
             c = c.getSuperclass();
         }
         System.out.println(NL + "Method's object: " + 
         Integer.toHexString(event.getSource().hashCode()) );
         System.out.println("Method's class: " + m.getDeclaringClass());
         e.printStackTrace();
      }  //shouldn't happen
      catch(IllegalArgumentException e) {e.printStackTrace();} //shouldn't happen
      catch(InvocationTargetException e) {
          e.getTargetException().printStackTrace();
      }
        finally {
        }
   }

/**
  * <P>This method is added by TRAC-WSMR, Authot Lt Col Olson, USMC.
  * <code>dumpDoMethodsStr</code> returns a String containing the same information as 
  * <code>dumpDoMethods</code>.  This method allows a developer to place the information
  * in a graphical user interface, textbox, or similar output device.
  *
  * <P> Method rewritten by A. Buss to use newer Iterator (vice Enumeration) and
  * to use <CODE>StringBuffer</CODE> for speed.  The underscores are also made to
  * exactly match the length of the heading.
  * 
  * @return String representation of this entity's "doMethods"
 **/
    public String dumpDoMethodsStr() {
        String name = getName();
        StringBuffer buf = new StringBuffer();
        buf.append("Event Methods for ");
        buf.append(name);
        buf.append(NL);
        for (int i = 0; i < 18 + name.length(); i++) {
            buf.append('=');
        }
        buf.append(NL);
        for (Iterator i = doMethods.keySet().iterator(); i.hasNext(); ) {
            buf.append(i.next());
            buf.append(NL);
        }
        return buf.toString();
    }

/**
 *  Prints out the "do" methods for this SimEntity
**/
   public void dumpDoMethods() {
      Schedule.getOutputStream().println(this.dumpDoMethodsStr());
   }

/**
  * <P> This method is added by TRAC-WSMR, Authot Lt Col Olson, USMC.
  * <code>dumpNamesAndSignaturesStr()</code> returns a String containing the same information as
  * <code>dumpNamesAndSignatures()</code>.  This method allows a developer to place the information
  * in a graphical user interface, textbox, or similar output device.
  *
  * <P> A. Buss modified this to utilize Iterators and StringBuffer.  Also, the corresponding
  * old method now simply invokes this one.
  *
  * @return String representation of this entity's "NamesAndSignatures"
  * 
 **/
    public String dumpNamesAndSignaturesStr() {
        String name = getName();
        StringBuffer buf = new StringBuffer();
        buf.append("Names and signatures for ");
        buf.append(name);
        buf.append(NL);
        for (int i = 0; i < 25 + name.length(); i++){
            buf.append('=');
        }
        buf.append(NL);
        for (Iterator i = namesAndSignatures.keySet().iterator(); i.hasNext(); ) {
            Object methodName = i.next();
            buf.append(methodName);
            buf.append(':');
            buf.append('\t');
            buf.append('(');
            for (Iterator j = ((List)namesAndSignatures.get(methodName)).iterator(); j.hasNext();) {
                 Class[] aClass = (Class[]) j.next();
                 for (int k = 0; k < aClass.length; k++) {
                    buf.append(aClass[k].getName());
                    if (k < aClass.length - 1) {buf.append(',');}
                }
                buf.append(')');
                if (i.hasNext()) {buf.append(NL);}
            }
        }
        return buf.toString();
    }

/**
 *  Prints names and signatures of "do" methods to output specified by Schedule. 
**/
    public void dumpNamesAndSignatures() {
        Schedule.getOutputStream().println(dumpNamesAndSignaturesStr());
    }
/**
  * Interrupt all this SimEntity's events when a certain time is reached
  * @deprecated - Use stopAtTime(double);
  * @param endingTime The ending time at which all this SimEntity's events are interrupted.
**/
   public void stopOnTime(double endingTime) {
        this.stopAtTime(endingTime);
   }

/**
  * Interrupt all this SimEntity's events when a certain time is reached
  * @param endingTime The ending time at which all this SimEntity's events are interrupted.
**/
   public void stopAtTime(double endingTime) {
      new Stop().waitDelay("StopSimEntity", endingTime, this, -Double.MAX_VALUE);
   }

/**
  * Interrupt all this SimEntity's events when a certain event count is reached
  * @param eventName The event to be used.
  * @param count The number of occurences of the event beyond which no events are
  *        scheduled.
**/
/*
   public void stopOnEvent(String eventName, int count) {
      resetEventCounts();
      stopEventName = eventName;
      numberEvents = count;
   }
*/

/**
  * Counts for events
**/
/*
   protected void updateEventCounts(SimEvent currentSimEvent) {
     int serial = 1;
     synchronized (eventCounts) {
       if (eventCounts.containsKey(currentSimEvent.getEventName())) {
         serial = ((Integer)
	       eventCounts.get(currentSimEvent.getEventName())).intValue() + 1;
       }
       eventCounts.put(currentSimEvent.getEventName(), new Integer(serial));
       if (currentSimEvent.getEventName().equals(stopEventName) &&
          ((Integer) eventCounts.get(currentSimEvent.getEventName())).intValue() >= numberEvents) {
         interruptAll();
       }
     }
  }
   public void resetEventCounts() { eventCounts.clear(); }
*/
    public void reset() {
        interruptAll();
    }

    public boolean isReRunnable() { return reRunnable;  }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        psp.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        psp.removePropertyChangeListener(pcl);
    }

    public void firePropertyChange(PropertyChangeEvent event) {
        psp.firePropertyChange(event);
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        psp.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        psp.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void firePropertyChange(String propertyName, boolean newValue) {
        psp.firePropertyChange(propertyName, null, new Boolean(newValue));
    }

    public void firePropertyChange(String propertyName, int newValue) {
        psp.firePropertyChange(propertyName, null, new Integer(newValue));
    }

    public void firePropertyChange(String propertyName, double newValue) {
        psp.firePropertyChange(propertyName, null, new Double(newValue));    }

    public void firePropertyChange(String propertyName, Object newValue) {
        psp.firePropertyChange(propertyName, null, newValue);
    }

    public void fireIndexedPropertyChange(int index, String propertyName, Object oldValue,
            Object newValue) {
        psp.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, oldValue, newValue, index));
    }

    public void fireIndexedPropertyChange(int index, String propertyName, Object newValue) {
        psp.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, null, newValue, index));
    }

    public void fireIndexedPropertyChange(int index, String propertyName, int newValue) {
        psp.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, null, new Integer(newValue), index));
    }

    public void fireIndexedPropertyChange(int index, String propertyName, double newValue) {
        psp.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, null, new Double(newValue), index));
    }

    public void fireIndexedPropertyChange(int index, String propertyName, boolean newValue) {
        psp.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, null, new Boolean(newValue), index));
    }

    public void setProperty(String property, Object value) {
        psp.setProperty(property, value);
    }

    public void setProperty(String property, Object value, Object defaultValue) {
        psp.setProperty(property, value != null ? value : defaultValue);
    }

    public Object getProperty(String property) {
        return psp.getProperty(property);
    }

    public Object getProperty(String property, Object defaultValue) {
        return psp.getProperty(property, defaultValue);
    }


   public static void setDebug(boolean b) {debug = b;}
   public static boolean isDebug() {return debug;}

/**
 *  @param m The method for which to get the full name (unfortunately the jdk does
 *           not appear to provide this particular String...to my knowledge).
 *  @return The full method name of m, including signature, as a String.
**/
    public static String getFullMethodName(Method m) {
        String name = m.toString();
        return m.getName() + getSignatureString(m);
    }

/**
 *  @param m The method for which to get the signature as a String (unfortunately
 *  the jdk does not appear to provide this particular String either...to my knowledge).
 *  @return The signature m as a String.
**/
    public static String getSignatureString(Method m) {
        String name = m.toString();
        return name.substring(name.indexOf('('));
    }

    public static String getPrefix() { return PREFIX; }

/**
  * register listener
  * @param SimEventListener s
**/
    public void addSimEventListener(SimEventListener s) {
        sourceProxy.addSimEventListener(s);
    }
 /**
  * unregister listener
  * @param SimEventListener s
**/
    public void removeSimEventListener(SimEventListener s) {
        sourceProxy.removeSimEventListener(s);
    }
/**
  * notify listeners
  * @param SimEvent event
**/
public void notifyListeners(SimEvent event) {
    sourceProxy.notifyListeners(event);
}


}
