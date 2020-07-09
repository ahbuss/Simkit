package simkit.test;

import simkit.Schedule;
import simkit.examples.ArrivalProcess;
import simkit.examples.ShortestQueueDispatcher;
import simkit.examples.SimpleServer;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.util.SimplePropertyDumper;

/**
 * Tests the ShortestQueueDispatcher.
 *
 * @author Arnold Buss
 */
public class TestShortestQueueDispatcher {

    public static void main(String[] args) {
        RandomVariate iat
                = RandomVariateFactory.getInstance("Exponential", 1.0);
        RandomVariate[] servTime = new RandomVariate[5];
        SimpleServer[] server = new SimpleServer[servTime.length];
        for (int i = 0; i < servTime.length; ++i) {
            servTime[i] = RandomVariateFactory.getInstance("Exponential", 4.5);
            server[i] = new SimpleServer(1, servTime[i]);
        }

        ArrivalProcess arrival = new ArrivalProcess(iat);
        ShortestQueueDispatcher dispatch = new ShortestQueueDispatcher(server);
        arrival.addSimEventListener(dispatch);

        dispatch.addPropertyChangeListener(new SimplePropertyDumper());

        Schedule.stopOnEvent(1000, "EndService");
        Schedule.setSingleStep(true);
        Schedule.reset();
        Schedule.startSimulation();
    }

}
