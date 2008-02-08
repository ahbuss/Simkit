/*
 * TestBridge.java
 *
 * Created on July 13, 2002, 12:00 PM
 */

package simkit.test;
import simkit.Bridge;
import simkit.PropertyChangeNamespace;
import simkit.Schedule;
import simkit.examples.ArrivalProcess;
import simkit.examples.SimpleServer;
import simkit.random.CongruentialSeeds;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.util.PropertyChangeFrame;
import simkit.util.SimplePropertyDumper;
/**
 *
 * @author  ahbuss
 */
public class TestBridge {
    
   /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String arrivalDistribution = "Exponential";
        Object[] arrivalParam = new Double[1];
        arrivalParam[0] = new Double(1.7);
        
        Object[][] serviceParam = 
            new Object[][] {
                new Object[] { new Double(1.4) },
                new Object[] { new Double(2.6) }
            };
        int[] SimpleServers = new int[] { 1, 2 };
        
        RandomVariate interArrivals = RandomVariateFactory.getInstance( arrivalDistribution, arrivalParam);
        
        ArrivalProcess arrival = new ArrivalProcess(interArrivals);
        
        RandomVariate[] serviceTimes = new RandomVariate[2];
        SimpleServer[] SimpleServer = new SimpleServer[2];
        
        for (int i = 0; i < serviceTimes.length; i++) {
            serviceTimes[i] = RandomVariateFactory.getInstance(arrivalDistribution,
                serviceParam[i]);
            SimpleServer[i] = new SimpleServer(SimpleServers[i], serviceTimes[i]);
        }
        
        Bridge bridge = new Bridge("EndService", "Arrival");
        
        arrival.addSimEventListener(SimpleServer[0]);
        SimpleServer[0].addSimEventListener(bridge);
        bridge.addSimEventListener(SimpleServer[1]);
        
        System.out.println(arrival);
        System.out.println(SimpleServer[0]);
        System.out.println(bridge);
        System.out.println(SimpleServer[1]);
        
        SimplePropertyDumper dump = new SimplePropertyDumper();
        SimpleServer[1].addPropertyChangeListener(dump);
        
        PropertyChangeNamespace namespace = new PropertyChangeNamespace(new Object[] {}, "tandem");
        SimpleServer[0].addPropertyChangeListener(namespace);
        SimpleServer[1].addPropertyChangeListener(namespace);
        
        PropertyChangeFrame pcf = new PropertyChangeFrame();
        namespace.addPropertyChangeListener(pcf);
        pcf.setVisible(true);
        
        Schedule.setSingleStep(true);
        Schedule.stopAtTime(100.0);
        
        Schedule.reset();
        Schedule.startSimulation();
        
    }
    
}
