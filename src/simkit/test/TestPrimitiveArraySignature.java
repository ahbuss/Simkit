package simkit.test;

import java.util.Arrays;
import simkit.Schedule;
import simkit.SimEntityBase;

/**
 * This class demonstrates that arrays of primitives (or ints at least) do in
 * fact work as signatures to do methods in SimEntityBase.
 *
 * @author Arnold Buss
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
        waitDelay("This", 0.0, startArray);
    }

    /**
     * Increment each element of the array by one (just to do something);
     * Schedule the next "This" event with the updated array in 1.1 time units.
     *
     * @param x The passed-in array.
     */
    public void doThis(int[] x) {
        for (int i = 0; i < x.length; ++i) {
            x[i]++;
        }
        System.out.println(Arrays.toString(x));
        waitDelay("This", 1.1, x);
        if (x.length > 0) {
            waitDelay("That", 0.5, x[0]);
        } else {
            waitDelay("That", 0.7, -1);
        }
    }

    /**
     * @param arg the primitive integer argument.
     */
    public void doThat(int arg) {
        System.out.println(arg);
    }

    public static void main(String[] args) {
        SimEntityBase tas = new TestPrimitiveArraySignature(new int[]{1, 2, 3});
        tas.setVerbose(true);
        Schedule.setVerbose(true);
        Schedule.stopOnEvent(10, "This", int[].class);
        Schedule.reset();
        Schedule.startSimulation();
    }

}
