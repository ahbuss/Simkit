package simkit.test;

import simkit.Schedule;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariateFactory;
import simkit.stat.MultipleSimpleStatsTimeVarying;
import simkit.stat.SampleStatistics;

public class TestMultipleSimpleStatsTimeVarying {

    public static void main(String[] args) {
        ArrivalProcess ap
                = new ArrivalProcess(
                        RandomVariateFactory.getInstance(
                                "Exponential", new Double(1.7)
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
            System.out.println(i + ": " + stats[i]);
        }

    }
}
