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
        firePropertyChange("numberArrivals", numberArrivals);
        waitDelay("Arrival", interArrivalTime.generate());
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
    public String paramString() { return toString(); }
    /**
     *  @return String suitable for part of report
     **/
    public String toString() {
        return "Arrival Process\n\tInterarrival times: " + interArrivalTime;
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
        
        RandomVariate interarrivalTime = 
            RandomVariateFactory.getInstance("Exponential",
                new Object[] {meanIAT}, simkit.random.CongruentialSeeds.SEED[0]);
        ArrivalProcess arrivals =
            new ArrivalProcess(interarrivalTime);
        
        System.out.println();
        System.out.println(arrivals);
        System.out.println("Simulation will end at time " + stopTime);
        System.out.println();
        
        simkit.Schedule.setVerbose(true);
        simkit.Schedule.setSingleStep(singleStep);
        
        simkit.Schedule.stopAtTime(stopTime);
            
        simkit.Schedule.reset();
        simkit.Schedule.startSimulation();
            
        System.out.println("At time " + simkit.Schedule.getSimTime() + " there have been " +
            arrivals.getNumberArrivals() + " arrivals");
    }
}