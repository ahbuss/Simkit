package simkit.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import simkit.Entity;
import simkit.Schedule;
import simkit.examples.EntityArrivalProcess;
import simkit.examples.EntityServer;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.util.SimplePropertyDumper;
import simkit.util.WarmupListener;

/**
 *
 * This runs EntityServer for a fixed number of service completions and
 * replications, writing the average for each customer to a file to plot for
 * determining truncation point.
 *
 * @author ahbuss
 */
public class TestEntityServerWarmup {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        Let's use some of the random variates we created
        RandomVariate interarrivalTimeGenerator = RandomVariateFactory.getInstance(
                "Exponential", 2.2);
        EntityArrivalProcess entityArrivalProcess = new EntityArrivalProcess(interarrivalTimeGenerator);

        RandomVariate serviceTimeGenerator = RandomVariateFactory.getInstance("Triangle", 3.3, 5.2, 4.4);

        int totalNumberServers = 2;
        EntityServer entityServer = new EntityServer(totalNumberServers, serviceTimeGenerator);

        entityArrivalProcess.addSimEventListener(entityServer);

//        This is for debugging - uncomment to view state changes for the
//        EntityServer instance.
        SimplePropertyDumper simplePropertyDumper = new SimplePropertyDumper();
//        entityServer.addPropertyChangeListener(simplePropertyDumper);

        WarmupListener warmupListener = new WarmupListener("timeInSystem");
        entityServer.addPropertyChangeListener("timeInSystem", warmupListener);

        System.out.println(entityArrivalProcess);
        System.out.println(entityServer);

        int numberEndServices = 15000;

        int numberReplications = 500;

        Schedule.stopOnEvent(numberEndServices, "EndService", Entity.class);

        Schedule.setVerbose(false);

        System.out.printf("%nSimulation will be run for %,d replications "
                + "of %,d service completions each%n%n", numberReplications, numberEndServices);

        String home = System.getProperty("user.home");
        File outputFile = new File(home, "timeInSystem.txt");
        System.out.println("Output to be written to " + outputFile.getAbsolutePath());

//        Wrap the execution calls in a for loop
        for (int replication = 1; replication <= numberReplications; ++replication) {
            Schedule.reset();
//            Run replication
            Schedule.startSimulation();
        }

        try {
            PrintWriter out = new PrintWriter(outputFile);
            double[] averages = warmupListener.getAverages();
            for (double average : averages) {
                out.println(average);
            }
            out.close();
            System.out.printf("%s written%n", outputFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestEntityServerWarmup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
