package simkit.examples;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
/**
 *  Simplest non-trivial Event Graph.  A basic arrival process that can have any
 *  interarrival probability distribution.  
**/
public class ArrivalProcess extends SimEntityBase {
/**
 *  The interarrival distribution (parameter)
**/
    private RandomVariate interArrivalTime;
/**
 *  The number of arrivals (state variable)
**/
    protected int numberArrivals;
/**
 *  Construct an <CODE>ArrivalProcess</CODE> instance with given interarrival
 *  distribution.  The distribution must generate values that are &gt;= 0. This
 *  is the preferred way to construct and ArrivalProcess instance.
 *  @param iat The interarrival distribution RandomVariate
**/
    public ArrivalProcess(RandomVariate iat) {
        this.setInterArrivalTime(iat);
    }
/**
 *  Construct an <CODE>ArrivalProcess</CODE> instance with given interarrival
 *  distribution.
 *  @param distribution The interarrival distribution name (fully-qualified
 *              class name).
 *  @param params The prarmeters of the interarrival distribution.
 *  @param seed The seed of the RandomVariate object.
**/
    public ArrivalProcess(String distribution, Object[] params, long seed) {
        this.setInterArrivalTime(RandomVariateFactory.getInstance(
            distribution, params, seed ));
    }
/**
 *  Resets the number of arrivals to 0
**/
    public void reset() {
        super.reset();
        numberArrivals = 0;
    }
/**
 *  Schedule the first arrival. (Event Method)
**/
    public void doRun() {
        waitDelay("Arrival", interArrivalTime.generate());
    }
/**
 *  Arrival event.  Increments number of arrivals and schedules next arrival.
**/
    public void doArrival() {
        firePropertyChange("numberArrivals", ++numberArrivals);
        waitDelay("Arrival", interArrivalTime.generate());
    }
/**
 *  @return number of arrivals (state variable)
**/
    public int getNumberArrivals() { return numberArrivals; }
/**
 *  @param iat Interarrival distribution (parameter)
**/
    public void setInterArrivalTime(RandomVariate iat) {
        interArrivalTime = RandomVariateFactory.getInstance(iat);
    }
/**
 *  @return Interarrival distribution (parameter)
**/
    public RandomVariate getInterArrivalTime() {
        return RandomVariateFactory.getInstance(interArrivalTime);
    }
/**
 *  @return String suitable for part of report
**/
    public String paramString() {
        return "Arrival Process with interarrival times " + interArrivalTime;
    }

}