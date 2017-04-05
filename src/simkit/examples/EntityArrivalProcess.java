package simkit.examples;

import simkit.Entity;
import simkit.random.RandomVariate;

/**
 *
 * @author ahbuss
 */
public class EntityArrivalProcess extends ArrivalProcess {

    public EntityArrivalProcess() { }
    
    public EntityArrivalProcess(RandomVariate interarrivalTimeGenerator) {
        super(interarrivalTimeGenerator);
    }
    
    @Override
    public void doArrival() {
        super.doArrival();
        waitDelay("Arrival", 0.0, new Entity());
    }
    
}
