/*
 * SimEntityComparator.java
 *
 * Created on March 11, 2002, 9:02 PM
 */

package simkit;
import java.util.Comparator;
/**
 * Determines the relative priority between two SimEntities.<P>
 * The order of comparison is:
 * <OL>
 * <LI>SimEntity priority. (Larger number is higher priority.)</LI>
 * <LI>SimEntity serial number. (Smaller serial number is higher priority.)</LI>
 * </OL>
 *
 * @author  ahbuss
 */
public class SimEntityComparator implements Comparator {

/** 
* Creates new SimEntityComparator 
*/
    public SimEntityComparator() {
    }

/**
* Compares the priority of the first SimEntity to the second.
* @return -1 if first is higher priority, 0 if equal priority, 1 if first is lower priority.
* @throws ClassCastException if either Object is not a SimEntity.
**/
    public int compare(java.lang.Object first, java.lang.Object second) {
        if (first.equals(second) || second.equals(first)) { return 0; }
        SimEntity firstSimEntity = (SimEntity) first;
        SimEntity secondSimEntity = (SimEntity) second;
        
        if (firstSimEntity.getPriority() < secondSimEntity.getPriority()) { return 1; }
        if (firstSimEntity.getPriority() > secondSimEntity.getPriority()) { return -1; }
        if (firstSimEntity.getSerial() < secondSimEntity.getSerial()) { return -1; }
        if (firstSimEntity.getSerial() > secondSimEntity.getSerial()) { return 1;} 
               
        return 0;
    }    
    
}
