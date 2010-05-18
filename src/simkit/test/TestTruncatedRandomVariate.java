package simkit.test;

import simkit.random.RandomNumberFactory;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 * Test of truncated classes - one specifically for the Normal truncated at 0.0
 * and one for any distribution and truncation point. By using a separate
 * instance of RandomNumber, the resuts should be identical when the
 * general case is set to the values of the specific.
 * <pre>
Normal (0.0, 3.0) (Truncated at 0.0) <--> TruncatedNormal (0.0, 3.0)
--------------------------------------------------------------------
0.09305861337221685 <--> 0.09305861337221685
0.5082776845075069 <--> 0.5082776845075069
0.0 <--> 0.0
0.0 <--> 0.0
0.0 <--> 0.0
2.4074610718320075 <--> 2.4074610718320075
1.6719099767014607 <--> 1.6719099767014607
0.0 <--> 0.0
0.0 <--> 0.0
0.2427369353452125 <--> 0.2427369353452125
</pre>
 * @version $Id$
 * @author ahbuss
 */
public class TestTruncatedRandomVariate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomVariate rv = RandomVariateFactory.getInstance("Normal", 0.0, 3.0);
        double truncationPoint = 0.0;

        RandomVariate truncated = RandomVariateFactory.getInstance("Truncated", rv,
                truncationPoint);

        RandomVariate truncated2 = RandomVariateFactory.getInstance("TruncatedNormal", 0.0, 3.0);
        System.out.println(truncated + " <--> " + truncated2);
        System.out.println("--------------------------------------------------------------------");
        truncated2.setRandomNumber(RandomNumberFactory.getInstance());

        for (int i = 0; i < 10; ++i) {
            System.out.println(truncated.generate() + " <--> " +
                    truncated2.generate());
        }
    }

}
