package simkit.test;
import simkit.*;
/**
 *
 * @author  Arnold Buss
 */
public class TestSimEntityBaseProtected extends SimEntityBaseProtected {
    
    public TestSimEntityBaseProtected() {
    }
    
    public void doRun() {
        waitDelay("This", 0.5, new Integer(2));
    }
    
    protected void doThis(int y) {
        System.out.println(y);
        waitDelay("This", 0.6, new Integer(y + 1));
    }
    
    protected void doThat(double[] arg) {
        System.out.println(doubleArrayToString(arg));
    }
    
    protected String doubleArrayToString(double[] x) {
        StringBuffer buf = new StringBuffer("{");
        for (int i = 0; i < x.length; ++i) {
            buf.append(x[i]);
            if (i < x.length - 1) { buf.append(", "); }
        }
        buf.append('}');
        return buf.toString();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestSimEntityBaseProtected tnsep =
            new TestSimEntityBaseProtected();
        Schedule.setVerbose(true);
        Schedule.stopAtTime(10.0);
        Schedule.reset();
        tnsep.waitDelay("That", 1.5, new Object[] { new double[] { 1.2, 3.4 } });
        Schedule.startSimulation();
    }
    
}
