package simkit.smd;

/**
 *  Solves quadratic formulae.  The size of the returned array is information
 *  about the solution
 *  <P> length 1 means the solution is degenerate in some fashion.
 *  <P> length 2 means real solutions
 *  <P> length 3 means complex solutions, with the real part in [0] and the
 *      imaginary part in [1].
 *
 *  @author Arnold Buss
 *  @version 0.5.0
**/

public class Quadratic {

// class variables

   public static final double NEARLY_ZERO = 1.0e-14;

// class methods
   public static double[] solve(double a, double b, double c) {

      double solution[] = null;
      
      if (Math.abs(a) < NEARLY_ZERO) {
            solution = new double[1];
            solution[0] = -c / b;
      }
      else {
         double determinant = b * b - 4.0 * a * c;
         if (determinant >= 0.0) {
            solution = new double[2];
            solution[0] = 0.5 * (- b - Math.sqrt(determinant))/a;
            solution[1] = 0.5 * (- b + Math.sqrt(determinant))/a;
         }
         else {
            solution = new double[3];
            solution[0] = 0.5 * b / a;
            solution[1] = 0.5 * Math.sqrt(-determinant) / a;

         }
      }
      return solution;
   }

// main method -- unit test
   public static void main(String[] args) {
      double[] x;
// All coefficients are 0.0 -- solution should be NaN
      x = Quadratic.solve(0.0, 0.0, 0.0);
      for (int i = 0; i < x.length; i++) {  
         System.out.print(x[i] + " ");
      }
      System.out.println();
// Just constant is nonzero -- solution should be -Infinity (-0.0 / 1.0)
      x = Quadratic.solve(0.0, 0.0, 1.0);
      for (int i = 0; i < x.length; i++) {  
         System.out.print(x[i] + " ");
      }
      System.out.println();
// Complex solutions -- array should be length 3 with values (0.0, 2.0, 0.0).
      x = Quadratic.solve(1.0, 0.0, 4.0);
      System.out.println(x + " " + x.length);
      for (int i = 0; i < x.length; i++) {  
         System.out.print(x[i] + " ");
      }
      System.out.println();
// Linear with finite solution -- solution should be - 1.0 / 3.2
      x = Quadratic.solve(0.0, 3.2, 1.0);
      System.out.println(x + " " + x.length);
      for (int i = 0; i < x.length; i++) {  
         System.out.print(x[i] + " ");
      }
      System.out.println();
// Two solutions, one negative -- should be -1/2 and 2/3
      x = Quadratic.solve(6.0, -1.0, -2.0);
      System.out.println(x + " " + x.length);
      for (int i = 0; i < x.length; i++) {  
         System.out.print(x[i] + " ");
      }
      System.out.println();
// The size of the x^2 coefficient should result in a linear solution (- 2.0 / 1.0)
      x = Quadratic.solve(1.0e-30, -1.0, -2.0);
      System.out.println(x + " " + x.length);
      for (int i = 0; i < x.length; i++) {  
         System.out.print(x[i] + " ");
      }
      System.out.println();
   }
}
/*output:
> java simkit.smd.Quadratic
NaN
-Infinity
[D@f3d2e0de 3
0.0 2.0 0.0
[D@f2a2e0de 1
-0.3125
[D@f302e0de 2
-0.5 0.6666666666666666
[D@f2cee0de 1
-2.0     
*/