package simkit;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import simkit.Named;
import simkit.Schedule;

/**
 * A generic entity that can be used as a Customer, a Job, etc. It can be subclassed
 * to add features or properties, or additional properties can be added dynamically.
 *
 * @version $Id$
 * @author ahbuss
 */
public class Entity implements Named {
    
    public static final DecimalFormat FORM = new DecimalFormat("0.0000");
    
    public static final String DEFAULT_NAME = "Entity";
    
    private static int NEXT_ID = 0;
    
    private int id;
    
    private double creationTime;
    
    private double timeStamp;
    
    private LinkedHashMap<String, Object> properties;
    
    private String name;
    
    /**
     * Instantiate an Entity with the given name.
     * @param name The name of this Entity
     */
    public Entity(String name) {
        setName(name);
        creationTime = Schedule.getSimTime();
        stampTime();
        id = ++NEXT_ID;
    }
    
    /**
     * Instantiate an Entity with the default name.
     */
    public Entity() {
        this(DEFAULT_NAME);
    }
    
    /**
     * Set timeStamp to the current simTime
     */
    public void stampTime() {
        timeStamp = Schedule.getSimTime();
    }

    /**
     * @return the simTime since this Entity was instantiated
     */    
    public double getAge() {
        return Schedule.getSimTime() - getCreationTime();
    }
    
    /**
     * @deprecated Use getElapsedTime() instead
     * @return the time since stampTime() was last invoked.
     */
    public double getElapedTime() {
        return Schedule.getSimTime() - getTimeStamp();
    }
    
    /**
     * @return the time since stampTime() was last invoked.
     */
    public double getElapsedTime() {
        return Schedule.getSimTime() - getTimeStamp();
    }
    
    /**
     * Add a new property with the given name and value.  If a property
     * already exists, then it is clobbered.
     * @param name the name of the property
     * @param value the value of the property
     */
    public void setProperty(String name, Object value) {
        if (properties == null) {
            properties = new LinkedHashMap<String, Object>();
        }
        properties.put(name, value);
    }
    
    /**
     * @param name the name of the desired property
     * @return the value of the property, or null if no property of that name
     *          has been set
     */
    public Object getProperty(String name) {
        return properties != null ? properties.get(name) : null;
    }
    
    /**
     * @return the simTime this Entity was instantiated
     */
    public double getCreationTime() {
        return creationTime;
    }

    /**
     * @return the last simTime when stampTime() was invoked
     */
    public double getTimeStamp() {
        return timeStamp;
    }

    /**
     * @return shallow copy of all properties
     */
    public LinkedHashMap<String, Object> getProperties() {
        return properties != null ? new LinkedHashMap<String, Object>(properties) :
            new LinkedHashMap<String, Object>();
    } 

    /**
     * @return the name of this Entity
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name of this Entity
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the unique ID of this Enrity
     */
    public int getID() {
        return id;
    }

    /**
     * @return Name.id [<creationTime>, <timeStamp>]
     */
    public String toString() {
        return getName() + '.' + getID() + ' ' + '[' + FORM.format(getCreationTime()) + ',' +
                FORM.format(getTimeStamp()) + ']';
    }
    
    /**
     * @return String of all added properties
     */
    public String paramString() {
        StringBuffer buf = new StringBuffer(toString());
        if (properties != null) {
            for (String property : properties.keySet()) {
                buf.append('\n');
                buf.append('\t');
                buf.append(property);
                buf.append(' ');
                buf.append('=');
                buf.append(' ');
                Object value = properties.get(property);
                if (value != null && value.getClass().isArray()) {
                    StringBuffer buf1 = new StringBuffer();
                    buf1.append('[');
                    for (int i = 0; i < Array.getLength(value); ++i) {
                        buf1.append(Array.get(value, i));
                        if (i < Array.getLength(value) - 1) {
                            buf1.append(',');
                            buf1.append(' ');
                        }
                    }
                    buf1.append(']');
                    value = buf1.toString();
                }
                buf.append(value);
            }
        }
        return buf.toString();
    }
}