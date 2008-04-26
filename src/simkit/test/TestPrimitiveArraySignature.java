package simkit.test;
import simkit.Schedule;
import simkit.SimEntityBase;
/**
 * This class demonstrates that arrays of primitives (or ints at least)
 * do in fact work as signatures to do methods in SimEntityBase.
 * @author  Arnold Buss
 */
public class TestPrimitiveArraySignature extends SimEntityBase {
    
    private int[] startArray;
    
/**
 * @param start The initial array
 */
    public TestPrimitiveArraySignature(int[] start) {
        startArray = (int[]) start.clone();
    }
    
    public void doRun() {
        waitDelay("This", 0.0,  startArray );
    }

/**
 * Increment each element of the array by one (just to do something);
 * Schedule the next "This" event with the updated array in 1.1 
 * time units.
 * @param x The passed-in array.
 */
    public void doThis(int[] x) {
        for (int i = 0; i < x.length; ++i) {
            x[i]++;
        }
        System.out.println( intArrayToString(x) );
        waitDelay("This", 1.1,  x  );
        if ( x.length > 0) {
            waitDelay("That", 0.5, new Integer(x[0]));
        }
        else {
            waitDelay("That", 0.7, new Integer(-1));
        }
    }
    
/**
 * @param arg the primitive integer argument.
*/    
    public void doThat(int arg) {
        System.out.println(arg);
    }
/**
 * Helper method to dump int[] arrays to a String.  The format is
 * "{array[0], array[1], ..., array[length -1]}"
 * @param array The array to be dumped.
 */    
    protected static String intArrayToString(int[] array) {
        StringBuffer buf = new StringBuffer("{");
        for (int i = 0; i < array.length; ++i) {
            buf.append(array[i]);
            if (i < array.length - 1) { buf.append(", "); }
        }
        buf.append("}");
        return buf.toString();
    }
    
    public static void main(String[] args) {
        SimEntityBase tas = new TestPrimitiveArraySignature(new int[] { 1, 2, 3 });
        tas.setVerbose(true);
        Schedule.setVerbose(true);
        Schedule.stopOnEvent(10, "This", int[].class );
        Schedule.reset();
        Schedule.startSimulation();
    }
    
}
