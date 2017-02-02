package simkit.test;

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.TimeTruncatedTallyStat;
import simkit.util.SimplePropertyDumper;

/**
 * Test of TimeTruncatedTallyStat.
 * <h2>Output</h2><pre>
ArrivalProcess.1
	interarrivalTimeGenerator = Constant (1.0)
Counter.2
	probability = 0.75
When truncated:
value (TALLY)
100 0.000 1.000 0.790 0.168 0.409
value (TALLY)
9900 0.000 1.000 0.756 0.185 0.430 true
When not truncated (simulation ended before truncation point:
value (TALLY)
10 0.000 1.000 0.700 0.233 0.483
value (TALLY)
10 0.000 1.000 0.700 0.233 0.483 false
</pre>
* @author ahbuss
 */
public class TestTimeTruncatedTallyStat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomVariate iat
                = RandomVariateFactory.getInstance("Constant", 1.0);
        ArrivalProcess arrivalProcess = new ArrivalProcess(iat);

        Counter counter = new Counter(0.75);
        arrivalProcess.addSimEventListener(counter);
        System.out.println(arrivalProcess);
        System.out.println(counter);

        SimplePropertyDumper simplePropertyDumper
                = new SimplePropertyDumper(true);
//        counter.addPropertyChangeListener(simplePropertyDumper);

//        counter = new Counter(1.2);

        TimeTruncatedTallyStat stat = new TimeTruncatedTallyStat("value", 100.0);
        counter.addPropertyChangeListener(stat);

        Schedule.stopOnEvent(10000, "Arrival");
        Schedule.setVerbose(false);

        Schedule.reset();
        Schedule.startSimulation();

        System.out.println("When truncated:");
        System.out.println(stat.getStatsAtTruncation());
        System.out.println(stat);
        Schedule.reset();
        
        Schedule.stopAtTime(10.0);
        stat.setTruncationTime(20.0);
        stat.reset();
        
        Schedule.reset();
        Schedule.startSimulation();
        
        System.out.println("When not truncated (simulation ended before truncation point:");
        System.out.println(stat.getStatsAtTruncation());
        System.out.println(stat);
        
        
    }

    public static class Counter extends SimEntityBase {

        private double probability;

        protected boolean value;

        public Counter(double probability) {
            this.setProbability(probability);
        }

        public void doArrival() {
            value
                    = RandomVariateFactory.getDefaultRandomNumber().draw()
                    < getProbability();
            firePropertyChange("value", getValue());
        }

        /**
         * @return the probability
         */
        public double getProbability() {
            return probability;
        }

        /**
         * @param probability the probability to set
         */
        public void setProbability(double probability) {
            if (probability < 0.0 || probability > 1.0) {
                throw new IllegalArgumentException(
                        "probability must be \u2208 [0,1]: " + probability);
            }
            this.probability = probability;
        }

        /**
         * @return the value
         */
        public boolean getValue() {
            return value;
        }
    }
}
