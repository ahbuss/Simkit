/*
 * TestCubic.java
 *
 * Created on April 5, 2002, 3:52 AM
 */

package simkit.smdx.test;
import java.awt.geom.*;
import java.util.*;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class TestCubic {

    /** Creates new TestCubic */
    public TestCubic() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        double[] coeff = new double[4];
        coeff[0] = -1.0;
        coeff[1] = 1.0;
        coeff[2] = -1.0;
        coeff[3] = 1.0;
        
        double[] sol = new double[3];
        
        int num = CubicCurve2D.solveCubic(coeff, sol);
        
        Arrays.sort(sol);
        System.out.println(num + " solutions:");
        System.out.println(simkit.smdx.Math2D.arrayToString(sol));
        
    }

}
