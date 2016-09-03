package simkit.examples;

import simkit.Schedule;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.util.SimplePropertyDumper;

/**
 * Tests the ShortestQueueDispatcher.
 *
 * @author Arnold Buss
 * @version $Id: TestShortestQueueDispatcher.java 1083 2008-06-11 20:41:21Z
 * kastork $
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

        Schedule.setSingleStep(true);
        Schedule.reset();
        Schedule.startSimulation();
    }

}
