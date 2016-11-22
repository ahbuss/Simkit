package utils;

import junit.framework.Assert;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.*;

/**
 * Given a List of expected property changes, will fail an assertion if they
 * don't occur in the correct order.
*
 */
public class PropertyChangeAccumulator extends Assert implements PropertyChangeListener {

    protected List<PropertyChangeEvent> expected = new ArrayList<>();
    protected int count = 0;
    protected String propertyName = null;

    /**
     * @return length of the expected List.
     */
    public int size() {
        return expected.size();
    }

    /**
     * Constructs a new one that only listens for the given property.
     * @param property given property
     */
    public PropertyChangeAccumulator(String property) {
        propertyName = property;
    }

    /**
     * Constructs an accumulator that will listen for all properties.
*
     */
    public PropertyChangeAccumulator() {
    }

    /**
     * Provides the List of expected property change events.
     * @param list given List
     */
    public void setExpected(List<PropertyChangeEvent> list) {
        expected = list;
    }

    /**
     * Appends a new PropertyChangeEvent to the end of the expected List.
     * @param event Given PropertyChangeEvent
     */
    public void addExpected(PropertyChangeEvent event) {
        expected.add(event);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (propertyName == null || propertyName.equals(event.getPropertyName())) {
            count++;
            PropertyChangeEvent expectedEvent = null;
            try {
                expectedEvent = expected.remove(0);
            } catch (IndexOutOfBoundsException e) {
                fail("More events occured than were expected. The extra event was:\n"
                        + "source=" + event.getSource() + "\npropertyName="
                        + event.getPropertyName() + "\noldValue=" + event.getOldValue()
                        + "\nnewValue=" + event.getNewValue());
            }
            AddedAsserts.assertEquivalent(expectedEvent, event, count);
        }
    }

    /**
     * Checks to make sure that all expected events have occurred.
*
     */
    public void done() {
        assertEquals("There were leftover expected events:" + expected, 0, expected.size());
    }
}
