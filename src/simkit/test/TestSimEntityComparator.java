/*
 * TestSimEntityComparator.java
 *
 * Created on March 11, 2002, 9:17 PM
 */

package simkit.test;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import simkit.Schedule;
import simkit.SimEntity;
import simkit.SimEntityBase;
import simkit.SimEntityComparator;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
/**
 *
 * @author  ahbuss
 * @version $Id$
 */
public class TestSimEntityComparator extends SimEntityBase {

    protected static RandomVariate priorityGen ;
    
    static {
        priorityGen = RandomVariateFactory.getInstance("simkit.random.DiscreteUniformVariate",
            new Integer[] { new Integer(-1), new Integer(1) }, 12345L);
   }
    /** Creates new TestSimEntityComparator */
    public TestSimEntityComparator() {
        this.setPriority(priorityGen.generate());
     }

    public void reset() {
        System.out.println("Reset: " + this);
    }
    
    public String toString() { 
        return "Priority = " + getPriority() + 
            " Serial = " + getSerial(); 
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        SortedSet sortedSet = new TreeSet(new SimEntityComparator());
        SimEntity[] entity = new SimEntity[10];
        for (int i = 0; i < entity.length; i++) {
            entity[i] = new TestSimEntityComparator();
            sortedSet.add(entity[i]);
        }
        for (Iterator i = sortedSet.iterator(); i.hasNext(); ) {
            System.out.println(i.next());
        }
        
        Schedule.reset();
    }

}
