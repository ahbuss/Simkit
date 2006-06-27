
package simkit;

import junit.framework.*;

import java.util.*;

public class PropertyChangeDispatcherTest extends TestCase {
    
    
    public void setUp() {
    }

    public void tearDown() {
    }

/**
* Make sure the constructor only finds public setters and getters.
**/
    public void testPublicMethods() {
        TestClass testClass = new TestClass();
        PropertyChangeDispatcher dis = new PropertyChangeDispatcher(testClass, Object.class);
        assertNotNull(dis.getProperty("public"));
        assertNull(dis.getProperty("protected"));
        assertNull(dis.getProperty("private"));
        assertNull(dis.getProperty("nothing"));

        dis.setProperty("public", new Object());
        dis.setProperty("protected", new Object());
        dis.setProperty("private", new Object());
        dis.setProperty("nothing", new Object());

        String[] props = dis.getAddedProperties();
        List propsList = Arrays.asList(props);
        assertFalse(propsList.contains("public"));
        assertTrue(propsList.contains("protected"));
        assertTrue(propsList.contains("private"));
        assertTrue(propsList.contains("nothing"));
        
    }

    public static class TestClass {

        public void setPublic(Object i) {}

        public Object getPublic() {return new Object();}

        protected void setProtected(Object i) {}

        protected Object getProtected() {return new Object();}
    
        private void setPrivate(Object i) {}

        private Object getPrivate() {return new Object();}

        void setNothing(Object i) {}

        Object getNothing() {return new Object();}
    }
}
