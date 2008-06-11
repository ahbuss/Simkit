package simkit.test;

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.stat.MultipleBooleanCounters;
import simkit.util.SimplePropertyDumper;

/**
 * @version $Id: TestBooleanCounters.java 768 2005-05-09 19:04:46Z ahbuss $
 * @author  ahbuss
 */
public class TestBooleanCounters extends SimEntityBase {
    
    protected boolean nextValue;
    
    public void doRun() {
        waitDelay("Fire", 0.0);
    }
    
    public void doFire() {
        firePropertyChange("one", nextValue);
        nextValue = !nextValue;
        firePropertyChange("two", nextValue);
        firePropertyChange("two", nextValue);
        firePropertyChange("three", nextValue);
        
        waitDelay("Fire", 1.0);
    }
    
    public static void main(String[] args) {
        TestBooleanCounters tbc = new TestBooleanCounters();
        MultipleBooleanCounters mbc = new MultipleBooleanCounters();
        tbc.addPropertyChangeListener(mbc);
        SimplePropertyDumper dumper = new SimplePropertyDumper();
        tbc.addPropertyChangeListener(dumper);
        
        Schedule.setVerbose(true);
        Schedule.stopAtTime(10.0);
        Schedule.reset();
        Schedule.startSimulation();
        
        System.out.println(mbc);
        
        System.out.println("Getting 'two': " + mbc.getCounter("two"));
        System.out.println("Getting 'four': " + mbc.getCounter("four"));        
        
        mbc.reset();
        System.out.println(mbc);
        mbc.clear();
        System.out.println(mbc);
    }
}
