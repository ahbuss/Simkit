package simkit.test;

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.SimEventRedispatcher;
import simkit.examples.ArrivalProcess;
import simkit.examples.SimpleServer;
import simkit.random.RandomVariateFactory;
import simkit.util.SimplePropertyDumper;

/**
 * @version $Id$
 * @author ahbuss
 */
public class TestSimEventRedispatcher1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrivalProcess arrivalProcess = new ArrivalProcess(
                RandomVariateFactory.getInstance("Exponential", 1.7));
        SimpleServer simpleServer = new SimpleServer(1,
                RandomVariateFactory.getInstance("Gamma", 1.2, 1.8));
        arrivalProcess.addSimEventListener(simpleServer);
        
        ReDispatchListener reDispatchListener = new ReDispatchListener();
        simpleServer.addSimEventListener(reDispatchListener);
        
        new SimEventRedispatcher(simpleServer);

        SimplePropertyDumper simplePropertyDumper = new SimplePropertyDumper(true);
        arrivalProcess.addPropertyChangeListener(simplePropertyDumper);
        simpleServer.addPropertyChangeListener(simplePropertyDumper);

        Schedule.setVerbose(true);

        Schedule.stopOnEvent(5, "EndService");
        
        Schedule.reset();
        Schedule.startSimulation();

    }
    
    public static class ReDispatchListener extends SimEntityBase {
        
        public void doArrival() {
            firePropertyChange("timeHeardArrival", getEventList().getSimTime());
        }
    }

}
