package simkit.test;
import simkit.Schedule;
import simkit.data.SimpleStats;
import simkit.data.SamplingType;
import simkit.examples.ArrivalProcess;
import simkit.stat.SimpleStatsTimeVarying;
import simkit.random.RandomVariateFactory;

public class TestSimpleStatsTimeVarying {

    public static void main(String[] args) {
        SimpleStatsTimeVarying sst = new SimpleStatsTimeVarying();
        sst.setName("numberArrivals");
        sst.newObservation(0.0);
        SimpleStats ss = new SimpleStats("numberArrivals", SamplingType.TIME_VARYING);
        ss.newObservation(0.0);

        ArrivalProcess ap =
            new ArrivalProcess(
                RandomVariateFactory.getInstance(
                    "simkit.random.ExponentialVariate",
                    new Object[] { new Double(1.0) },
                    12345L
                )
            );
        ap.addPropertyChangeListener(ss);
        ap.addPropertyChangeListener(sst);

        Schedule.stopAtTime(2000.0);
        Schedule.reset();
        Schedule.startSimulation();

        System.out.println(sst);
        System.out.println(ss);
        System.out.println(sst.clone());
    }
} 