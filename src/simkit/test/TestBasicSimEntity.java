/*
 * TestBasicSimEntity.java
 *
 * Created on April 25, 2002, 2:03 PM
 */

package simkit.test;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
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
 * 
 */
public class TestBasicSimEntity {

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        String arrivalDist = "Exponential";
        String serviceDist = "Exponential";
        RandomVariate[] rv = new RandomVariate[2];
        rv[0] = RandomVariateFactory.getInstance(arrivalDist, 1.5);
        rv[1] = RandomVariateFactory.getInstance(serviceDist, 2.2);
        BasicSimEntity arrival = new ArrivalProcess2(rv[0]);

        BasicSimEntity server = new SimpleServer(2, rv[1]);
        BasicSimEntity server2 = new SimpleServer(1, rv[0]);
        arrival.addSimEventListener(server);
        server.addSimEventListener(server2);
        
        PropertyChangeFrame pcf = new PropertyChangeFrame();
        pcf.setDefaultCloseOperation(EXIT_ON_CLOSE);
        server2.addPropertyChangeListener(pcf);
        pcf.setVisible(true);

        System.out.println(arrival);
        Schedule.setVerbose(true);
//        Schedule.stopOnEvent("Arrival", 10);
        
        Schedule.getDefaultEventList().dump("Before reset()");
        
        Schedule.stopAtTime(20.0);
        Schedule.reset();
        Schedule.getDefaultEventList().dump("After reset()");

        Schedule.startSimulation();
        
    }

}
