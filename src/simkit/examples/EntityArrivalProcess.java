package simkit.examples;

import simkit.Entity;
import simkit.random.RandomVariate;

/**
 * Created Entities and passes them as arguments t Arrival events
 *
 * @author ahbuss
 */
public class EntityArrivalProcess extends ArrivalProcess {

    public EntityArrivalProcess() {
    }

    /**
     *
     * @param interarrivalTimeGenerator Given generator for interarrival times
     */
    public EntityArrivalProcess(RandomVariate interarrivalTimeGenerator) {
        super(interarrivalTimeGenerator);
    }

    /**
     * Note call to super.doArrival() - normally event methods are not directly
     * called. In this case, we want everything in the super class' Arrival
     * event.
     */
    @Override
    public void doArrival() {
        super.doArrival();
        waitDelay("Arrival", 0.0, new Entity());
    }

}
