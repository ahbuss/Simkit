package simkit.test;
import simkit.*;
/**
 * @version $Id$
 * @author  ahbuss
 */
public class TestAddedProperties extends SimEntityBase {
    
    private int foobar;
    
    public void setFoobar(int f)  {foobar = f; }
    
    public int getFoobar() { return foobar; }
    
    public static void main(String[] args) {
        SimEntity taps = new TestAddedProperties();
        
        taps.setProperty("foo", "Bar");
        taps.setProperty("one", new Integer(1));
        taps.setProperty("array", new double[] { 1, 2, 3.4 });
        
        System.out.println(taps);
        
        String[] added = taps.getAddedProperties();
        
        for (int i = 0; i < added.length; ++i) {
            System.out.println(added[i] + " -> " + taps.getProperty(added[i]));
        }
    }
    
}
