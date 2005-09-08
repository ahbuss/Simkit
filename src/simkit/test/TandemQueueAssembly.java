package simkit.test;

import simkit.Adapter;
import simkit.BasicAssembly;
import simkit.SimEntity;
import simkit.examples.ArrivalProcess;
import simkit.examples.Server;
import simkit.random.RandomVariateFactory;
import simkit.stat.MultipleSimpleStatsTally;
import simkit.stat.SampleStatistics;
import simkit.stat.SimpleStatsTimeVarying;

/**
 * <p>This is an example of multiple SimEntities that fire identical property names.  This is 
 * overcome by using instances of <code>MultipleSimpleStatsTally</code> as the 
 * designPointStats instances.  The IndexedPropertyChange event is the one listened for here,
 * and they are separated by index - each instance of <code>MultipleSimpleStatsTally</code>
 * listens for an IndexedPropertyChangeEvent of the same base name, so two are required, one
 * for numberInQueue and one for numberAvailableServers.
 *
 * <p>This also illustrates the use of the Adapter class to make the tandem queue model
 * work.  Each adapter instance hears an EndService event and schedules an Arrival event, which
 * is then heard by the following station.  All the listening must be established in 
 * hookupSimEventListeners().
 *
 * @version $Id$
 * @author ahbuss
 */
public class TandemQueueAssembly extends BasicAssembly {
    
    protected SimEntity[] adapter;
    
    public TandemQueueAssembly() {
        simEntity = new SimEntity[4];
        simEntity[0] = new ArrivalProcess(RandomVariateFactory.getInstance("Exponential", new Object[] { new Double(1.1) }));
        simEntity[1] = new Server(1, RandomVariateFactory.getInstance("Exponential", new Object[] { new Double(1.0) }));
        simEntity[2] = new Server(3, RandomVariateFactory.getInstance("Exponential", new Object[] { new Double(3.0) }));
        simEntity[3] = new Server(2, RandomVariateFactory.getInstance("Exponential", new Object[] { new Double(2.0) }));

        adapter = new SimEntity[2]; 
        adapter[0] = new Adapter("EndService", "Arrival");
        adapter[1] = new Adapter("EndService", "Arrival");
        
        replicationStats = new SampleStatistics[6];
        for (int i = 0; i < 3; ++i) {
            replicationStats[i] = new SimpleStatsTimeVarying("numberInQueue");
            replicationStats[i + 3] = new SimpleStatsTimeVarying("numberAvailableServers");
        }
        
        performHookups();
                                        
    }
    
    /**
     * Set up adapter listening
     */
    protected void hookupSimEventListeners() {
        simEntity[0].addSimEventListener(simEntity[1]);
        for (int i = 1; i < simEntity.length - 1; ++i) {
            simEntity[i].addSimEventListener(adapter[i - 1]);
            adapter[i - 1].addSimEventListener(simEntity[i + 1]);
        }
    }
    /**
     * The three workstations are listened to, not the ArrivalProcess.  The
     * indexing must be kept straight for the Stats objects to be receiving
     * the correct data.
     */
    protected void hookupReplicationListeners() {
        for (int i = 0; i < 3; ++i) {
            simEntity[1 + i].addPropertyChangeListener(replicationStats[i]);
            simEntity[1 + i].addPropertyChangeListener(replicationStats[3 + i]);
        }
    }
    
    /**
     * Use MultipleSimpleStatsTally to listen to the means for the replicationStats.  Only
     * two instances are needed - they will sort out the data by the index of the
     * InexedPropertyChangeEvent.
     */
    protected void createDesignPointStats() {
        designPointStats = new SampleStatistics[2];
        designPointStats[0] = new MultipleSimpleStatsTally("numberInQueue.mean");
        designPointStats[1] = new MultipleSimpleStatsTally("numberAvailableServers.mean");
    }
    
    /**
     * The summary report must be overridden to see the individual statistics.
     */
    public String getSummaryReport() {
        StringBuffer buf = new StringBuffer("Summary Output Report:");
        buf.append(System.getProperty("line.separator"));
        buf.append(super.toString());
        for (int i = 0; i < designPointStats.length; ++i) {
            buf.append(System.getProperty("line.separator"));
            buf.append(designPointStats[i]);
        }
       return buf.toString();
    }
    
    /**
     * Use the form of setting parameters in main, leaving the unset parameters
     * to their defaults.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TandemQueueAssembly tandemQueueAssembly = new TandemQueueAssembly();
        
        tandemQueueAssembly.setStopTime(5000.0);
        tandemQueueAssembly.setVerbose(false);
        tandemQueueAssembly.setNumberReplications(10);
        tandemQueueAssembly.setPrintReplicationReports(true);
        
        new Thread(tandemQueueAssembly).start();
        
    }
    
}
