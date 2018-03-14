package simkit.test;

import simkit.BasicAssembly;
import simkit.SimEntity;
import simkit.examples.ArrivalProcess;
import simkit.examples.SimpleServer;
import simkit.random.RandomVariateFactory;
import simkit.stat.SampleStatistics;
import simkit.stat.SimpleStatsTimeVarying;

/**
 * Prototypical subclass of BasicAssembly. This is the basic queueing model with
 * an ArrivalProcess and a SimpleServer instance. Statistics on numberInQueue
 * and numberAvailableServers are kept as replicationStats. The designPointStats
 * just use the default in BasicAssembly.
 *
 * @version $Id$
 * @author ahbuss
 */
public class SimpleServerAssembly extends BasicAssembly {

    /**
     * <p>
     * Instantiate SimEntities and Stats objects in arrays (defined in
     * BasicAssembly superclass). Perform hookups - this must be done here
     * because in the superclass constructor it would happen before the objects
     * were instantiated.
     *
     * <p>
     * In this form, all the parameters for the Assembly object are hard wired
     * in the constructor. This is less flexible for reuse, but makes it easier
     * to instantiate one and run it.
     */
    public SimpleServerAssembly() {
    }

    protected void createSimEntities() {
        simEntity = new SimEntity[]{
            new ArrivalProcess(
            RandomVariateFactory.getInstance(
            "Exponential", new Object[]{new Double(1.7)})
            ),
            new SimpleServer(
            2,
            RandomVariateFactory.getInstance(
            "Gamma", new Object[]{new Double(2.5), new Double(1.2)}))
        };
        replicationStats = new SampleStatistics[]{
            new SimpleStatsTimeVarying("numberInQueue"),
            new SimpleStatsTimeVarying("numberAvailableServers")
        };
    }

    /**
     * All SimEventListening is connected here. The identities of simEntity[0]
     * and simEntity[1] are from their definition in the constructor.
     */
    public void hookupSimEventListeners() {
        simEntity[0].addSimEventListener(simEntity[1]);
    }

    /**
     * Hook up the replication stats objects - these will be computing means for
     * each run, and those means will be the inputs to the designPoint stats
     * objects.
     */
    public void hookupReplicationListeners() {
        for (int i = 0; i < replicationStats.length; ++i) {
            simEntity[1].addPropertyChangeListener(replicationStats[i].getName(), replicationStats[i]);
        }
    }

    /**
     * Note: Normally most of this would not be done, it is just for testing and
     * illustrative purposes.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SimpleServerAssembly serverAssembly = new SimpleServerAssembly();
        serverAssembly.setStopTime(10000.0);
        serverAssembly.setNumberReplications(10);
        serverAssembly.setPrintReplicationReports(true);
        serverAssembly.setPrintSummaryReport(true);
        serverAssembly.setSaveReplicationData(true);
        System.out.println(serverAssembly);
        serverAssembly.init();
//        If running threaded, use this construct:
//        new Thread(serverAssembly1).start();

//        Here we just call run(), otherwise the statistics will
//        all be empty.
        serverAssembly.run();

//        Output each designPoint stat.
        SampleStatistics[] stats = serverAssembly.getDesignPointStats();
        for (int i = 0; i < stats.length; ++i) {
            System.out.println(stats[i]);
        }
//        For each stat, output all replications
        for (int i = 0; i < stats.length; ++i) {
            SampleStatistics[] repStats = serverAssembly.getReplicationStats(i);
            for (int j = 0; j < repStats.length; ++j) {
                System.out.print("Rep #" + j + " ");
                System.out.println(repStats[j]);
            }
        }
    }

}
