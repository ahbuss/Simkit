package simkit.examples;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
/**
 *  <P>Simplest non-trivial Event Graph.  A basic arrival process that can have any
 *  interarrival probability distribution.
 *  @author Arnold Buss
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
        waitDelay("StopArrivals", 20.0);
    }
    /**
     *  Arrival event.  Increments number of arrivals and schedules next arrival. (Event Mathod)
     **/
    public void doArrival() {
        firePropertyChange("numberArrivals", numberArrivals, ++numberArrivals);
        waitDelay("Arrival", interArrivalTime.generate());
    }
    
    public void doStopArrivals() {
        interrupt("Arrival");
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
    /**
     *  @return Usage String (uses Exponential interarrival times)
     **/
    public static String usage() {
        return "Usage: java simkit.examples.ArrivalProcess <mean Interarrival Time> " +
        " <stop time> [single-step (true|false)]";
    }
    /**
     *  @return A short description of this class
     **/
    public static String description() {
        return "Models an arrival process.  The interarrival time distribution is " +
        "passed to the constructor via a RandomVariate object.";
    }
    /**
     *  <P>Test ArrivalProcess for simple, verbose scenario.  Interarrival times are Exponential.
     *  The user specifies the mean interarrival time and the stopping time on the command line.
     *  The user can optionally specify whether it is to be run in single-step mode
     *  or just verbose mode.
     *
     *  <P> Example:
     *  <PRE>
java simkit.examples.ArrivalProcess 40.0 100.0
 
Arrival Process with interarrival times Exponential (40.0)
Simulation will end at time 100.0
 
     ** Event List -- Starting Simulation **
0.000   Run
100.000 Stop
     ** End  of Event List -- Starting Simulation **
 
Time: 0.000     Current Event: Run      [1]
     ** Event List --  **
3.139   Arrival
100.000 Stop
     ** End  of Event List --  **
 
Time: 3.139     Current Event: Arrival  [1]
     ** Event List --  **
72.111  Arrival
100.000 Stop
     ** End  of Event List --  **
 
Time: 72.111    Current Event: Arrival  [2]
     ** Event List --  **
100.000 Stop
128.740 Arrival
     ** End  of Event List --  **
 
Time: 100.000   Current Event: Stop     [1]
     ** Event List --  **
               << empty >>
     ** End  of Event List --  **
 
At time 100.0 there have been 2 arrivals
     *  </PRE>
     **/
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println(usage());
            return;
        }
        Double meanIAT = Double.valueOf(args[0]);
        double stopTime = Double.parseDouble(args[1]);
        boolean singleStep = (args.length == 2) ? false : Boolean.valueOf(args[2]).booleanValue();
        
        ArrivalProcess arrivals =
        new ArrivalProcess(
        RandomVariateFactory.getInstance("simkit.random.ExponentialVariate",
        new Object[] {meanIAT}, simkit.random.CongruentialSeeds.SEED[0]) );
        
        System.out.println();
        System.out.println(arrivals.paramString());
        System.out.println("Simulation will end at time " + stopTime);
        System.out.println();
        
        simkit.Schedule.setVerbose(true);
        simkit.Schedule.setSingleStep(singleStep);
        
//        simkit.Schedule.stopAtTime(stopTime);
//        simkit.Schedule.stopOnEvent("Arrival", 10);
        for (int i = 0; i < 2; i++) {
            
            if (i > 0) {
//        simkit.SimEventFactory.setVerbose(true);
//                simkit.Schedule.setReallyVerbose(true);
            }
            
            simkit.Schedule.reset();
            simkit.Schedule.startSimulation();
            
            System.out.println("At time " + simkit.Schedule.getSimTime() + " there have been " +
            arrivals.getNumberArrivals() + " arrivals");
        }
        
    }
}