package simkit;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import simkit.util.Misc;

/**
 * Verify that java reflection works the way we are using it.
 * 
 * @author Kirk Stork (The MOVES Institute)
 */
public class JavaReflectionTest extends TestCase {

    public JavaReflectionTest(String testName) {
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

    class SomethingToReflectUpon {

        public void foo(Double x, Integer y) {
        }

        public void baz(int x, Integer y) {
        }

        public int returnZero() {
            return 0;
        }

        protected void protectedNoOp() {
        }
    }

    public void testGetFullMethodName_Class_String_Array() {
        Object[] arg = new Object[]{new Double(1.0), new Integer(3)};
        Method[] method = SomethingToReflectUpon.class.getDeclaredMethods();
//        for (int i = 0; i < method.length; i++) {
//            System.out.println(method[i] + ": " + method[i].getName());
//        }

        // simkit.util.Misc reflects on the given class and produces
        // a string representation of the method if it exists.  Notice, the
        // arguments must be objects, not primative types

        String name = Misc.getFullMethodName(SomethingToReflectUpon.class,
                "foo", arg);
//        System.out.println(name);
        assertEquals(
                "simkit.JavaReflectionTest$SomethingToReflectUpon.foo(java.lang.Double,java.lang.Integer)", name);
        try {
            name = Misc.getFullMethodName(SomethingToReflectUpon.class, "bar", arg);
//            System.out.println(name);
            fail("should have received an exception");
        } catch (RuntimeException e) {
            assertSame(java.lang.NoSuchMethodException.class, e.getCause().getClass());
        }

        // only for methods that return void ...

        try {
            name = Misc.getFullMethodName(SomethingToReflectUpon.class, "returnZero", arg);
//            System.out.println(name);
            fail("should have received an exception");
        } catch (RuntimeException e) {
            assertSame(java.lang.NoSuchMethodException.class, e.getCause().getClass());
        }

        // and only for methods that are public

        try {
            name = Misc.getFullMethodName(SomethingToReflectUpon.class, "protectedNoOp", arg);
//            System.out.println(name);
            fail("should have received an exception");
        } catch (RuntimeException e) {
            assertSame(java.lang.NoSuchMethodException.class, e.getCause().getClass());
        }

    }

    public void testGetFullMethodName_String_Array() {
        Object[] arg = new Object[]{new Double(1.0), new Integer(3)};
        Method[] method = SomethingToReflectUpon.class.getDeclaredMethods();
        String name;


        // There is no bar method anywhere, but simkit.util.Misc can still create
        // a string representation that includes argumement type information.

        name = Misc.getFullMethodName("bar", arg);
        assertEquals(
                "bar(class java.lang.Double,class java.lang.Integer)", name);



    }
    
    public void testsSimpleReflection() {
        Method[] method = SomethingToReflectUpon.class.getDeclaredMethods();

        Map<String, Method> methodMap = new HashMap<>();
        for (Method m : method) {
            methodMap.put(m.getName(), m);
        }
        
        Class<?> intClass = int.class;
        int x = 1;
        Integer y = x;

        assertFalse(intClass.isAssignableFrom(y.getClass()));

        assertTrue(intClass.isAssignableFrom(Integer.TYPE));

        assertTrue(Integer.TYPE.isAssignableFrom(intClass));
        System.out.println();


        assertEquals(4, method.length);

        Map<String, String> fullNameMap = new HashMap<>();
        fullNameMap.put("foo", "public void simkit.JavaReflectionTest$Something" +
                "ToReflectUpon.foo(java.lang.Double,java.lang.Integer)");
        fullNameMap.put("baz", "public void simkit.JavaReflectionTest$Something" +
                "ToReflectUpon.baz(int,java.lang.Integer)");
        fullNameMap.put("returnZero", "public int simkit.JavaReflectionTest$Something" +
                "ToReflectUpon.returnZero()");
        fullNameMap.put("protectedNoOp", "protected void simkit.JavaReflectionTest$Something" +
                "ToReflectUpon.protectedNoOp()");
        
        for (String name : fullNameMap.keySet()) {
            Method m = methodMap.get(name);
            String fullName = fullNameMap.get(name);
            assertEquals(fullName, m.toString());
            assertEquals(name, m.getName());
        }
        

//        System.out.println();
//        System.out.println(SomethingToReflectUpon.class.getName());
        assertEquals("simkit.JavaReflectionTest$SomethingToReflectUpon",
                SomethingToReflectUpon.class.getName());
    }
}
