
package simkit.stat;

import junit.framework.*;

public class SimpleStatsTallyTest extends TestCase {

    private SimpleStatsTally tally;
    
    public void setUp() {
        tally = new SimpleStatsTally();
    }

    public void tearDown() {
    }

    public void testManyIdenticalObservations() {
        for(int i=0;i<1000;i++){
            tally.newObservation(1.0);
        }
        assertEquals(1000, tally.getCount());
        assertEquals(1.0, tally.getMinObs());
        assertEquals(1.0, tally.getMaxObs());
        assertEquals(1.0, tally.getMean());
        assertEquals(0.0, tally.getVariance());
        assertEquals(0.0, tally.getStandardDeviation());
    }
 }
