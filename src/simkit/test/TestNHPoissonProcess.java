/*
 * Main.java
 *
 * Created on October 1, 2001, 5:38 PM
 */

package simkit.test;
import java.lang.reflect.Method;
import simkit.Schedule;
import simkit.examples.ArrivalProcess;

import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestNHPoissonProcess {
    
    private double mean;

    public double lambdaInverse(double t) {
        return mean * t;
    }
    
    public double inv2(double t) {
        if (t < 10.0) return t*1000.0;
        else if (t < 20.0) return t - 9.99;
        else return (t - 9.99)/1000.0;
    }
    
    public void setMean(double m) { mean = m; }
    
    public double getMean() { return mean; }
    
    public String toString() { return "Constant Rate(" + getMean() + ")"; }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) throws Throwable {
         double inter;
         double lastArr = 0.0;
         TestNHPoissonProcess test = new TestNHPoissonProcess();
         test.setMean(2.0);
         Method m = TestNHPoissonProcess.class.getMethod("lambdaInverse",
            new Class[] { Double.TYPE });
         Object[] params = new Object[] { m, test };
         String dist = "simkit.random.NHPoissonProcessVariate";
         
         RandomVariate rv = RandomVariateFactory.getInstance(dist, params);
         
         System.out.println(rv);
         System.out.println(((simkit.random.NHPoissonProcessVariate) rv).stateString());
         
         for (int i = 1; i < 10; i++) {
             inter = rv.generate();
             lastArr += inter;
             System.out.println("Arrival Time: " + lastArr + "\tInterarrival Time: " + inter);
         }
         
         Schedule.reset();
         params = new Object[] { "lambdaInverse", test };
         
         System.out.println(rv);
         System.out.println(((simkit.random.NHPoissonProcessVariate) rv).stateString());
         
         rv.setParameters(params);
         lastArr = 0.0;
         System.out.println("------------------//----------------");
         for (int i = 1; i < 10; i++) {
             inter = rv.generate();
             lastArr += inter;
             System.out.println("Arrival Time: " + lastArr + "\tInterarrival Time: " + inter);
         }
         
         ArrivalProcess arrivalProcess = new ArrivalProcess(rv);
         System.out.println(arrivalProcess);
         
         Schedule.stopAtTime(30.0);
         Schedule.setVerbose(true);
         
         Schedule.reset();
         Schedule.startSimulation();
         
    }

}
