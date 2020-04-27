package simkit.test;

import java.beans.PropertyChangeListener;
import simkit.BasicAssembly;
import simkit.SimEntity;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariateFactory;
import simkit.stat.SampleStatistics;
import simkit.stat.SimpleStatsTimeVarying;
import simkit.util.SimplePropertyDumper;

/**
 * <p>
 * Simple example of subclassing BasicAssembly and running an ArrivalProcess in
 * verbose mode with a SimplePropertyDumper.
 * <p>
 * In this form, the parameters are set in main().
 *
 * 
 * @author ahbuss
 */
public class ArrivalProcessAssembly extends BasicAssembly {

    /**
     * <p>
     * Instantiate the ArrivalProcess in the simEntity array.
     * <p>
     * Instantiate a SimplePropertyDumper in the propertyChangeListener array.
     */
    public ArrivalProcessAssembly() {
    }

    protected void createReplicationStats() {
        replicationStats = new SampleStatistics[]{
            new SimpleStatsTimeVarying("numberArrivals")
        };
    }

    protected void createPropertyChangeListeners() {
        propertyChangeListener = new PropertyChangeListener[]{
            new SimplePropertyDumper()
        };
    }

    /**
     * Have the SimplePropertyDumper listen to the ArrivalProcess. The indices
     * for this must be determined by the generator for the code.
     */
    protected void hookupPropertyChangeListeners() {
        simEntity[0].addPropertyChangeListener(propertyChangeListener[0]);
    }

    /**
     * Hookup the stats object - this doesn't mean much, but illustrates how.
     */
    public void hookupReplicationListeners() {
        simEntity[0].addPropertyChangeListener(replicationStats[0]);
    }

    /**
     * Does nothing since there are no SimEventListeners
     */
    public void hookupSimEventListeners() {
    }

    protected void createSimEntities() {
        simEntity = new SimEntity[]{
            new ArrivalProcess(
            RandomVariateFactory.getInstance(
            "Exponential", 2.1)
            )
        };

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Instntiate ArrivalProcessAssembly and set parameters.
        ArrivalProcessAssembly arrivalProcessAssembly = new ArrivalProcessAssembly();
        arrivalProcessAssembly.setStopTime(20.0);
        arrivalProcessAssembly.setVerbose(true);
        arrivalProcessAssembly.setSingleStep(false);
        arrivalProcessAssembly.setNumberReplications(2);
        arrivalProcessAssembly.setPrintReplicationReports(true);
        arrivalProcessAssembly.setPrintSummaryReport(true);

//        Make sure parameters are correctly set.
        System.out.println(arrivalProcessAssembly);
        arrivalProcessAssembly.init();
//        Run in a separate thread.
        new Thread(arrivalProcessAssembly).start();
    }

}
