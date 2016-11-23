package simkit.test;

import simkit.Schedule;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;

public class TestSimpleStatsTally {

    public static void main(String[] args) throws CloneNotSupportedException {
        SimpleStatsTally sst = new SimpleStatsTally();
//        SimpleStats ss = new SimpleStats();
        for (int i = 0; i <= 10; i++) {
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
                    "Exponential",1.7) );
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