/*
 * SimEntityComparator.java
 *
 * Created on March 11, 2002, 9:02 PM
 */

package simkit;
import java.util.Comparator;
/**
 *
 * @author  ahbuss
 * @version 
 */
public class SimEntityComparator implements Comparator {

    /** Creates new SimEntityComparator */
    public SimEntityComparator() {
    }

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
