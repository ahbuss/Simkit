/*
 * TestOacillate.java
 *
 * Created on March 14, 2002, 3:01 PM
 */

package simkit.test;
import simkit.*;
import simkit.random.*;
import simkitx.random.*;
import simkit.util.*;
import java.io.*;
import java.net.*;
/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class TestOscillate extends SimEntityBase {

    public static RandomVariate exp = RandomVariateFactory.getInstance(
        "simkit.random.ExponentialVariate", new Double[] { new Double(1.0) });
    
    private double deltaT;
    private RandomVariate rv;
    
    /** Creates new TestOacillate */
    public TestOscillate(double dt, RandomVariate rv) {
        deltaT = dt;
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
    
    public String paramString() { 
        return "TestOscillate (deltaT = " + deltaT + " distribution = " + rv + ")";
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) throws Throwable {
        String name = "simkitx.random.OscillatingExponentialVariate";
        Object[] params = new Object[] {
            new Double(1.0),
            new Double(1.0),
            new Double(1.0),
            new Double(0.0)
        };
        
        RandomVariate rv = RandomVariateFactory.getInstance(
            name, params
            );
        System.out.println(rv);
        TestOscillate to = new TestOscillate(0.1, rv);
        System.out.println(to.paramString());
        
        URL url = TestOscillate.class.getResource("..");
        
        File outputDir = new File(url.getFile());
        String outputFileName = args.length > 0 ? args[0] : "test.out";
        File outputFile = new File(outputDir, outputFileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(outputFile);
        
        PropertyDataLogger pdl = new PropertyDataLogger("observation", out);
        to.addPropertyChangeListener(pdl);
        
        Schedule.stopAtTime(2.0);
        Schedule.reset();
        Schedule.setVerbose(false);
        Schedule.startSimulation();
        
        pdl.closeOutput();
    }

}
