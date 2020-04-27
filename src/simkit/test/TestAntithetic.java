/*
 * TestAntithetic.java
 *
 * Created on March 15, 2002, 4:41 PM
 */

package simkit.test;
import simkit.random.RandomNumber;
import simkit.random.RandomNumberFactory;

/**
 * Output:
 * <pre>
 0.1826699401717633	0.8173300598282367	0.1826699401717633
9.391005150973797E-4	0.9990608994849026	9.391005150973797E-4
0.489645627560094	0.510354372439906	0.489645627560094
0.8684670901857316	0.13153290981426835	0.8684670901857316
0.9645836516283453	0.03541634837165475	0.9645836516283453
0.007530465722084045	0.992469534277916	0.007530465722084045
0.374291296582669	0.625708703417331	0.374291296582669
0.9374080542474985	0.06259194575250149	0.9374080542474985
0.5892894889693707	0.4107105110306293	0.5892894889693707
0.8652263251133263	0.1347736748866737	0.8652263251133263
 * </pre>
 * 
 * @author  Arnold Buss
 * 
 */
public class TestAntithetic {

    /** Creates new TestAntithetic */
    public TestAntithetic() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        RandomNumber rng = RandomNumberFactory.getInstance();
        RandomNumber anti = RandomNumberFactory.getAntithetic(rng);
        RandomNumber copy = RandomNumberFactory.getInstance();

        for (int i = 0; i < 10; i++) {
            double x = copy.draw();
            System.out.println(anti.draw() + "\t" + x + "\t" + (1.0 - x));
        }
    }
}