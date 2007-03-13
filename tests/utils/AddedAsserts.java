
package utils;

import junit.framework.Assert;
import java.beans.PropertyChangeEvent;
/**
* Used to hold added junit assertions.
**/
public class AddedAsserts extends Assert {

/**
* Compares two files, failing a junit assertEquals if they are different
**/
    public static void assertSameFile(String file1, String file2) {
        FileCompare.assertSameFile(file1, file2);
    }

/**
* Compares two sources of XML. A file is the expected value, and a String is the tested value.
**/
    public static void assertSameXml(String file, String string) {
        FileCompare.assertSameXml(file, string);
    }

/**
* Compares 2 double arrays for equality within the accuracy of delta.
**/
    public static void assertEqualArrays(double[] a, double[] b, double delta) {
        if (a.length != b.length) {
            fail("equalArrays with different lengths");
        }
        for (int i = 0; i < a.length; i++) {
            assertEquals("Element " + i, a[i], b[i], delta);
        }
    }

/**
* Compares 2 int arrays for equality within the accuracy of delta.
**/
    public static void assertEqualArrays(int[] a, int[] b) {
        if (a.length != b.length) {
            fail("equalArrays with different lengths");
        }
        for (int i = 0; i < a.length; i++) {
            assertEquals("Element " + i, a[i], b[i]);
        }
    }

/**
* Returns true if both objects are null or both are not null and they are equals()
**/
    public static boolean equivelentObjects(Object expectedObject, Object actualObject) {
        return ((expectedObject == null && actualObject == null) ||
            ((expectedObject != null && actualObject != null) && 
            expectedObject.equals(actualObject)));
    }

/**
* Compares 2 PropertyChangeEvents to see if they contain the same data.
**/
    public static void assertEquivalent(PropertyChangeEvent expected, PropertyChangeEvent actual) {
        assertEquivalent(expected, actual, -1);
    }

/**
* Compares 2 PropertyChangeEvents to see if they contain the same data.
**/
    public static void assertEquivalent(PropertyChangeEvent expected, 
                                        PropertyChangeEvent actual,
                                        int count) {

        boolean ok = true;
        String msg = "The following data was different: ";
        if (count >= 0) {
            msg += " (event number " + count + ")";
        }
        Object expectedObject = null;
        Object actualObject = null;
        expectedObject = expected.getSource();
        actualObject = actual.getSource();
        if (!equivelentObjects(expectedObject, actualObject)) {
            ok = false;
            msg += "\n\tExpected source= " + expectedObject + "; "
                + " Actual source=" + actualObject;
        } else {
            msg += "\n\tSource= " + actualObject;
        }
        expectedObject = expected.getPropertyName();
        actualObject = actual.getPropertyName();
        if (!equivelentObjects(expectedObject, actualObject)) {
            ok = false;
            msg += "\n\tExpected propertyName= " + expectedObject + "; "
                + " Actual propertyName=" + actualObject;
        } else {
            msg += " propertyName=" + actualObject;
        }
        expectedObject = expected.getOldValue(); 
        actualObject = actual.getOldValue();
        if (!equivelentObjects(expectedObject, actualObject)) {
            ok = false;
            msg += "\n\tExpected oldValue= " + expectedObject + "; "
                + " Actual oldValue=" + actualObject;
        } else {
            msg += " oldValue=" + actualObject;
        }
        expectedObject = expected.getNewValue();
        actualObject = actual.getNewValue();
        if (!equivelentObjects(expectedObject, actualObject)) {
            ok = false;
            msg += "\n\tExpected newValue= " + expectedObject + "; "
                + " Actual newValue=" + actualObject;
        } else {
            msg += " newValue=" + actualObject;
        }
        assertTrue(msg, ok);
    }
}
