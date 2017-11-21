package simkit.stat;

import junit.framework.*;

public class SimpleStatsTallyTest extends TestCase {

    private SimpleStatsTally tally;

    @Override
    public void setUp() {
        tally = new SimpleStatsTally();
    }

    @Override
    public void tearDown() {
    }

    public void testManyIdenticalObservations() {
        System.out.println("Many Identical Observations");
        for (int i = 0; i < 1000; i++) {
            tally.newObservation(1.0);
        }
        assertEquals(1000, tally.getCount());
        assertEquals(1.0, tally.getMinObs());
        assertEquals(1.0, tally.getMaxObs());
        assertEquals(1.0, tally.getMean());
        assertEquals(0.0, tally.getVariance());
        assertEquals(0.0, tally.getStandardDeviation());
    }

    public void testCopyConstructor() {
        System.out.println("Copy Constructor");

        for (int i = 0; i < 1000; ++i) {
            tally.newObservation(i);
        }

        SimpleStatsTally copy = new SimpleStatsTally(tally);
        String expected = tally.getDataLine();
        assertEquals(expected, copy.getDataLine());

        double anotherObservation = 250.0;
        
        tally.newObservation(anotherObservation);
        copy.newObservation(anotherObservation);
        expected = tally.getDataLine();
        assertEquals(expected, copy.getDataLine());
    }
}
