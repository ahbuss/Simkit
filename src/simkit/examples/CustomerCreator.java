package simkit.examples;
import simkit.SimEntityBase;
/**
 * An adapter class that listens for an Arrival event and schedules an
 * Arrival event with a new Customer as its parameter.
 * @author  Arnold Buss
 */
public class CustomerCreator extends SimEntityBase {
    
    public void doArrival() {
        waitDelay("Arrival", 0.0, new Customer());
    }
    
}
