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
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestRenewalProcess extends SimEntityBase {

    protected int count;
    
    private RandomVariate iat;
    /** Creates new TestRenewalProcess */
    public TestRenewalProcess(RandomVariate iat) {
        this.iat = RandomVariateFactory.getInstance(iat);
    }
    
    public void reset() {
        super.reset();
        count = 0;
    }
    
    public void doRun() {
        waitDelay("Arrival", iat.generate());
    }
    
    public void doArrival() {
        firePropertyChange("arrival", ++count);
        waitDelay("Arrival", iat.generate());
    }
    
    public String toString() {
        return "Arrival Process: " + iat;
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        String dist = "Exponential";
        double meanIAT = 1.7;
        Object[] params = new Object[] { new Double(meanIAT) };
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
            System.out.println(df.format(ren) + "\t" + 
                    df.format(iat) + "\t" + 
                    df.format(diff));
        }
        System.out.println(ap);
        Schedule.setVerbose(true);
        Schedule.stopOnEvent("Arrival", 10);
        Schedule.reset();
        Schedule.startSimulation();
    }

}
