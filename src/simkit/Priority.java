package simkit;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @version $Id$
 * @author ahbuss
 */
public class Priority implements Comparable<Priority> {
    
    public static final Priority LOWEST = new Priority("LOWEST", -Double.MAX_VALUE);
    public static final Priority LOWER = new Priority("LOWER", -20.0);
    public static final Priority LOW = new Priority("LOW", -10.0);
    public static final Priority DEFAULT = new Priority("DEFAULT", 0.0);
    public static final Priority HIGH = new Priority("HIGH", 10.0);
    public static final Priority HIGHER = new Priority("HIGHER", 20.0);
    public static final Priority HIGHEST = new Priority("HIGHEST", Double.MAX_VALUE);
    
    
    protected static NumberFormat form = new DecimalFormat("0.0000");
    
    private double priority;
    
    private String name;

    public Priority(String name, double priority) {
        this.name = name;
        this.priority = priority;
    }

    public double getPriority() {
        return priority;
    }
    
    public String toString() {
        return "Priority " + name + "[" + form.format(getPriority()) +"]";
    }
    
    public int compareTo(Priority other) {
        if (this.getPriority() > other.getPriority()) { return -1; }
        if (this.getPriority() < other.getPriority()) { return 1; }
        return 0;
    }    
}