package simkit.test;

import simkit.Schedule;
import simkit.data.SimpleStats;
import simkit.examples.ArrivalProcess;
import simkit.stat.SampleStatistics;
import simkit.stat.MultipleSimpleStatsTimeVarying;
import simkit.random.RandomVariateFactory;

public class TestMultipleSimpleStatsTimeVarying {

    public static void main(String[] args) {
        ArrivalProcess ap =
            new ArrivalProcess(
                RandomVariateFactory.getInstance(
                    "simkit.random.ExponentialVariate",
                    new Object[] { new Double(1.7) },
                    12345L
                )
            );

        IndexRedispatcher ir = new IndexRedispatcher(ap, 1, 2, 10);
        MultipleSimpleStatsTimeVarying msst = new MultipleSimpleStatsTimeVarying();
        msst.setName("numberArrivals");

        ap.addPropertyChangeListener(ir);
        ir.addPropertyChangeListener(msst);

        Schedule.stopAtTime(20.0);
        Schedule.reset();
        Schedule.startSimulation();

        System.out.println(msst);

        SampleStatistics[] stats = msst.getAllSampleStat();
        for (int i = 0; i < stats.length; i++) {
            System.out.println(i+ ": " + stats[i]);
        }

    }
} 