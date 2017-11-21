package simkit.test;
import simkit.*;
import simkit.util.*;
/**
 *
 * @author  ahbuss
 */
public class TestAdapter {
    
    public static void main(String[] args) {
        TestSimEntityClass tsec = new TestSimEntityClass();
        Adapter adapter = new Adapter("This", "That");
        MyListener ml = new MyListener();
        
        System.out.println(adapter);
        
        tsec.addSimEventListener(adapter);
        adapter.addSimEventListener(ml);
        
        ml.addPropertyChangeListener(new SimplePropertyDumper());
        
        Schedule.setSingleStep(true);
        Schedule.reset();
        Schedule.startSimulation();
    }
    
}
