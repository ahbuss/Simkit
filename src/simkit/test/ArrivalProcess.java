package simkit.test;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 *  <p>Simplest non-trivial Event Graph model, the ArrivalProcess consists basically
 *  of a single event that reccurs with the time between arrivals a positive,
 *  but otherwise arbitrary random variable.
 *  <p> Note about comments: The main decsription (this section) should be put
 *  <em>after</em> the <code>import</code> statements but <em>before</em> the
 *  class declaration.  Setters and getters usually only need the <code>@return</code>
 *  and <code>@param</code> tags.  Identify variables and setter/getters as
 *  state variables or parameters.  Identify "do" methods as Event methods.
 *
 *  @author Arnold Buss
**/
public class ArrivalProcess extends SimEntityBase {
/**
 *  Number of arrivals since object was reset.  (State Variable)
**/
    protected int numberArrivals;
/**
 *  Interarrival time generator. (Parameter)
**/
    private RandomVariate interArrivalTime;
/**
 *  Construct an ArrivalProcess with the given distribution, parameters, and
 *  starting seed.
 *  @param name The fully-qualified name of the RandomVariate class for interarrival
 *              times (e.g. "simkit.random.ExponentialVariate")
 *  @param params The parameters for the RandomVariate objectto be used for
 *              interarrival times.
 *  @param seed The seed for the RandomVariate object used for interarrival times.
**/
    public ArrivalProcess(String name, Object[] params, long seed) {
        interArrivalTime = RandomVariateFactory.getInstance(name, params, seed);
    }
/**
 *  Construct an ArrivalProcess with the given RandomVariate as the interarrival
 *  times.  A copy is obtained from the RandomVariateFactory to preserve encapsulation.
 *  @param rv The RandomVariate instance to be copied and used for interarrival times.
**/
    public ArrivalProcess(RandomVariate rv) {
        setInterarrivalTime(rv);
    }
/**
 *  Reset the state variables of the model: numberArrivals is initialized to 0. 
**/
    public void reset() {
        super.reset();
        numberArrivals = 0;
    }
/**
 *  At start of simulation, schedule an Arrival event with an interarrival time
 *  delay.  (Event Method)
**/
    public void doRun() {
        waitDelay("Arrival", interArrivalTime.generate());
    }
/**
 *  Arrival of a customer: Increment numberArrivals, then schedule another Arrival
 *  event after a delay of an interarrival time.
**/
    public void doArrival() {
        firePropertyChange("numberArrivals", numberArrivals, ++numberArrivals);
        double iat = interArrivalTime.generate();
        if (!Double.isNaN(iat)) {
            waitDelay("Arrival", iat);
        }
    }
/**
 *  @return Copy of Interarrival time RandomVariate.  (Parameter)
**/
    public RandomVariate getInterarrivalTime() {
        return RandomVariateFactory.getInstance(interArrivalTime);
    }
/**
 *  @param iat RandomVariate instance to be copied for interarrival times. (Parameter)
**/
    public void setInterarrivalTime(RandomVariate iat) {
        interArrivalTime = iat;
    }
/**
 *  @return Number of Arrivals since last reset(). (State Variable).
**/
    public int getNumberArrivals() { return numberArrivals; }
/**
 *  @return Short description of this instance.
**/
    public String paramString() {
        return "Arrival Process with " + interArrivalTime + " interarrival times";
    }
}
