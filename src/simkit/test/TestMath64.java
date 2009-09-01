package simkit.test;

import simkit.random.RandomVariateFactory;
import simkit.util.Math64;

/**
 * This is the test that fails on 64-bit platforms when using Math.log.
 * @version $Id$
 * @author ahbuss
 */
public class TestMath64 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        double n = Integer.parseInt("17197");
        System.out.println("n=" + n);
        double d = Math64.log(n);
        for (int i = 0; i < 100000; i++) {
            double e = Math64.log(n);
            if (e != d) {
                System.err.println("ERROR after " + i + " iterations:\n" +
                        "previous value: " + d + " (" +
                        Long.toHexString(Double.doubleToLongBits(d)) + ")\n" +
                        " current value: " + e + " (" +
                        Long.toHexString(Double.doubleToLongBits(e)) + ")");
                System.exit(1);
            }
        }
        System.err.println("SUCCESS!");

        for (int i = 0; i < 10; ++i) {
            double x = RandomVariateFactory.getDefaultRandomNumber().draw();
            double y = Math.log(x);
            double z = Math64.log(x);
            System.out.println(x + "\t" + y + "\t" + z + "\t" + (y - z));
        }

        System.out.println("---------------");

        for (double x = 0.0; x <= 1.0; x += 0.001) {
            double y = Math64.log(x);
            double z = Math.log(x);
            System.out.println(x + "\t" + y + "\t" + z + "\t" + (y - z));
        }
        for (double x = 1.0; x <= 10.0; x += 0.01) {
            double y = Math64.log(x);
            double z = Math.log(x);
            System.out.println(x + "\t" + y + "\t" + z + "\t" + (y - z));
        }

        int iter = 100000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < iter; ++i) {
            double x = Math64.log(i);
        }
        long end = System.currentTimeMillis();
        long time1 = end - start;
        System.out.println();

        start = System.currentTimeMillis();
        for (int i = 0; i < iter; ++i) {
            double x = Math.log(i);
        }
        end = System.currentTimeMillis();
        long time2 = end - start;


        System.out.println("\nfor " + iter + " iterations: Math64.log(): " + time1 + " [" +
                (double) time1 / iter + "]\tMath.log(): " + time2 );

    }

}
