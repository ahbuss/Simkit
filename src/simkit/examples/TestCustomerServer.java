package simkit.examples;
import simkit.*;
import simkit.random.*;
import simkit.stat.*;
import simkit.util.*;
import java.text.*;
/**
 *
 * @author  Arnold Buss
 */
public class TestCustomerServer {
    
    public static void main(String[] args) {
        RandomVariate[] rv = new RandomVariate[2];
        rv[0] = RandomVariateFactory.getInstance("Exponential", 
            new Object[] { new Double(1.7) }, 12345L);
        rv[1] = RandomVariateFactory.getInstance("Gamma", 
            new Object[] { new Double(2.5), new Double(1.2) }, 54321L);
        int numberServers = 2;
        
        ArrivalProcess arrival = new ArrivalProcess(rv[0]);
        CustomerCreator creator = new CustomerCreator();
        CustomerServer server = new CustomerServer(numberServers, rv[1]);
        arrival.addSimEventListener(creator);
        creator.addSimEventListener(server);
        
        SimpleStatsTimeVarying niqStat = new SimpleStatsTimeVarying("numberInQueue");
        SimpleStatsTimeVarying nasStat = new SimpleStatsTimeVarying("numberAvailableServers");
        SimpleStatsTally diqStat = new SimpleStatsTally("delayInQueue");
        SimpleStatsTally tisStat = new SimpleStatsTally("timeInSystem");
        
        server.addPropertyChangeListener(niqStat);
        server.addPropertyChangeListener(nasStat);
        server.addPropertyChangeListener(diqStat);
        server.addPropertyChangeListener(tisStat);
        
//        server.addPropertyChangeListener(new SimplePropertyDumper());
        
        System.out.println(arrival);
        System.out.println(server);
        
        Schedule.stopAtTime(1000.0);
        
        Schedule.setVerbose(false);
        
        Schedule.reset();
        Schedule.startSimulation();
        
        DecimalFormat form = new DecimalFormat("0.000");

        System.out.println();
        System.out.println("Simulation ended at\t" + form.format(Schedule.getSimTime()));
        System.out.println("Number arrivals: \t" + arrival.getNumberArrivals());
        System.out.println("Number delays:   \t" + diqStat.getCount());
        System.out.println("Number served:   \t" + tisStat.getCount());
        
        System.out.println("Avg # in queue: \t" + form.format(niqStat.getMean()));
        System.out.println("Avg delay in queue: \t" + form.format(diqStat.getMean()));
        System.out.println("Avg time in system: \t" + form.format(tisStat.getMean()));
        
        System.out.println("Avg Utilization:\t" + form.format(1.0 - 
            nasStat.getMean() / server.getNumberServers()));
        
    }
    
}
