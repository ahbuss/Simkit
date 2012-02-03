package simkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import simkit.util.LinkedHashMap2;

/**
 *  A modified version of SimEntityBase that allows the use of protected
 * event methods. Warning: Currently a prototype.
 *
 *  @author Arnold Buss
 *  @author K. A. Stork
 *  @author John Ruck, R&A
 *  @version $Id$
 *
 **/
public abstract class SimEntityBaseProtected extends BasicSimEntity {
    
    public static final Logger log = Logger.getLogger("simkit");
/**
* A two dimensional Hash table used to cache doMethods
* for all SimEntityBases. Keyed by Class and Method.
**/
    private static LinkedHashMap2<Class, String, Method> allDoMethods;

/**
* A two dimensional Hash table used to hold the
* names and signatures of all doMethods of all SimEntityBases.
* Keyed by Class and Method name.
**/
    private static LinkedHashMap2<Class, String, List<Class<?>[]>> 
            allNamesAndSignatures;

    /**
     * True if the entity has a doRun method.
     */
    protected boolean reRunnable = false;
    
    /**
     * True if the entity has a doRun method. Overrides BasicSimEntity to 
     * allow the doRun method to be protected or private.
     */
    public boolean isReRunnable() {
        return reRunnable;
    }
    
    static {
        allDoMethods = new LinkedHashMap2<Class, String, Method>();
        allNamesAndSignatures = new LinkedHashMap2<Class, String, List<Class<?>[]>>();
    }
    
/**
* If true, print debug information
**/
    private static boolean debug = false;
 
    /**
     * Contruct a new SimEntityBaseProtected with the given name and
     * event priority.
     * @param name The name of the entity.
     * @param priority The default priority for processing this entity's events.
     **/
    public SimEntityBaseProtected(String name, Priority priority) {
        super(name, priority);
        log.warning("??? Warning: SimEntityBaseProtected is still a prototype and may not work.");
        Map<String, Method> doMethods = allDoMethods.get(this.getClass());
        if (doMethods == null) {
            doMethods = new LinkedHashMap<String, Method>();
            Method[] methods = this.getClass().getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().startsWith(EVENT_METHOD_PREFIX)) {
                    doMethods.put(getFullMethodName(methods[i]), methods[i]);
                    String methodName = methods[i].getName();
                } // if
            }  // for
            allDoMethods.put(this.getClass(), doMethods);
        } // if
        
        Map<String, List<Class<?>[]>> namesAndSignatures = allNamesAndSignatures.get(this.getClass());
        if (namesAndSignatures == null) {
            namesAndSignatures = new LinkedHashMap<String, List<Class<?>[]>>();
            for (Method nextDoMethod : doMethods.values()) {
                List<Class<?>[]> v = null;
                if (namesAndSignatures.containsKey(nextDoMethod.getName())) {
                    v = namesAndSignatures.get(nextDoMethod.getName());
                }
                else {
                    v = new ArrayList<Class<?>[]>();
                    namesAndSignatures.put(nextDoMethod.getName(), v);
                }
                v.add(nextDoMethod.getParameterTypes());
            }
            allNamesAndSignatures.put(this.getClass(), namesAndSignatures);
        }
    }
    
/**
* Construct a new SimEntityBaseProtected with a default name and priority.
* The name is the class name plus a unique serial number.
**/
    public SimEntityBaseProtected() {
        this(DEFAULT_ENTITY_NAME, DEFAULT_PRIORITY);
        setName(getClass().getSimpleName() + '.' + getSerial());
    }
    
/**
* Construct a new SimEntityBaseProtected with  given name and a default priority.
* @param name The name of the entity.
**/
    public SimEntityBaseProtected(String name) {
        this(name, DEFAULT_PRIORITY);
    }
    
/**
* Construct a new SimEntityBaseProtected with a default name and 
* the given priority.
* The name is the class name plus a unique serial number.
* @param priority The priority for processing this entity's events.
**/
    public SimEntityBaseProtected(Priority priority) {
        this(DEFAULT_ENTITY_NAME, priority);
        setName(getClass().getSimpleName() + '.' + getSerial());
    }
 
/**
* Gets all the do Methods for this entity. This includes public, private, and
* protected events defined anywhere in the inheritence. 
*/
    protected Map<String, Method> getDoMethods() {
        Map<String, Method> doMethods = new LinkedHashMap<String, Method>();
        // I really wanted to handle this with recursion, but couldn't.
        // Walk up the inheritance adding any method+signatures we haven't added yet.
        // When getClass() is called on Object we get a null and stop.
        Class clazz = this.getClass();
        while (clazz != null) { 
            if (debug) System.out.println("+++ Checking class: " + clazz.getName() + " for do methods");
            Method[] methods = clazz.getDeclaredMethods(); //Get methods declared in this class.
            for (int i = 0; i < methods.length; i++) {
                String fullName = getFullMethodName(methods[i], true); //name + wrapped signature.
                if (debug) System.out.print("\tFound: " + fullName + "...");
            // If it is a do method and has not been added then add it.
                if (fullName.startsWith(EVENT_METHOD_PREFIX) && !doMethods.containsKey(fullName)) {
                    doMethods.put(fullName,methods[i]);
                    if (methods[i].getName().equals("doRun")) { 
                        reRunnable = true;
                        if (debug) System.out.print("Found a doRun, setting reRunnable to true ... ");
                    } //endif doRun.
                    if (debug) System.out.println("*** added it***");
                } else {
                    if (debug) System.out.println("did not add");
                }// if fullName ...
            } //for
            clazz = clazz.getSuperclass();
        }//while
        return doMethods;
    }

/**
* Process the given SimEvent. If the Method signature does not match any for
* this entity, the event is ignored. Also other entity's doRun events are ignored.
* Just calls processSimEvent.
*/   
    public synchronized void handleSimEvent(SimEvent event) {
        processSimEvent(event);
    }
    
/**
* Process the given SimEvent. If the Method signature does not match any for
* this entity, the event is ignored. Also other entity's doRun events are ignored.
*/   
    public synchronized void processSimEvent(SimEvent event) {
        if (event == null) { return; }
        Method m = null;
        String methodName =  event.getMethodName();
        // Do not process other SimEntityBase's "doRun()" methods
        if ( !event.getSource().equals(this) && event.getFullMethodName().equals("doRun()")) {
            return;
        } // if
        // If no method of that name, then there is no hope.
        Map<String, List<Class<?>[]>> namesAndSignatures = allNamesAndSignatures.get(this.getClass());
        if (!namesAndSignatures.containsKey(methodName)) {
            if (debug) {
                System.out.println("No method of name " + methodName + " -- giving up...");
            } // if
            return;
        } // if
        if (isVerbose()) {
            System.out.println("Event processed by " + this + ": " + event);
        } // if
        
        try {
            Map<String, Method> doMethods = allDoMethods.get(this.getClass());
            // This method has either happened before or matches one in method exactly
            if (isDebug()) {
                System.out.println("doMethods hashcode = " + doMethods.hashCode());
                System.out.println("namesAndSignatures hashcode = " + namesAndSignatures.hashCode());
                System.out.println("doMethods: " + doMethods);
            }

            if (doMethods.containsKey(event.getFullMethodName())) {
                m = doMethods.get(event.getFullMethodName());
                m.setAccessible(true);
                m.invoke(this, event.getParameters());
                //                updateEventCounts(event);
            } // if
            else { 
                
                if (isVerbose()) {
                    System.out.println(
                    "Master lookup failed, trying namesAndSignatures..." +
                    " Method Name = " + event.getFullMethodName());
                }
                // Now, we are here only because there is some chance that there will be a match.
                // First
                Object[] params = event.getParameters();
                for (Iterator<Class<?>[]> iter = namesAndSignatures.get(methodName).iterator(); iter.hasNext(); ) {
                    if (isDebug()) {
                        System.out.println("namesAndSignatures: " + namesAndSignatures.hashCode());
                    }
                    Class<?>[] signature = iter.next();
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
                                match = match && ( params[i] == null ||  signature[i].isAssignableFrom(params[i].getClass()));
                            }  // else
                            if (isVerbose()) {
                                System.out.println(signature[i].getName() + " ?=? " +
                                params[i].getClass().getName());
                            } // if
                        } // for
                        if (match) {
                            if (debug) {
                                System.out.println("Match found: " + event.getFullMethodName());
                            }
                            String key = getFullMethodName(methodName,  signature, false);
                            m = doMethods.get(key);
                            if (m != null) {
                                m.setAccessible(true);
                                m.invoke(this, params);
                                doMethods.put(event.getFullMethodName(), m);
                            } else { // m is null
                                System.out.println("*** Error in SimEntityBaseProtected.ProcessSimEvent " +
                                "was unable to find " + key + " in the doMethods even though it was found earlier.");
                            }
                        }  else {//no match
                            if (isVerbose()) {
                                System.out.println(" No match found\n");
                            }
                        }//endif match
                    } else { //param not same length
                        if (isVerbose()) {
                            System.out.println("Different number of parameters");
                        }
                    }//endif param lengths.
                }
            }
        }
        catch (NullPointerException e) {
            System.err.println("\n*** In SimEntityBaseProtected.processSimEvent " + e);
            System.err.println("Attempted method: " + event.getFullMethodName());
            //e.printStackTrace();
            throw(new RuntimeException(e));
        }
        catch(IllegalAccessException e) {
            System.err.println("\n*** In SimEntityBaseProtected.processSimEvent " + e);
            System.err.println("Attempted method: " + m );
            System.err.println("  [key = " + event.getFullMethodName() +"]");
            System.err.println("  [name = " + event.getMethodName() + "]");
            System.err.print  ("  [params = (");
            for (int i = 0; i < event.getParameters().length; i++) {
                System.err.print(event.getParameters()[i].getClass().getName());
                if (i < event.getParameters().length - 1) {System.out.print(",");}
            }
            System.err.println(") ]");
            System.err.println("This object: " +
            Integer.toHexString(this.hashCode()) );
            System.err.print("This class: " );
            Class c = this.getClass();
            while(!c.equals(SimEntityBase.class)) {
                System.err.print(c);
                c = c.getSuperclass();
            }
            System.err.println(NL + "Method's object: " +
            Integer.toHexString(event.getSource().hashCode()) );
            System.err.println("Method's class: " + m.getDeclaringClass());
            e.printStackTrace();
            throw(new RuntimeException(e));
        }  //shouldn't happen
        catch(IllegalArgumentException e) {
            throw(new RuntimeException(e));
        }
        catch(InvocationTargetException e) {
            throw(new RuntimeException(e));
        }
        finally {
        }
    }
    
    /**
     * Gets a String representation of this entity's event methods.
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
        Map doMethods = (Map) allDoMethods.get(this.getClass());
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
        System.out.println(this.dumpDoMethodsStr());
    }
    
    /**
     * Produces a String containing the names and signatures of this entity's "do" methods.
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
        Map namesAndSignatures = (Map) allNamesAndSignatures.get(this.getClass());
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
        String str = dumpNamesAndSignaturesStr();
        System.out.println(str);
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
        new Stop().waitDelay("StopSimEntity", endingTime, Priority.LOWEST, this);
    }
    
    
/**
* If true, print debug information
**/
    public static void setDebug(boolean b) {debug = b;}

/**
* If true, print debug information
**/
    public static boolean isDebug() {return debug;}
    
/**
* Gets the method name plus signature as a String.
*  @param m The method for which to get the full name (unfortunately the jdk does
*           not appear to provide this particular String...to my knowledge).
*  @return The full method name of m, including signature, as a String.
**/
    public static String getFullMethodName(Method m) {
        String name = m.toString();
        return m.getName() + getSignatureString(m);
    }
/**
* Gets the method name plus signature as a String.
* @param name The name of the method.
* @param parameters An array of Class objects that are the signature of the method.
* @param wrap If true, primatives will be converted into Objects.
**/
   public static String getFullMethodName(String name, Class[] parameters, boolean wrap) {
        StringBuffer buf = new StringBuffer();
        buf.append(name); 
        buf.append('(');
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] != null) {
                    if (parameters[i].isPrimitive() && wrap) {
                        if (parameters[i].equals(Integer.TYPE)) {
                            buf.append("java.lang.Integer");
                        } else if (parameters[i].equals(Float.TYPE)) {
                            buf.append("java.lang.Float");
                        } else if (parameters[i].equals(Double.TYPE)) {
                            buf.append("java.lang.Double");                            
                        } else if (parameters[i].equals(Long.TYPE)) {
                            buf.append("java.lang.Long");                            
                        } else if (parameters[i].equals(Boolean.TYPE)) {
                            buf.append("java.lang.Boolean");                            
                         } else if (parameters[i].equals(Byte.TYPE)) {
                            buf.append("java.lang.Byte");                           
                         } else if (parameters[i].equals(Short.TYPE)) {
                            buf.append("java.lang.Short");
                         } else if (parameters[i].equals(Character.TYPE)) {
                            buf.append("java.lang.Character");                           
                        } else {
                            System.out.println("*** Error in SimEntityBaseProtected.getFullMethodNameWrapped ");
                            System.out.println("\tAn unknown primitive data type: " + parameters[i].getName());
                        }
                    } else {//else not Primitive
                        buf.append(parameters[i].getName());
                    } // endif Primative
                }
                else { //else paramters[i] null
                    buf.append("null");
                }
                if (i < parameters.length - 1) {buf.append(',');}
            }//for
        }//null
        buf.append(')');
        return buf.toString();
   }
    /**
     *  Gets the signature of the Method as a String, primative arguments are
     *  displayed with the primative name (e.g, int)
     *  @param m The method for which to get the signature as a String (unfortunately
     *  the jdk does not appear to provide this particular String either...to my knowledge).
     *  @return The signature m as a String.
     **/
    public static String getSignatureString(Method m) {
        String name = m.toString();
        return name.substring(name.indexOf('('));
    }
    
    /**
     * Gets a String containing the Method name plus the signature, with any
     * primative parameters optionally wrapped in an Object.
     * @param m Get the name of this Method
     * @param wrap If true wrap primitives in an Object. (e.g., "int" is replaced by
     * "java.lang.Integer"
     */
    public static String getFullMethodName(Method m, boolean wrap) {
        if (wrap) {
            Class[] parameters = m.getParameterTypes();
            String name = m.getName();
            return getFullMethodName(name, parameters, true);
        } 
        return getFullMethodName(m); //un-wrapped
    }

/**
* Determines if a event signature is equivelent to the given arguments.
* @param signature An array of the method's Classes.
* @param args An array containing the method's arguments as Objects.
* @return True if the corresponding signatures are assignable from
* the arguments and are therefore equivelent.
**/    
    public static boolean isAssignableFrom(Class<?>[] signature, Object[] args) {
        boolean assignable = true;
        if (signature.length != args.length) {
            assignable = false;
        }
        else {
            for (int i = 0; i < signature.length; i++) {
                if (!signature[i].isAssignableFrom(args[i].getClass())) {
                    assignable = false;
                    break;
                }
            }
        }
        return assignable;
    }
    
    /**
     * Clears cache of doMethods and namesAndSignatures
     */
    public static void coldReset() {
        allDoMethods.clear();
        allNamesAndSignatures.clear();
    }
    
}
