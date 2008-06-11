package simkit.test;

import simkit.Priority;

/**
 * @version $Id: MorePriority.java 1000 2007-02-15 19:43:11Z ahbuss $
 * @author ahbuss
 */
public class MorePriority extends Priority {
    
    public static final Priority HUMONGOUS = new Priority("Humongous", 100.0);
    public static final Priority MINISCULE = new Priority("Miniscule", -100.0);
    
    public MorePriority(String name, double value) {
        super(name, value);
    }    
}