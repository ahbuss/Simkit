package simkit.test;

import simkit.Schedule;
import simkit.examples.ArrivalProcess;
import simkit.stat.SimpleStatsTally;
import simkit.random.RandomVariateFactory;

public class TestSimpleStatsTally {

    public static void main(String[] args) {
        SimpleStatsTally sst = new SimpleStatsTally();
//        SimpleStats ss = new SimpleStats();
        for (int i = 1; i < 100001; i++) {
            sst.newObservation(i);
//            ss.newObservation(i);
        }
        System.out.println(sst);
//        System.out.println(ss);

//        ss.setName("numberArrivals");
        sst.setName("numberArrivals");
        sst.reset();
//        ss.reset();

        ArrivalProcess ap =
            new ArrivalProcess(
                RandomVariateFactory.getInstance(
                    "simkit.random.ExponentialVariate",
                    new Object[] { new Double(1.7) },
                    12345L
                )
            );
//        ap.addPropertyChangeListener(ss);
        ap.addPropertyChangeListener(sst);

        Schedule.stopAtTime(20.0);
        Schedule.reset();
        Schedule.startSimulation();

        System.out.println(sst);
//        System.out.println(ss);
        System.out.println(sst.clone());
    }
} 