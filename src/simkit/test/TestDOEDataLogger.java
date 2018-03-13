/*
 * TestSimpleServer.java
 *
 * Created on January 24, 2002, 12:21 AM
 */
package simkit.test;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import simkit.Schedule;
import simkit.examples.ArrivalProcess;
import simkit.examples.SimpleServer;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTimeVarying;
import simkit.util.DOEDataLogger;

/**
 * Different numbers now that MersenneTwister is new default RandomNumber.
 * Adjusted for JDK 1.5 style RandomVariate instancing. output:
 * <pre>
 * </pre>
 *
 * @author Arnold RunSimpleServers
 * @version $Id$
 */
public class TestDOEDataLogger {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException {
        String rvName = "Exponential";
        double mean = 1.7;
        RandomVariate rv = RandomVariateFactory.getInstance(
                rvName, mean);
        ArrivalProcess arrival = new ArrivalProcess(rv);
        
        rvName = "Gamma";
        double alpha = 2.5;
        double beta = 1.2;
        
        rv = RandomVariateFactory.getInstance(rvName, alpha, beta);
        int numServ = 2;
        SimpleServer server = new SimpleServer(numServ, rv);
        arrival.addSimEventListener(server);

//        SimplePropertyDumper dump = new SimplePropertyDumper();
//        server.addPropertyChangeListener(dump);
        SimpleStatsTimeVarying niqStat = new SimpleStatsTimeVarying("numberInQueue");
        SimpleStatsTimeVarying nasStat = new SimpleStatsTimeVarying("numberAvailableServers");
        
        server.addPropertyChangeListener(niqStat);
        server.addPropertyChangeListener(nasStat);
        
        System.out.println(arrival);
        System.out.println(server);

//        Schedule.setVerbose(true);
//        Schedule.setSingleStep(false);
//        double stopTime = 2.0;
        double stopTime = 100.0;
        
        File outputFile = new File(System.getProperty("user.home"), "output.csv");
        DOEDataLogger doeDataLogger = new DOEDataLogger(
                "numberInQueue", outputFile,
                new String[]{"meanInterarrivalTime", "meanServiceTime",
                    "serviceTimeVariance"},
                mean, alpha * beta, alpha * beta * beta);
//        server.addPropertyChangeListener("numberInQueue", doeDataLogger);
        
        for (double factor = 0; factor < .3; factor += .1) {
            double theMean = mean + factor;
            doeDataLogger.setFactors(theMean, alpha * beta, alpha * beta * beta);
            int numberReplications = 10;
            for (int replication = 1; replication <= numberReplications; ++replication) {
                
                Schedule.stopAtTime(stopTime);
                Schedule.reset();
                niqStat.reset();
                Schedule.startSimulation();
                doeDataLogger.propertyChange(new PropertyChangeEvent(
                        server, "numberInQueue", null, niqStat.getMean()));
            }
        }
        
        doeDataLogger.close();
        System.out.println(outputFile.getAbsoluteFile() + " written");
        if (true) {
            return;
        }
        DecimalFormat df = new DecimalFormat("0.0000");
        
        System.out.println("\nSimulation ended at time " + Schedule.getSimTime());
        System.out.println("\nThere have been " + arrival.getNumberArrivals()
                + " arrivals");
        System.out.println("There have been " + server.getNumberServed()
                + " customers served");
        System.out.println("Average number in queue\t" + df.format(niqStat.getMean()));
        System.out.println("Average utilization\t"
                + df.format(1.0 - nasStat.getMean() / server.getTotalNumberServers()));
    }
} /* output:
oa3302.ArrivalProcess.1
        interarrivalTime = Exponential (1.7)
oa3302.Server.2
        numberServers = 2
        serviceTime = Gamma (2.5, 1.2)

Simulation ended at time 10000.0

There have been 5804 arrivals
There have been 5803 customers served
Average number in queue 4.5251
Average utilization     0.8716
   */
