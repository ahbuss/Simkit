/*
 * TestObjectArray.java
 *
 * Created on May 13, 2002, 11:10 AM
 */

package simkit.test;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;

/**
 *  How to dynamically create an arbitrary array using reflection.
 *  @author  Arnold Buss
 */
public class TestObjectArray {
    
    private String foo;
    
    /** Creates new TestObjectArray */
    public TestObjectArray(String foo) {
        this.foo = foo;
    }
    
    public String toString() { return "TOA [" + foo +"]"; }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Throwable{
        String[] data = {"one", "two", "buckle my", "shoe"};
        String className = args.length > 0 ? args[0] : "simkit.test.TestObjectArray";
//          Get class object for contents of array.
        Class<?> classObject = Class.forName(className);
        System.out.println(classObject);
//          Assumes that the data array will populate the array, so that's 
//          how big the array will be.
        Object array = Array.newInstance(classObject, data.length);
//          Assumes existence of a String constructor.  0-parameters constructor
//          would be useful.
        Constructor constructor = classObject.getConstructor(new Class[] { java.lang.String.class });
        System.out.println("\tlength=" + Array.getLength(array));
//          Polulate array with data (using String array - should be made more general)
        for (int i = 0; i < data.length; i++) {
            Array.set(array, i, constructor.newInstance(new Object[] { data[i] }));
        }
//          Proof that array was indeed constructed...
        for (int i = 0; i < Array.getLength(array); i++) {
            Object value = Array.get(array, i);
            System.out.println("array[" + i + "]: " + value + "\t[" + value.getClass() + "]");
        }
    }
}
/* outputs:

> java simkit.test.TestObjectArray 
 class simkit.test.TestObjectArray
        length=4
array[0]: TOA [one]        [class simkit.test.TestObjectArray]
array[1]: TOA [two]        [class simkit.test.TestObjectArray]
array[2]: TOA [buckle my]        [class simkit.test.TestObjectArray]
array[3]: TOA [shoe]        [class simkit.test.TestObjectArray]


> java simkit.test.TestObjectArray java.lang.String 
 class java.lang.String
        length=4
array[0]: one        [class java.lang.String]
array[1]: two        [class java.lang.String]
array[2]: buckle my        [class java.lang.String]
array[3]: shoe        [class java.lang.String]

> java simkit.test.TestObjectArray java.lang.Boolean 
class java.lang.Boolean
        length=4
array[0]: false        [class java.lang.Boolean]
array[1]: false        [class java.lang.Boolean]
array[2]: false        [class java.lang.Boolean]
array[3]: false        [class java.lang.Boolean]
*/