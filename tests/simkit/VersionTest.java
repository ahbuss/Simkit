package simkit;

import junit.framework.TestCase;

/**
 *
 * @author Kirk Stork (The MOVES Institute)
 */
public class VersionTest extends TestCase {
    
    private static final int MAJOR_VERSION = 1;
    private static final int MINOR_VERSION = 5;
    private static final int SUB_MINOR_VERSION = 3;
    
    public VersionTest(String testName) {
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
    
    public void testVersionNumbers() {
        assertEquals("Major version number has changed.", MAJOR_VERSION, Version.getVersionNumber());
        assertEquals("Compatibility version number has changed.", MINOR_VERSION, Version.getSubVersionNumber());
        assertEquals("Minor version number has changed", SUB_MINOR_VERSION, Version.getSubSubVersionNumber());
    }
    
    public void testAlLeastVersion() {
        assertTrue(Version.isAtLeastVersion("1.0.0"));
        assertTrue(Version.isAtLeastVersion("1.3.0"));
        assertTrue(Version.isAtLeastVersion("1.3.5"));
        assertTrue(Version.isAtLeastVersion("1.3.8"));
        assertTrue(Version.isAtLeastVersion("1.4.0"));
        assertTrue(Version.isAtLeastVersion("1.5.0"));
    }

}
