package simkit.test;

import simkit.Schedule;
import simkit.examples.EntityArrivalProcess;
import simkit.examples.EntityServer;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.TallyStatsBySource;

/**
 * Test of the TallyStatsBySource class. Instantiate a bunch of EntityServers
 * with the TallyStatsBySource instance listening for the timeInSystem state. It
 * should separate each when output:
 * <pre>
EntityServer.3
	serviceTime = Gamma (1.300, 1.200)
	numberServers = 1
117308 0.000 25.172 2.727 6.251 2.500
EntityServer.2
	serviceTime = Gamma (1.100, 1.200)
	numberServers = 1
117308 0.000 25.699 2.126 4.271 2.067
EntityServer.5
	serviceTime = Gamma (1.700, 1.200)
	numberServers = 1
117308 0.001 48.679 4.434 15.305 3.912
EntityServer.4
	serviceTime = Gamma (1.500, 1.200)
	numberServers = 1
117308 0.002 36.182 3.483 9.642 3.105
EntityServer.6
	serviceTime = Gamma (1.900, 1.200)
	numberServers = 1
117307 0.003 51.051 5.779 25.809 5.080</pre>
 *
 * @author ahbuss
 */
public class TestTallyStatsBySource {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TallyStatsBySource tallyStatsBySource = new TallyStatsBySource("timeInSystem");
        int number = 5;
        RandomVariate rv = RandomVariateFactory.getInstance("Exponential", 3.4);
        EntityArrivalProcess arrivalProcess = new EntityArrivalProcess(rv);

        for (int i = 0; i < number; ++i) {
            RandomVariate serviceTime = RandomVariateFactory.getInstance("Gamma", 1.1 + 0.2 * i, 1.2);
            EntityServer entityServer = new EntityServer(1, serviceTime);
            arrivalProcess.addSimEventListener(entityServer);
            entityServer.addPropertyChangeListener(tallyStatsBySource);
        }

        double stopTime = 400000.0;
        Schedule.setVerbose(false);
        Schedule.stopAtTime(stopTime);
        Schedule.reset();
        Schedule.startSimulation();

        for (Object source : tallyStatsBySource.getAllStats().keySet()) {
            System.out.println(source);
            System.out.printf("%s%n", tallyStatsBySource.getStatsFor(source).getDataLine());
        }
    }

}
