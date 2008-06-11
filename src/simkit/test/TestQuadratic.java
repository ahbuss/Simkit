/*
 * TestQuadratic.java
 *
 * Created on March 30, 2002, 4:53 PM
 */

package simkit.test;
import java.awt.geom.QuadCurve2D;
/**
 *
 * @author  Arnold Buss
 * @version $Id: TestQuadratic.java 643 2004-04-25 22:59:33Z kastork $
 */
public class TestQuadratic {

    /** Creates new TestQuadratic */
    public TestQuadratic() {
    }

    public static String arrayToString(double[] x) {
        if( x == null) { return "null"; }
        return arrayToString(x, x.length);
    }
    
    public static String arrayToString(double[] x, int n) {
        if (x == null || n < 0 || n > x.length) { return "null"; }
        StringBuffer buf = new StringBuffer("{");
        for (int i = 0; i < n; i++) {
            buf.append(x[i]);
            if (i < n - 1) { buf.append(','); }
        }
        buf.append('}');
        return buf.toString();
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        double[] eqn= null;
        double[] sol = new double[2];
        int numRoots = 0;
        
        eqn = new double[] { 1.0, -4.0, 4.0};
        numRoots = QuadCurve2D.solveQuadratic(eqn, sol);
        System.out.println(arrayToString(eqn));
        System.out.println(numRoots + " roots");
        System.out.println(arrayToString(sol, numRoots));
        
        eqn = new double[] { 1.0, 4.0, 4.0};
        numRoots = QuadCurve2D.solveQuadratic(eqn, sol);
        System.out.println(arrayToString(eqn));
        System.out.println(numRoots + " roots");
        System.out.println(arrayToString(sol, numRoots));
        
        eqn = new double[] { 1.0, 1.0, 1.0};
        numRoots = QuadCurve2D.solveQuadratic(eqn, sol);
        System.out.println(arrayToString(eqn));
        System.out.println(numRoots + " roots");
        System.out.println(arrayToString(sol, numRoots));
        
        eqn = new double[] { 1.0, 1.0, 0.0};
        numRoots = QuadCurve2D.solveQuadratic(eqn, sol);
        System.out.println(arrayToString(eqn));
        System.out.println(numRoots + " roots");
        System.out.println(arrayToString(sol, numRoots));
        
        eqn = new double[] { 1.0, 0.0, 0.0};
        numRoots = QuadCurve2D.solveQuadratic(eqn, sol);
        System.out.println(arrayToString(eqn));
        System.out.println(numRoots + " roots");
        System.out.println(arrayToString(sol, numRoots));
        
        eqn = new double[] { 0.0, 0.0, 0.0};
        numRoots = QuadCurve2D.solveQuadratic(eqn, sol);
        System.out.println(arrayToString(eqn));
        System.out.println(numRoots + " roots");
        System.out.println(arrayToString(sol, numRoots));
    }

}
