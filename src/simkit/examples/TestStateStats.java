package simkit.examples;
import java.text.DecimalFormat;

import simkit.Schedule;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.PercentageInStateStat;
import simkit.stat.SimpleStatsTally;
import simkit.stat.SimpleStatsTimeVarying;
/**
 * Simulates a system consisting of a 2 server single queue system for Customers. Uses an 
 * {@link ArrivalProcess}, a {@link EntityCreator}, and a {@link EntityServer}.
 * @author  Arnold Buss
 * @version $Id: TestStateStats.java 1049 2008-02-08 16:44:09Z ahbuss $
 */
public class TestStateStats {
    
    public static void main(String[] args) {
        RandomVariate[] rv = new RandomVariate[2];
        rv[0] = RandomVariateFactory.getInstance("Exponential", 1.7);
        rv[1] = RandomVariateFactory.getInstance("Gamma", 2.5, 1.2);
        int numberServers = 2;
        
        ArrivalProcess arrival = new ArrivalProcess(rv[0]);
        EntityCreator creator = new EntityCreator();
        EntityServer server = new EntityServer(numberServers, rv[1]);
        arrival.addSimEventListener(creator);
        creator.addSimEventListener(server);
        
        PercentageInStateStat percentageInStateStat = new PercentageInStateStat("numberAvailableServers", numberServers);
        server.addPropertyChangeListener(percentageInStateStat);

        SimpleStatsTimeVarying niqStat = new SimpleStatsTimeVarying("numberInQueue");
        SimpleStatsTimeVarying nasStat = new SimpleStatsTimeVarying("numberAvailableServers");
        SimpleStatsTally diqStat = new SimpleStatsTally("delayInQueue");
        SimpleStatsTally tisStat = new SimpleStatsTally("timeInSystem");

        int eventListID = 0;
        arrival.setEventListID(eventListID);
        creator.setEventListID(eventListID);
        server.setEventListID(eventListID);
        
        niqStat.setEventListID(eventListID);
        nasStat.setEventListID(eventListID);
        
        server.addPropertyChangeListener(niqStat);
        server.addPropertyChangeListener(nasStat);
        server.addPropertyChangeListener(diqStat);
        server.addPropertyChangeListener(tisStat);
        
//        server.addPropertyChangeListener(new SimplePropertyDumper());
        
        System.out.println(arrival);
        System.out.println(server);
        
        Schedule.getEventList(eventListID).stopAtTime(1000.0);
        
        Schedule.getEventList(eventListID).setVerbose(false);
        
        Schedule.getEventList(eventListID).reset();
        Schedule.getEventList(eventListID).startSimulation();
        
        DecimalFormat form = new DecimalFormat("0.000");

        System.out.println();
        System.out.println("Simulation ended at\t" + form.format(Schedule.getEventList(eventListID).getSimTime()));
        System.out.println("Number arrivals: \t" + arrival.getNumberArrivals());
        System.out.println("Number delays:   \t" + diqStat.getCount());
        System.out.println("Number served:   \t" + tisStat.getCount());
        
        System.out.println("Avg # in queue: \t" + form.format(niqStat.getMean()));
        System.out.println("Avg delay in queue: \t" + form.format(diqStat.getMean()));
        System.out.println("Avg time in system: \t" + form.format(tisStat.getMean()));
        
        System.out.println("Avg Utilization:\t" + form.format(1.0 - 
            nasStat.getMean() / server.getNumberServers()));
        
        System.out.println(percentageInStateStat.stateString());
        
    }
    
}
