package simkit.examples;
import simkit.Schedule;
import simkit.random.CongruentialSeeds;
import simkit.random.RandomNumber;
import simkit.random.RandomNumberFactory;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.util.SimplePropertyDumper;
/**
 * Tests the ShortestQueueDispatcher.
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestShortestQueueDispatcher {
    
    public static void main(String[] args) {
        RandomVariate iat = RandomVariateFactory.getInstance("Exponential",
            new Object[] { new Double(1.0) }, CongruentialSeeds.SEED[0]);
        RandomVariate[] servTime = new RandomVariate[5];
        RandomNumber rng = RandomNumberFactory.getInstance(CongruentialSeeds.SEED[1]);
        Server[] server = new Server[servTime.length];
        for (int i = 0; i < servTime.length; ++i) {
            servTime[i] = RandomVariateFactory.getInstance("Exponential",
                new Object[] { new Double(4.5) }, rng);
            server[i] = new Server(1, servTime[i]);
        }
        
        ArrivalProcess arrival = new ArrivalProcess(iat);        
        ShortestQueueDispatcher dispatch = new ShortestQueueDispatcher(server);
        arrival.addSimEventListener(dispatch);
        
        dispatch.addPropertyChangeListener(new SimplePropertyDumper());
        
        Schedule.setSingleStep(true);
        Schedule.reset();
        Schedule.startSimulation();
    }
    
}
