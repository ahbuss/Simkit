package simkit.test;
import simkit.util.*;
import simkit.*;
/**
 *
 * @author  ahbuss
 */
public class TestPropertyChangeDispatcher extends SimEntityBase {
    
    private int foo;
    
    private double[] foobar;
    
    public TestPropertyChangeDispatcher() {
    }
    
    public void setFoo(int i) { foo = i; }
    
    public int getFoo() { return foo; }
    
    public void setFoobar(double[] f) { foobar = (double[]) f.clone(); }
    
    public double[] getFoobar() { return foobar == null ? null : (double[]) foobar.clone(); }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestPropertyChangeDispatcher test = new TestPropertyChangeDispatcher();
        PropertyChangeDispatcher pcd = new PropertyChangeDispatcher(test, BasicSimEntity.class);
        SimplePropertyDumper dumper = new SimplePropertyDumper();
        pcd.addPropertyChangeListener(dumper);
        test.addPropertyChangeListener(dumper);
        
        pcd.setProperty("foo", new Integer(11));
        pcd.setProperty("foobar", new double[] { 3.14, 1.59 } );
        
        System.out.println(pcd);
        System.out.println(test.getFoo());
        
        pcd.setProperty("bar", "Hi there!");
        pcd.setProperty("baz", new int[] { 1, 2, 3 });
        System.out.println(pcd);
        
        pcd.clearAddedProperties();
        System.out.println(pcd);
        
        test.setProperty("foo", new Integer(42));
        System.out.println(test);
        
    }
    
}
