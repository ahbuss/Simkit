package simkit.test;
import simkit.*;
/**
 * Tests new addition to SimEventComp that allows a "precision" to
 * be specified.  Events within the precision amount of time are considered
 * to be "simultaneous".
 *
 * @author  ahbuss
 */
public class TestSimEventComp extends SimEntityBase {
    
    public static void main(String[] args) {
        Schedule.getDefaultEventList().setSimEventPrecision(1.0E-10);
        
        double secondsInAnHour = 60 * 60;
        double secondsInADay = 24 * 60 * 60;
        double secondsInAWeek = 7 * 24 * 60 * 60;
        double secondsInAMonth = 31 * 24 * 60 * 60;
        double secondsInAYear = 365 * 24 * 60 * 60;
        
        double baseTime = secondsInADay;
//        double baseTime = secondsInAMonth;
//        double baseTime = secondsInAYear;
        
        TestSimEventComp test = new TestSimEventComp();
        test.waitDelay("Foo1", baseTime + 1.0E-10);
        test.waitDelay("Foo2", baseTime);
        test.waitDelay("Foo3", baseTime + 1.0E-9);
        test.waitDelay("Foo4", baseTime + 1.E-11);
        test.waitDelay("Foo5", baseTime - 1.0E-10);
        test.waitDelay("Foo6", baseTime - 1.5E-10);
        
        Schedule.getDefaultEventList().setFormat("0.000000000000000");
        Schedule.setVerbose(true);
        Schedule.startSimulation();
        
        test.waitDelay("Foo7", -1.0E-11);
        
        System.out.println("Order should be: 6 1 2 5 4 3");
        
        Schedule.startSimulation();
        
        double timeInSeconds = secondsInAMonth * Math.random();
        double convertedToHours = timeInSeconds / secondsInAnHour;
        double backToSeconds = convertedToHours * secondsInAnHour;
        
        System.out.println(timeInSeconds);
        System.out.println(convertedToHours);
        System.out.println(backToSeconds);
        
    }
    
}
