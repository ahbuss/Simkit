package simkit.test;

import simkit.Schedule;
import simkit.SimEntityBaseA;
import simkit.SimEventMethod;

/**
 * @author kirk
 */
public class TestSimEntityBaseA {

    public class A {}
    public class B extends A{}

    public class EntityA extends SimEntityBaseA {

        public EntityA(String name) {
            super(name);
        }

        @SimEventMethod("Arrival")
        public void foo(A a) {
            System.out.println("EntityA has arrived" + a);
            waitDelay("Arrival", 1.0, new B());
        }
        @SimEventMethod("Arrival")
        public void foo(boolean x) {
            System.out.println("bool");
        }
        @SimEventMethod("Arrival")
        public void foo(Double x) {
            System.out.println("Double");
        }
        @SimEventMethod("Arrival")
        public void foo(int x) {
            System.out.println("int");
        }

        @SimEventMethod("Run")
        public void bar() {
            waitDelay("Arrival", 1.0, new A());
        }
    }

    public class EntityB extends EntityA {
        public EntityB(String name) {
            super(name);
        }

        @SimEventMethod("Arrival")
        public void foo(int x) {
            System.out.println("int");
        }
    }

    public class EntityC extends SimEntityBaseA {
        public EntityC(String name) {
            super(name);
        }

        @SimEventMethod("Arrival")
        public void foo(int x) {
            System.out.println("int");
        }

        // this should generate a warning that there is no
        // "Arrival" SimEventMethod with signature ()
        
        @SimEventMethod("Run")
        public void brokenRun() {
            waitDelay("Arrival", 1.0);
        }

    }

    public void go() {
        SimEntityBaseA a = new EntityA("A");
        SimEntityBaseA b = new EntityB("B");
        SimEntityBaseA c = new EntityC("C");
        System.out.println("SimEventMethods in " + a.getClass().toString() +":");
        System.out.println(a.describeSimEventMethods());
        System.out.println("---------------");
        System.out.println("SimEventMethods in " + b.getClass().toString() +":");
        System.out.println(b.describeSimEventMethods());
        System.out.println("---------------");
        System.out.println("SimEventMethods in " + c.getClass().toString() +":");
        System.out.println(c.describeSimEventMethods());
        System.out.println("---------------");
        Schedule.reset();
        Schedule.setVerbose(true);
        Schedule.stopAtTime(5.0);
        Schedule.startSimulation();
    }

    public static void main (String[] args) {
        TestSimEntityBaseA m = new TestSimEntityBaseA();
        m.go();
    }
}
