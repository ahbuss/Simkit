/*
 * TestReflection.java
 *
 * Created on December 7, 2001, 3:16 PM
 */

package simkit.test;

import java.lang.reflect.*;
import simkit.util.*;
/**
 *
 * @author  ahbuss
 * @version 
 */
public class TestReflection {

    /** Creates new TestReflection */
    public TestReflection() {
    }
    
    public void foo(Double x, Integer y) {}

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) throws Throwable {
        Object[] arg = new Object[] { new Double(1.0), new Integer(3) };
        String name = Misc.getFullMethodName(TestReflection.class, "foo", arg);
        System.out.println(name);
        name = Misc.getFullMethodName("bar", arg);
        System.out.println(name);
        
    }

}
