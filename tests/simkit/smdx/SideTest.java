/*
 */
package simkit.smdx;

import junit.framework.TestCase;

/**
 *
 * @author Kirk Stork (The MOVES Institute)
 */
public class SideTest extends TestCase {

    public SideTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getSide method, of class Side.
     */
    public void testGetSide() {
        System.out.println("getSide");
        assertSame("Red", Side.RED.getSide());
        assertSame("Blue", Side.BLUE.getSide());
        assertSame("Orange", Side.ORANGE.getSide());
        assertSame("Purple", Side.PURPLE.getSide());
        assertSame("Green", Side.GREEN.getSide());
        assertSame("White", Side.WHITE.getSide());
    }

    /**
     * Test of toString method, of class Side.
     */
    public void testToString() {
        System.out.println("toString");

        assertSame("Red", Side.RED.toString());
        assertSame("Blue", Side.BLUE.toString());
        assertSame("Orange", Side.ORANGE.toString());
        assertSame("Purple", Side.PURPLE.toString());
        assertSame("Green", Side.GREEN.toString());
        assertSame("White", Side.WHITE.toString());
    }

    /**
     * Test of getSideFor method, of class Side.
     */
    public void testGetSideFor() {
        System.out.println("getSideFor");
        assertSame(Side.RED, Side.getSideFor("red"));
        assertSame(Side.RED, Side.getSideFor("Red"));
        assertSame(Side.BLUE, Side.getSideFor("blue"));
        assertSame(Side.BLUE, Side.getSideFor("Blue"));
        assertSame(Side.ORANGE, Side.getSideFor("orange"));
        assertSame(Side.ORANGE, Side.getSideFor("Orange"));
        assertSame(Side.PURPLE, Side.getSideFor("purple"));
        assertSame(Side.PURPLE, Side.getSideFor("Purple"));
        assertSame(Side.GREEN, Side.getSideFor("green"));
        assertSame(Side.GREEN, Side.getSideFor("Green"));
        assertSame(Side.WHITE, Side.getSideFor("white"));
        assertSame(Side.WHITE, Side.getSideFor("White"));
        assertNull(Side.getSideFor("Magenta"));
        assertSame(NewSide.MAGENTA, NewSide.getSideFor("Magenta"));
        assertSame(NewSide.CHARTRUESE, NewSide.getSideFor("Chartruese"));
    }
}