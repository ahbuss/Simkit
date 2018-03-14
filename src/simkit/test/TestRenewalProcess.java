/*
 * TestRenewalProcess.java
 *
 * Created on March 29, 2002, 8:12 AM
 */
package simkit.test;

import java.text.DecimalFormat;

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class TestRenewalProcess extends SimEntityBase {

    protected int count;

    private RandomVariate interarrivalTimeGenerator;

    /**
     * Creates new TestRenewalProcess
     *
     * @param interarrivalTimeGenerator Generates interarrival times
     */
    public TestRenewalProcess(RandomVariate interarrivalTimeGenerator) {
        setInterarrivalTimeGenerator(interarrivalTimeGenerator);
    }

    @Override
    public void reset() {
        super.reset();
        count = 0;
    }

    public void doRun() {
        waitDelay("Arrival", interarrivalTimeGenerator.generate());
    }

    public void doArrival() {
        firePropertyChange("arrival", ++count);
        waitDelay("Arrival", interarrivalTimeGenerator.generate());
    }

    @Override
    public String toString() {
        return "Arrival Process: " + interarrivalTimeGenerator;
    }

    /**
     * @return the interarrivalTimeGenerator
     */
    public RandomVariate getInterarrivalTimeGenerator() {
        return interarrivalTimeGenerator;
    }

    /**
     * @param interarrivalTimeGenerator the interarrivalTimeGenerator to set
     */
    public void setInterarrivalTimeGenerator(RandomVariate interarrivalTimeGenerator) {
        this.interarrivalTimeGenerator = interarrivalTimeGenerator;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        String dist = "Exponential";
        double meanIAT = 1.7;
        Object[] params = new Object[]{new Double(meanIAT)};
        RandomVariate exp = RandomVariateFactory.getInstance(dist, params);
        dist = "RenewalProcess";
        params[0] = exp;
        RandomVariate pp = RandomVariateFactory.getInstance(dist, params);
        exp = RandomVariateFactory.getInstance(exp);
        TestRenewalProcess ap = new TestRenewalProcess(exp);
        DecimalFormat df = new DecimalFormat("0.000");
        double last = 0.0;
        for (int i = 0; i < 10; i++) {
            double ren = pp.generate();
            double iat = exp.generate();
            double diff = ren - last;
            last = ren;
            System.out.println(df.format(ren) + "\t"
                    + df.format(iat) + "\t"
                    + df.format(diff));
        }
        System.out.println(ap);
        Schedule.setVerbose(true);
        Schedule.stopOnEvent(10, "Arrival");
        Schedule.reset();
        Schedule.startSimulation();
    }

}
