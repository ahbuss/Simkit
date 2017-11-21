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
        double p = 1.0 - 0.01/2;
        for (int n = 0; n < 100; ++n) {
            System.out.printf("p = %.3f n = %d q = %.3f : %.3f%n", p, n, StudentT.getQuantile(p, n),
                    StudentT.getQuantile2(p, n));
        }
        System.out.println("--------------");
        p = 1.0 - p;
        for (int n = 1; n < 10; ++n) {
            System.out.printf("p = %.3f n = %d q = %.3f : %.3f%n", p, n, StudentT.getQuantile(p, n),
                    StudentT.getQuantile2(p, n));
        }
        
        System.out.println(StudentT.getQuantile(0.0, 3) + " : " + StudentT.getQuantile2(0.0, 3));
        System.out.println(StudentT.getQuantile(1.0, 3) + " : " + StudentT.getQuantile2(1.0, 3));
        System.out.println(StudentT.getQuantile(0.5, 3) + " : " + StudentT.getQuantile2(0.5, 3));
        System.out.println(StudentT.getQuantile(0.975, Integer.MAX_VALUE) + " : " 
                + StudentT.getQuantile2(0.975, Integer.MAX_VALUE));
        
        try {
            System.out.println(StudentT.getQuantile2(-0.00001, 1));
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        } finally{
            
        }
    }

}
