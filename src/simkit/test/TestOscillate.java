package simkit.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.util.PropertyDataLogger;

/**
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class TestOscillate extends SimEntityBase {

    public static RandomVariate exp = RandomVariateFactory.getInstance(
            "simkit.random.ExponentialVariate", 1.0);

    private  double deltaT;
    private  RandomVariate rv;

    /**
     * Creates new TestOacillate
     * @param deltaT Given &Delta;t
     * @param rv Given RandomVariate to generate observations
     */
    public TestOscillate(double deltaT, RandomVariate rv) {
        setDeltaT(deltaT);
        this.rv = RandomVariateFactory.getInstance(rv);
    }

    public void doRun() {
        waitDelay("Fire", 0.0);
    }

    public void doFire() {
        double obs = rv.generate();
        firePropertyChange("observation", Schedule.getSimTimeStr() + " " + obs);
        waitDelay("Fire", deltaT);
    }

    /**
     * @return the deltaT
     */
    public double getDeltaT() {
        return deltaT;
    }

    /**
     * @param deltaT the deltaT to set
     */
    public void setDeltaT(double deltaT) {
        this.deltaT = deltaT;
    }

    /**
     * @return the rv
     */
    public RandomVariate getRv() {
        return rv;
    }

    /**
     * @param rv the rv to set
     */
    public void setRv(RandomVariate rv) {
        this.rv = rv;
    }

    public String paramString() {
        return "TestOscillate (deltaT = " + deltaT + " distribution = " + rv + ")";
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])  {
        String name = "simkit.random.OscillatingExponentialVariate";
        Object[] params = new Object[]{
            0.0, //mean
            1.0, //amplitude
            1.0, //frequency
            0.5 //phase
        };

        RandomVariate rv = RandomVariateFactory.getInstance(
                name, params
        );
        System.out.println(rv);
        TestOscillate to = new TestOscillate(0.1, rv);
        System.out.println(to.paramString());

        String outputFileName = args.length > 0 ? args[0] : "test.out";
        File outputFile = new File(outputFileName);
        System.out.println("to be written: " + outputFile.getAbsolutePath());
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestOscillate.class.getName()).log(Level.SEVERE, null, ex);
        }

        PropertyDataLogger pdl = new PropertyDataLogger("observation", out);
        to.addPropertyChangeListener(pdl);

        Schedule.stopAtTime(2.0);
        Schedule.reset();
        Schedule.setVerbose(false);
        Schedule.startSimulation();

        pdl.closeOutput();
    }

}
