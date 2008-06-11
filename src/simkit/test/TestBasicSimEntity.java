/*
 * TestBasicSimEntity.java
 *
 * Created on April 25, 2002, 2:03 PM
 */

package simkit.test;
import simkit.BasicSimEntity;
import simkit.Schedule;
import simkit.examples.ArrivalProcess2;
import simkit.examples.SimpleServer;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.util.PropertyChangeFrame;

/**
 *
 * @author  Arnold Buss
 * @version $Id: TestBasicSimEntity.java 1049 2008-02-08 16:44:09Z ahbuss $
 */
public class TestBasicSimEntity {

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        String arrivalDist = "Exponential";
        Object[] arrivalParam = new Object[] { new Double(1.5) };
        String serviceDist = "Exponential";
        Object[] serviceParam = new Object[] { new Double(2.2) };
        RandomVariate[] rv = new RandomVariate[2];
        rv[0] = RandomVariateFactory.getInstance(arrivalDist, arrivalParam);
        rv[1] = RandomVariateFactory.getInstance(serviceDist, serviceParam);
        BasicSimEntity arrival = new ArrivalProcess2(rv[0]);

        BasicSimEntity server = new SimpleServer(2, rv[1]);
        BasicSimEntity server2 = new SimpleServer(1, rv[0]);
        arrival.addSimEventListener(server);
        server.addSimEventListener(server2);
        
        PropertyChangeFrame pcf = new PropertyChangeFrame();
        server2.addPropertyChangeListener(pcf);
        pcf.setVisible(true);

        
        Schedule.setVerbose(true);
//        Schedule.stopOnEvent("Arrival", 10);
        
        Schedule.getDefaultEventList().dump("Before reset()");
        
        Schedule.stopAtTime(20.0);
        Schedule.reset();
        Schedule.getDefaultEventList().dump("After reset()");

        Schedule.startSimulation();
        
    }

}
