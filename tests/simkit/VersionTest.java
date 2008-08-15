package simkit;

import junit.framework.TestCase;

/**
 *
 * @author Kirk Stork (The MOVES Institute)
 */
public class VersionTest extends TestCase {
    
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
        assertEquals("Major version number has changed.", 1, Version.getVersionNumber());
        assertEquals("Compatibility version number has changed.", 3, Version.getSubVersionNumber());
        assertEquals("Minor version number has changed", 7, Version.getSubSubVersionNumber());
    }
    
    public void testAlLeastVersion() {
        assertTrue(Version.isAtLeastVersion("1.0.0"));
        assertTrue(Version.isAtLeastVersion("1.3.0"));
        assertTrue(Version.isAtLeastVersion("1.3.5"));
    }

}
