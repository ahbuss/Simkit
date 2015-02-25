package simkit.test;

import simkit.stat.StudentT;

/**
 *
 * @author ahbuss
 */
public class TestStudentT {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double p = 0.975;
        for (int n = 1; n < 10; ++n) {
            System.out.printf("p = %.3f n = %d q = %.3f%n", p, n, StudentT.getQuantile(p, n));
        }
        System.out.println("--------------");
        p = 1.0 - p;
        for (int n = 1; n < 10; ++n) {
            System.out.printf("p = %.3f n = %d q = %.3f%n", p, n, StudentT.getQuantile(p, n));
        }
        
        System.out.println(StudentT.getQuantile(0.0, 3));
        System.out.println(StudentT.getQuantile(1.0, 3));
        System.out.println(StudentT.getQuantile(0.5, 3));
    }

}
