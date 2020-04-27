package simkit.test;

import simkit.Priority;

/**
 * 
 * @author ahbuss
 */
public class MorePriority extends Priority {
    
    public static final Priority HUMONGOUS = new Priority("Humongous", 100.0);
    public static final Priority MINISCULE = new Priority("Miniscule", -100.0);
    
    public MorePriority(String name, double value) {
        super(name, value);
    }    
}