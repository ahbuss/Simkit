package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import simkit.Named;

/**
 * <p>Listens for boolean PropertyChangeEvent and counts number of true
 * and number of false values.  It can be used to calculate the proportion
 * of true (or false) properties as a proportion of the total number, 
 * or to calculate the number true/number false or number false/number true.
 * <p>Like SimpleStats, this listens for a single property name.
 *
 * @version $Id$
 * @author  ahbuss
 */
public class BooleanCounter implements PropertyChangeListener, Named {
    
    private String propertyName;
    
    protected int numberTrue;
    protected int numberFalse;
    
    /**
     * Instantiate a BooleanCounter listening to given property.
     * @param propName Name of property to listen for
     */    
    public BooleanCounter(String propName) {
        this.setName(propName);
    }
    
    /**
     * Copy constructor
     * @param counter BooleanCounter we are copying
     */    
    public BooleanCounter(BooleanCounter counter) {
        this(counter.getName());
        numberTrue = counter.getNumberTrue();
        numberFalse = counter.getNumberFalse();
    }
    
    /**
     * Clear counters
     */    
    public void reset() {
        numberTrue = 0;
        numberFalse = 0;
    }
    
    /**
     * If propertyName is same as one we are listening to and the type
     * is a Boolean, update the appropriate counter.
     * @param e Heard Event
     */    
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (propertyName.equals(e.getPropertyName())) {
            Object value = e.getNewValue();
            if (value instanceof Boolean) {
                if ( ((Boolean) value) ) {
                    numberTrue += 1;
                }
                else {
                    numberFalse += 1;
                }
            }
        }
    }
    
    /**
     * @return Number of true events heard
     */    
    public int getNumberTrue() { return numberTrue; }
    
    /**
     * @return number of false events heard
     */    
    public int getNumberFalse() { return numberFalse; }
    
    /**
     * @return Percent of true events of total number heard
     */    
    public double getPercentTrue() { return (double) numberTrue / (double) (numberTrue + numberFalse); }
    
    /**
     *
     * @return Percent of false events of total number heard
     */    
    public double getPercentFalse() { return (double) numberFalse / (double) (numberTrue + numberFalse); }
    
    /**
     * @return Number true / number false
     */    
    public double getTrueOverFalse() { return (double) numberTrue / (double) numberFalse; }
    
    /**
     * @return Number false / number true
     */    
    public double getFalseOverTrue() { return (double) numberFalse / (double) numberTrue; }
    
    @Override
    public String getName() { return propertyName; }
    
    @Override
    public void setName(String name) {
        propertyName = name;
        reset();
    }
    
    /**
     * @return String containing values of the two counters (# true # false)
     */    
    public String stateString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getNumberTrue());
        buf.append(' ');
        buf.append(getNumberFalse());
        return buf.toString();
    }
    
    /**
     * @return Name + stateString
     */    
    @Override
    public String toString() { return 
        "BooleanCounter [" + getName() + "] " + stateString();
    }
}
