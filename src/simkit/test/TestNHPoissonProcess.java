/*
 * Main.java
 *
 * Created on October 1, 2001, 5:38 PM
 */

package simkit.test;
import simkit.random.*;
import java.lang.reflect.*;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class TestNHPoissonProcess {
    
    private double mean;

    /** Creates new Main */
    public TestNHPoissonProcess() {
    }
    
    public double lambdaInverse(double t) {
        return mean * t;
    }
    
    public void setMean(double m) { mean = m; }
    
    public double getMean() { return mean; }
    
    public String toString() { return "Constant Rate(" + getMean() + ")"; }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) throws Throwable {
         TestNHPoissonProcess test = new TestNHPoissonProcess();
         test.setMean(2.0);
         Method m = TestNHPoissonProcess.class.getMethod("lambdaInverse",
            new Class[] { Double.TYPE });
         Number startTime = new Double(0.0);
         Object[] params = new Object[] { m, test, startTime };
         String dist = "simkit.random.NHPoissonProcessVariate";
         
         RandomVariate rv = RandomVariateFactory.getInstance(dist, params, 12345L);
         
         for (int i = 1; i < 10; i++) {
             System.out.println(rv.generate());
         }
         
        
    }

}
