/*
 * TestBridge.java
 *
 * Created on July 13, 2002, 12:00 PM
 */

package simkit.test;
import simkit.*;
import simkit.random.*;
import simkit.util.*;
import simkit.examples.*;
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
        int[] servers = new int[] { 1, 2 };
        long[] seeds =  (long[]) CongruentialSeeds.SEED.clone();
        
        RandomVariate interArrivals = RandomVariateFactory.getInstance( arrivalDistribution, arrivalParam, seeds[0]);
        
        ArrivalProcess arrival = new ArrivalProcess(interArrivals);
        
        RandomVariate[] serviceTimes = new RandomVariate[2];
        Server[] server = new Server[2];
        
        for (int i = 0; i < serviceTimes.length; i++) {
            serviceTimes[i] = RandomVariateFactory.getInstance(arrivalDistribution,
                serviceParam[i], seeds[i+1]);
            server[i] = new Server(servers[i], serviceTimes[i]);
        }
        
        Bridge bridge = new Bridge("EndService", "Arrival");
        
        arrival.addSimEventListener(server[0]);
        server[0].addSimEventListener(bridge);
        bridge.addSimEventListener(server[1]);
        
        System.out.println(arrival.paramString());
        System.out.println(server[0].paramString());
        System.out.println(bridge);
        System.out.println(server[1].paramString());
        
        SimplePropertyDumper dump = new SimplePropertyDumper();
        server[1].addPropertyChangeListener(dump);
        
        PropertyChangeNamespace namespace = new PropertyChangeNamespace(new Object[] {}, "tandem");
        server[0].addPropertyChangeListener(namespace);
        server[1].addPropertyChangeListener(namespace);
        
        PropertyChangeFrame pcf = new PropertyChangeFrame();
        namespace.addPropertyChangeListener(pcf);
        pcf.setVisible(true);
        
        Schedule.setSingleStep(true);
        Schedule.stopAtTime(100.0);
        
        Schedule.reset();
        Schedule.startSimulation();
        
    }
    
}
