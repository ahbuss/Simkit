package simkit.random;

import junit.framework.*;

public class LogNormalTest extends TestCase {

    public static final double EPSILON = 1.0E-10;

    public void setUp() {
    }

    public void tearDown() {
    }

    public void testGetLogNormalParameters1() {
        double[] ans = LogNormalVariate.getLogNormalParameters(0.0, 1.0);
        assertEquals(1.64872127070013, ans[0], EPSILON);
        assertEquals(2.16119741589509, ans[1], EPSILON);
    }

    public void testGetLogNormalParameters2() {
        double[] ans = LogNormalVariate.getLogNormalParameters(0.2, 0.8);
        assertEquals(1.68202764969889, ans[0], EPSILON);
        assertEquals(1.59258875946379, ans[1], EPSILON);
    }

    public void testGetLogNormalParameters3() {
        double[] ans = LogNormalVariate.getLogNormalParameters(2.0, 2.0);
        assertEquals(54.5981500331442, ans[0], EPSILON);
        assertEquals(399.717191814365, ans[1], EPSILON);
    }

    public void testGetLogNormalParameters4() {
        double[] ans = LogNormalVariate.getLogNormalParameters(-0.2, 2.0);
        assertEquals(6.04964746441295, ans[0], EPSILON);
        assertEquals(44.2899273047545, ans[1], EPSILON);
    }

    public void testGetNormalParameters1() {
        double[] ans = LogNormalVariate.getNormalParameters(1.0, 1.0);
        assertEquals(-0.346573590279973, ans[0], EPSILON);
        assertEquals(0.832554611157698, ans[1], EPSILON);
    }

    public void testGetNormalParameters2() {
        double[] ans = LogNormalVariate.getNormalParameters(2.0, 2.0);
        assertEquals(0.346573590279973, ans[0], EPSILON);
        assertEquals(0.832554611157698, ans[1], EPSILON);
    }

    public void testGetNormalParameters3() {
        double[] ans = LogNormalVariate.getNormalParameters(0.5, 0.2);
        assertEquals(-0.767357183119082, ans[0], EPSILON);
        assertEquals(0.385253170159927, ans[1], EPSILON);
    }

    public void testGetNormalParameters4() {
        double[] ans = LogNormalVariate.getNormalParameters(10.0, 2.0);
        assertEquals(2.28297473641741, ans[0], EPSILON);
        assertEquals(0.198042200435365, ans[1], EPSILON);
    }

    public void testRoundTripA1() {
        double[] ans = LogNormalVariate.getLogNormalParameters(0.0, 1.0);
        ans = LogNormalVariate.getNormalParameters(ans[0], ans[1]);
        assertEquals(0.0, ans[0], EPSILON);
        assertEquals(1.0, ans[1], EPSILON);
    }

    public void testRoundTripA2() {
        double[] ans = LogNormalVariate.getLogNormalParameters(0.2, 0.8);
        ans = LogNormalVariate.getNormalParameters(ans[0], ans[1]);
        assertEquals(0.2, ans[0], EPSILON);
        assertEquals(0.8, ans[1], EPSILON);
    }

    public void testRoundTripA3() {
        double[] ans = LogNormalVariate.getLogNormalParameters(2.0, 2.0);
        ans = LogNormalVariate.getNormalParameters(ans[0], ans[1]);
        assertEquals(2.0, ans[0], EPSILON);
        assertEquals(2.0, ans[1], EPSILON);
    }

    public void testRoundTripA4() {
        double[] ans = LogNormalVariate.getLogNormalParameters(-0.2, 2.0);
        ans = LogNormalVariate.getNormalParameters(ans[0], ans[1]);
        assertEquals(-0.2, ans[0], EPSILON);
        assertEquals(2.0, ans[1], EPSILON);
    }

    public void testRoundTripB1() {
        double[] ans = LogNormalVariate.getNormalParameters(1.0, 1.0);
        ans = LogNormalVariate.getLogNormalParameters(ans[0], ans[1]);
        assertEquals(1.0, ans[0], EPSILON);
        assertEquals(1.0, ans[1], EPSILON);
    }

    public void testRoundTripB2() {
        double[] ans = LogNormalVariate.getNormalParameters(2.0, 2.0);
        ans = LogNormalVariate.getLogNormalParameters(ans[0], ans[1]);
        assertEquals(2.0, ans[0], EPSILON);
        assertEquals(2.0, ans[1], EPSILON);
    }

    public void testRoundTripB3() {
        double[] ans = LogNormalVariate.getNormalParameters(0.5, 0.2);
        ans = LogNormalVariate.getLogNormalParameters(ans[0], ans[1]);
        assertEquals(0.5, ans[0], EPSILON);
        assertEquals(0.2, ans[1], EPSILON);
    }

    public void testRoundTripB4() {
        double[] ans = LogNormalVariate.getNormalParameters(10.0, 2.0);
        ans = LogNormalVariate.getLogNormalParameters(ans[0], ans[1]);
        assertEquals(10.0, ans[0], EPSILON);
        assertEquals(2.0, ans[1], EPSILON);
    }
}
        
