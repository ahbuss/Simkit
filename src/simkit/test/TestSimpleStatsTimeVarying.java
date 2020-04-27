package simkit.test;

import simkit.Schedule;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTimeVarying;

public class TestSimpleStatsTimeVarying {

    public static void main(String[] args) throws CloneNotSupportedException {
        SimpleStatsTimeVarying sst = new SimpleStatsTimeVarying();
        sst.setName("numberArrivals");
        sst.newObservation(0.0);
        ArrivalProcess ap
                = new ArrivalProcess(
                        RandomVariateFactory.getInstance(
                                "simkit.random.ExponentialVariate",
                                new Object[]{1.0})
                );
        ap.addPropertyChangeListener(sst);

        Schedule.stopAtTime(2000.0);
        Schedule.reset();
        Schedule.startSimulation();

        System.out.println(sst);
        System.out.println(sst.clone());
    }
}
