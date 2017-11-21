package simkit;

import junit.framework.TestCase;

/**
 * @version $Id$
 * @author abuss
 */
public class BasicSimEventSourceTest extends TestCase {

    public BasicSimEventSourceTest(String testName) {
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
     * Test of addSimEventListener method, of class BasicSimEventSource.
     */
    public void testAddSimEventListener() {
        System.out.println("addSimEventListener");
        SimEventListener listener = null;
        BasicSimEventSource instance = new BasicSimEventSource();
        instance.addSimEventListener(listener);

        SimEventListener[] listeners = instance.getSimEventListeners();
        assertEquals(0, listeners.length);

        listener = new SimEntityBase() {
        };
        instance.addSimEventListener(listener);
        listeners = instance.getSimEventListeners();
        assertEquals(1, listeners.length);
        assertSame(listener, listeners[0]);
    }

    /**
     * Test of removeSimEventListener method, of class BasicSimEventSource.
     */
    public void testRemoveSimEventListener() {
        System.out.println("removeSimEventListener");
        SimEventListener[] listener = new SimEventListener[]{
            new SimEntityBase() {
            },
            new SimEntityBase() {
            },
            new SimEntityBase() {
            }
        };

        BasicSimEventSource instance = new BasicSimEventSource();
        for (int i = 0; i < listener.length; ++i) {
            instance.addSimEventListener(listener[i]);
        }
        instance.removeSimEventListener(listener[1]);
        SimEventListener[] listeners = instance.getSimEventListeners();
        assertEquals(2, listeners.length);
        assertSame(listener[0], listeners[0]);
        assertSame(listener[2], listeners[1]);

    }

    /**
     * Test the utilities in the Misc class related to the listener
     * pattern.
     */
    public void testMiscRemoveAllSimEventListeners() {
        System.out.println("removeSimEventListener");
        SimEventListener[] listener = new SimEventListener[]{
            new SimEntityBase() {
            },
            new SimEntityBase() {
            },
            new SimEntityBase() {
            }
        };

        BasicSimEventSource instance = new BasicSimEventSource();
        for (int i = 0; i < listener.length; ++i) {
            instance.addSimEventListener(listener[i]);
        }
        SimEventListener[] listeners = instance.getSimEventListeners();
        assertEquals(3, listeners.length);
        simkit.util.Misc.removeAllSimEventListeners(instance);
//        instance.removeSimEventListener(listener[1]);
        listeners = instance.getSimEventListeners();
        assertEquals(0, listeners.length);
//        assertSame(listener[0], listeners[0]);
//        assertSame(listener[2], listeners[1]);

    }

    /**
     * Test of notifyListeners method, of class BasicSimEventSource.
     */
    public void testNotifyListeners() {
        System.out.println("notifyListeners");
        SimEventListenerHelper listener = new SimEventListenerHelper();
        SimEvent event = new SimEvent(listener, "Foo", 0.0);
        BasicSimEventSource instance = new BasicSimEventSource();
        instance.addSimEventListener(listener);
        instance.notifyListeners(event);

        assertNotNull(listener.getLastSimEvent());

        assertSame(event, listener.getLastSimEvent());
    }

    class SimEventListenerHelper extends BasicSimEntity implements SimEventListener {

        private SimEvent lastSimEvent;

        @Override
        public void processSimEvent(SimEvent event) {
            this.lastSimEvent = event;
        }

        public SimEvent getLastSimEvent() {
            return lastSimEvent;
        }

        @Override
        public void handleSimEvent(SimEvent event) {
        }
    }
}

