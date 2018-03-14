/*
 * Main.java
 *
 * Created on October 1, 2001, 5:38 PM
 */

package simkit.test;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import simkit.Schedule;
import simkit.examples.ArrivalProcess;

import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 *
 *  TODO: Fix this
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestNHPoissonProcess {
    
    private double mean;
    
    private double lambdaDay;
    private double lambdaNight;

    public double lambdaInverse(double t) {
        return mean * t;
    }
    
    public double inv2(double t) {
        if (t < 10.0) return t*1000.0;
        else if (t < 20.0) return t - 9.99;
        else return (t - 9.99)/1000.0;
    }
    
    public double dayNight(double t) {
        t = Math.IEEEremainder(t, 24.0);
        double val = Double.NaN;
        if (t < 12.0) {
            val =  t * getLambdaDay();
        } else {
            val = 12 * getLambdaDay() + (t - 12.0) * getLambdaNight();
        }
        return val;
    }
    
    public void setMean(double m) { mean = m; }
    
    public double getMean() { return mean; }
    
//    public String toString() { return "Constant Rate(" + getMean() + ")"; }

    public String toString() {
        return "DayNight (" + getLambdaDay() + " " + getLambdaNight() + ")";
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main (String args[])  {
        try {
            double inter;
            double lastArr = 0.0;
            TestNHPoissonProcess test = new TestNHPoissonProcess();
            
            test.setMean(2.0);
            
            test.setLambdaDay(1.5);
            test.setLambdaNight(5.0);
            
            for (double t = 0.0; t < 48.0; t += 0.1) {
                System.out.println(test.dayNight(t));
            }
            
            if (true) return;
            
            Method m = TestNHPoissonProcess.class.getMethod("dayNight",
                     Double.TYPE );
            Object[] params = new Object[] { m, test };
            String dist = "simkit.random.NHPoissonProcessVariate";
            
            RandomVariate rv = RandomVariateFactory.getInstance(dist, params);
            
            System.out.println(rv);
            System.out.println(((simkit.random.NHPoissonProcessVariate) rv).stateString());
            
            for (int i = 1; i < 100; i++) {
                inter = rv.generate();
                lastArr += inter;
                System.out.println("Arrival Time: " + lastArr + "\tInterarrival Time: " + inter);
            }
            
            Schedule.reset();
            params = new Object[] { "dayNight", test };
            
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
            
            Schedule.stopAtTime(300.0);
            Schedule.setVerbose(true);
            
            Schedule.reset();
            Schedule.startSimulation();
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(TestNHPoissonProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }

    public double getLambdaDay() {
        return lambdaDay;
    }

    public void setLambdaDay(double lambdaDay) {
        this.lambdaDay = lambdaDay;
    }

    public double getLambdaNight() {
        return lambdaNight;
    }

    public void setLambdaNight(double lambdaNight) {
        this.lambdaNight = lambdaNight;
    }

}
