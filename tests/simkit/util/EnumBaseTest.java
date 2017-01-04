package simkit.util;

import java.lang.reflect.Constructor;
import junit.framework.TestCase;

/**
 *
 * @author ahbuss
 */
public class EnumBaseTest extends TestCase {

    public EnumBaseTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        EnumBase.clear();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of find method, of class EnumBase.
     */
    public void testFind() {
        System.out.println("find");
        String name = "Test";
        Class<? extends EnumBase> clazz = EnumBaseImpl.class;
        EnumBase expResult = null;
        EnumBase result = EnumBase.find(name, clazz);
        assertEquals(expResult, result);

        expResult = new EnumBaseImpl(name);
        result = EnumBase.find(name, clazz);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class EnumBase.
     */
    public void testEquals() {
        System.out.println("equals");
        String name = "Test";
        Object o = new EnumBaseImpl(name);
        EnumBase instance = EnumBase.find(name, EnumBaseImpl.class);
        boolean expResult = true;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
        
        o = new EnumBaseImpl2(name);
        result = instance.equals(o);
        assertNotSame(expResult, result);
    }

    /**
     * Test of compareTo method, of class EnumBase.
     */
    public void testCompareTo() {
        System.out.println("compareTo");
        EnumBase e = new EnumBaseImpl("One");
        EnumBase instance = new EnumBaseImpl("Two");
        int expResult = 1;
        int result = instance.compareTo(e);
        assertEquals(expResult, result);

        EnumBase instance2 = new EnumBaseImpl2("One");
        try {
            result = instance2.compareTo(e);
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException ex) {
        }

    }

    /**
     * Test of clear method, of class EnumBase.
     */
    public void testClear_0args() {
        System.out.println("clear");
        int expectedNumber = 10;
        for (int i = 0; i < expectedNumber; ++i) {
            EnumBase newInstance = new EnumBaseImpl(Integer.toString(i));
        }
        int result = EnumBase.types.size();
        assertEquals(1, result);
        result = EnumBase.types.get(EnumBaseImpl.class).size();
        assertEquals(expectedNumber, result);
        EnumBase.clear();
        assertEquals(0, EnumBase.getMembers(EnumBaseImpl.class).size());
    }

    /**
     * Test of clear method, of class EnumBase.
     */
    public void testClear_Class() {
        System.out.println("clear(Class<?>)");
        int expectedNumber = 10;
        for (int i = 0; i < expectedNumber; ++i) {
            EnumBase newInstance = new EnumBaseImpl(Integer.toString(i));
        }

        for (int i = 0; i < expectedNumber; ++i) {
            EnumBase newInstance = new EnumBaseImpl2("Type II " + i);
        }
        Class<? extends EnumBase> clazz = EnumBaseImpl.class;
        EnumBase.clear(clazz);
        assertEquals(0, EnumBase.getMembers(EnumBaseImpl.class).size());
        assertEquals(expectedNumber, EnumBase.getMembers(EnumBaseImpl2.class).size());
    }

    /**
     * Test of findOrCreate method, of class EnumBase.<br>
     * Note: The inner classes did not work with reflection in getting
     * the constructors
     */
    public void testFindOrCreate() {
        System.out.println("findOrCreate");
        
        String name = "Test";
        Class<? extends EnumBase> clazz = EnumBaseExample.class;

        EnumBase result = EnumBase.findOrCreate(name, clazz);
        assertNotNull(result);
        
        EnumBase expected = EnumBase.find(name, clazz);
        assertEquals(expected, result);
    }

    class EnumBaseImpl extends EnumBase {

        public EnumBaseImpl(String name) {
            super(name);
        }
    }

    class EnumBaseImpl2 extends EnumBase {

        public EnumBaseImpl2(String name) {
            super(name);
        }
    }

}
