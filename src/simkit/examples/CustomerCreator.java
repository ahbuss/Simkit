package simkit.examples;
import simkit.SimEntityBase;
/**
 *
 * @author  Arnold Buss
 */
public class CustomerCreator extends SimEntityBase {
    
    public void doArrival() {
        waitDelay("Arrival", 0.0, new Customer());
    }
    
}
